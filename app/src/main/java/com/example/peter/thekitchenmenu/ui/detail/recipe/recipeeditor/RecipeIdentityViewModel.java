package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;
import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidation.*;
import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.*;

public class RecipeIdentityViewModel
        extends
        ObservableViewModel
        implements
        DataSource.GetEntityCallback<RecipeIdentityEntity>,
        RecipeModelComposite.RecipeModelActions {

    private static final String TAG = "tkm-RecipeIdentityVM";

    private Resources resources;
    private TextValidationHandler textValidationHandler;
    private RepositoryRecipeIdentity repository;
    private TimeProvider timeProvider;
    private RecipeValidatorModelSubmission modelSubmitter;

    private String title = "";
    private String description = "";

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();

    private RecipeIdentityEntity identityEntity;
    private String recipeId;

    private boolean
            observablesUpdating,
            isCloned,
            titleValid,
            descriptionValid = true;

    public RecipeIdentityViewModel(RepositoryRecipeIdentity repository,
                                   TimeProvider timeProvider,
                                   Resources resources,
                                   TextValidationHandler textValidationHandler) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.resources = resources;
        this.textValidationHandler = textValidationHandler;
    }

    void setModelValidationSubmitter(RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (recipeId != null) {
            this.recipeId = recipeId;
            repository.getById(recipeId, this);
        } else {
            throw new RuntimeException("Recipe id cannot be null");
        }
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        isCloned = true;
        this.recipeId = newRecipeId;
        repository.getById(oldRecipeId, this);
    }

    private RecipeIdentityEntity cloneEntity() {
        isCloned = false;
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIdentityEntity(
                recipeId,
                identityEntity.getTitle(),
                identityEntity.getDescription(),
                currentTime,
                currentTime
        );
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity identityEntity) {
        this.identityEntity = identityEntity;

        if (isCloned) {
            this.identityEntity = cloneEntity();
            updateObservables();
            save(updatedEntity());
        } else
            updateObservables();
    }

    @Override
    public void onDataNotAvailable() {
        identityEntity = createEntity();
        updateObservables();
    }

    private RecipeIdentityEntity createEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIdentityEntity(
                recipeId,
                "",
                "",
                currentTime,
                currentTime);
    }

    private void updateObservables() {
        observablesUpdating = true;
        title = identityEntity.getTitle();
        notifyPropertyChanged(BR.title);
        description = identityEntity.getDescription();
        notifyPropertyChanged(BR.description);
        observablesUpdating = false;
        submitModelStatus();
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (!this.title.equals(title))
            titleChanged(title);
    }

    private void titleChanged(String title) {
        titleErrorMessage.set(null);
        String validationResponse = validateShortText(title);

        titleValid = validationResponse.equals(VALIDATED);

        if (!titleValid)
            titleErrorMessage.set(validationResponse);
        else {
            this.title = title;
            notifyPropertyChanged(BR.title);
        }
        submitModelStatus();
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!this.description.equals(description))
            descriptionChanged(description);
    }

    private void descriptionChanged(String description) {
        descriptionErrorMessage.set(null);
        String validationResponse = validateLongText(description);

        descriptionValid = validationResponse.equals(VALIDATED);

        if (!descriptionValid)
            descriptionErrorMessage.set(validationResponse);
        else {
            this.description = description;
            notifyPropertyChanged(BR.description);
        }

        submitModelStatus();
    }

    private String validateShortText(String textToValidate) {
        return textValidationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return textValidationHandler.validateLongText(resources, textToValidate);
    }

    private void submitModelStatus() {
        if (!observablesUpdating) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.IDENTITY_MODEL,
                    isChanged(),
                    isValid()
            ));
            if (isValid() && isChanged()) {
                save(updatedEntity());
            }
        }
    }

    private boolean isValid() {
        return titleValid && descriptionValid;
    }

    private boolean isChanged() {
        if (identityEntity != null) {
            RecipeIdentityEntity updatedEntity = new RecipeIdentityEntity(
                    identityEntity.getId(),
                    title,
                    description,
                    identityEntity.getCreateDate(),
                    identityEntity.getLastUpdate()
            );
            return !identityEntity.equals(updatedEntity);
        } else
            return false;
    }

    private RecipeIdentityEntity updatedEntity() {
        return new RecipeIdentityEntity(
                identityEntity.getId(),
                title,
                description,
                identityEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void save(RecipeIdentityEntity entity) {
        identityEntity = entity;
        repository.save(entity);
    }
}