package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

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

    private boolean updatingUi;
    private boolean isCloned;
    private boolean titleValid;
    private boolean descriptionValid = true;
    private boolean dataLoading;

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
        this.recipeId = recipeId;
        dataLoading = true;
        repository.getById(recipeId, this);
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        isCloned = true;
        this.recipeId = newRecipeId;
        dataLoading = true;
        repository.getById(oldRecipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity identityEntity) {
        if (isCloned)
            this.identityEntity = cloneEntity(identityEntity);
        else
            this.identityEntity = identityEntity;
        updateObservables();
    }

    private RecipeIdentityEntity cloneEntity(RecipeIdentityEntity toClone) {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIdentityEntity(
                recipeId,
                toClone.getTitle(),
                toClone.getDescription(),
                currentTime,
                currentTime
        );
    }

    @Override
    public void onDataNotAvailable() {
        identityEntity = createNewEntity();
        dataLoading = false;
        updateObservables();
    }

    private RecipeIdentityEntity createNewEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIdentityEntity(
                recipeId,
                title,
                description,
                currentTime,
                currentTime);
    }

    private void updateObservables() {
        updatingUi = true;

        title = identityEntity.getTitle();
        notifyPropertyChanged(BR.title);

        description = identityEntity.getDescription();
        notifyPropertyChanged(BR.description);

        updatingUi = false;

        if (dataLoading) {
            validateTitle(identityEntity.getTitle());
            validateDescription(identityEntity.getDescription());
            dataLoading = false;
        } else
            submitModelStatus();

        if (isCloned && isValid()) {
            save(identityEntity);
            isCloned = false;
        }
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (isTitleChanged(title)) {
            this.title = title;
            validateTitle(title);
        }
    }

    private boolean isTitleChanged(String title) {
        return !updatingUi && !this.title.equals(title);
    }

    private void validateTitle(String title) {
        titleErrorMessage.set(null);
        String validationResponse = validateShortText(title);
        titleValid = validationResponse.equals(VALIDATED);

        if (!titleValid)
            titleErrorMessage.set(validationResponse);

        saveValidChanges();
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (isDescriptionChanged(description)) {
            this.description = description;
            validateDescription(description);
        }
    }

    private boolean isDescriptionChanged(String description) {
        return !updatingUi && !this.description.equals(description);
    }

    private void validateDescription(String description) {
        descriptionErrorMessage.set(null);
        String validationResponse = validateLongText(description);
        descriptionValid = validationResponse.equals(VALIDATED);

        if (!descriptionValid)
            descriptionErrorMessage.set(validationResponse);

        saveValidChanges();
    }


    private String validateShortText(String textToValidate) {
        return textValidationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return textValidationHandler.validateLongText(resources, textToValidate);
    }

    private void saveValidChanges() {
        RecipeIdentityEntity updatedEntity = updatedEntity();

        System.out.println("isValid=" + isValid() + " isChanged=" + isChanged());

        if (isValid() && isChanged())
            save(updatedEntity);

        submitModelStatus();
        identityEntity = updatedEntity;
    }

    private void submitModelStatus() {
        if (!updatingUi) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.IDENTITY_MODEL,
                    isChanged(),
                    isValid()
            ));
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
        repository.save(entity);
    }
}