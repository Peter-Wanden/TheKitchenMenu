package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidation.*;
import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.*;

public class RecipeIdentityViewModel
        extends
        ViewModel
        implements
        DataSource.GetEntityCallback<RecipeIdentityEntity>,
        RecipeModelComposite.RecipeModelActions {

    private Resources resources;
    private TextValidationHandler textValidationHandler;
    private RepositoryRecipeIdentity repository;
    private TimeProvider timeProvider;
    private RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> titleObservable = new ObservableField<>("");
    public final ObservableField<String> descriptionObservable = new ObservableField<>("");

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

        titleObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!titleObservable.get().isEmpty())
                    titleChanged();
            }
        });
        descriptionObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!descriptionObservable.get().isEmpty())
                    descriptionChanged();
            }
        });
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
        titleObservable.set(identityEntity.getTitle());
        descriptionObservable.set(identityEntity.getDescription());
        observablesUpdating = false;
        submitModelStatus();
    }

    private void titleChanged() {
        titleErrorMessage.set(null);
        String validationResponse = validateShortText(titleObservable.get());

        titleValid = validationResponse.equals(VALIDATED);

        if (!titleValid)
            titleErrorMessage.set(validationResponse);

        submitModelStatus();
    }

    private void descriptionChanged() {
        descriptionErrorMessage.set(null);
        String validationResponse = validateLongText(descriptionObservable.get());

        descriptionValid = validationResponse.equals(VALIDATED);
        if (!descriptionValid)
            descriptionErrorMessage.set(validationResponse);

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
                    titleObservable.get(),
                    descriptionObservable.get(),
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
                titleObservable.get(),
                descriptionObservable.get(),
                identityEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void save(RecipeIdentityEntity entity) {
        identityEntity = entity;
        repository.save(entity);
    }
}