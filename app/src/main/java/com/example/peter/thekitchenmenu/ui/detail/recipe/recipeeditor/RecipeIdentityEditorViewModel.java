package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.ui.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidation.*;
import static com.example.peter.thekitchenmenu.ui.utils.TextValidationHandler.*;

public class RecipeIdentityEditorViewModel
        extends
        ObservableViewModel
        implements
        DataSource.GetEntityCallback<RecipeIdentityEntity>,
        RecipeModelComposite.RecipeModelActions {

    private Resources resources;
    private TimeProvider timeProvider;
    private RepositoryRecipeIdentity repository;
    private TextValidationHandler textValidationHandler;
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

    public RecipeIdentityEditorViewModel(RepositoryRecipeIdentity repository,
                                         TimeProvider timeProvider,
                                         Resources resources,
                                         TextValidationHandler textValidationHandler) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.resources = resources;
        this.textValidationHandler = textValidationHandler;
    }

    @Override
    public void start(String recipeId) {
        if (this.recipeId == null || !this.recipeId.equals(recipeId)) {
            this.recipeId = recipeId;
            getData(recipeId);


        }
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        if (recipeId == null || !this.recipeId.equals(newRecipeId)) {
            isCloned = true;
            recipeId = newRecipeId;
            getData(oldRecipeId);
        }
    }

    private void getData(String recipeId) {
        dataLoading = true;
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity identityEntity) {
        if (isCloned) {
            this.identityEntity = cloneEntity(identityEntity);
        } else {
            this.identityEntity = identityEntity;
        }
        updateObservables();
    }

    private RecipeIdentityEntity cloneEntity(RecipeIdentityEntity toClone) {
        long currentTime = timeProvider.getCurrentTimeInMills();
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
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityEntity(
                recipeId,
                title,
                description,
                currentTime,
                currentTime);
    }

    private void updateObservables() {
        updatingUi = true;
        updateTitle(identityEntity.getTitle());
        updateDescription(identityEntity.getDescription());
        updatingUi = false;

        if (dataLoading) {
            validateTitle();
            validateDescription();
            dataLoading = false;
        } else {
            submitModelStatus(isChanged(), isValid());
            return;
        }

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
            validateTitle();
        }
    }

    private boolean isTitleChanged(String title) {
        return !updatingUi && !this.title.equals(title);
    }

    private void validateTitle() {
        titleErrorMessage.set(null);
        String validationResponse = validateShortText(title);

        titleValid = validationResponse.equals(VALIDATED);
        if (!titleValid)
            titleErrorMessage.set(validationResponse);

        updateTitle(title);
        saveValidChanges();
    }

    private void updateTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (isDescriptionChanged(description)) {
            this.description = description;
            validateDescription();
        }
    }

    private boolean isDescriptionChanged(String description) {
        return !updatingUi && !this.description.equals(description);
    }

    private void validateDescription() {
        descriptionErrorMessage.set(null);
        String validationResponse = validateLongText(description);

        descriptionValid = validationResponse.equals(VALIDATED);
        if (!descriptionValid)
            descriptionErrorMessage.set(validationResponse);

        updateDescription(description);
        saveValidChanges();
    }

    private void updateDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }


    private String validateShortText(String textToValidate) {
        return textValidationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return textValidationHandler.validateLongText(resources, textToValidate);
    }

    void setModelValidationSubmitter(RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    private void saveValidChanges() {
        RecipeIdentityEntity updatedEntity = updatedEntity();
        boolean isChanged = isChanged();
        boolean isValid = isValid();

        if (isChanged && isValid) {
            save(updatedEntity);
        }
        equaliseState(updatedEntity);
        submitModelStatus(isChanged, isValid);
    }

    private void equaliseState(RecipeIdentityEntity entity) {
        identityEntity = entity;
    }

    private void submitModelStatus(boolean isChanged, boolean isValid) {
        if (!updatingUi) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.IDENTITY_MODEL,
                    isChanged,
                    isValid
            ));
        }
    }

    private boolean isChanged() {
        if (identityEntity != null) {
            RecipeIdentityEntity latestData = new RecipeIdentityEntity(
                    identityEntity.getId(),
                    title,
                    description,
                    identityEntity.getCreateDate(),
                    identityEntity.getLastUpdate()
            );
            return !identityEntity.equals(latestData);
        } else
            return false;
    }

    private boolean isValid() {
        return titleValid && descriptionValid;
    }

    private RecipeIdentityEntity updatedEntity() {
        return new RecipeIdentityEntity(
                identityEntity.getId(),
                title,
                description,
                identityEntity.getCreateDate(),
                timeProvider.getCurrentTimeInMills()
        );
    }

    private void save(RecipeIdentityEntity entity) {
        repository.save(entity);
    }
}