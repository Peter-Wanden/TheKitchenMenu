package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;
import android.util.Log;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
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

    private static final String TAG = "tkm-RecipeIdentityVM";

    private Resources resources;
    private TextValidationHandler validationHandler;
    private DataSource<RecipeIdentityEntity> identityEntityDataSource;
    private TimeProvider timeProvider;
    private RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> titleObservable = new ObservableField<>("");
    public final ObservableField<String> descriptionObservable = new ObservableField<>("");

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();

    private RecipeIdentityEntity identityEntity;
    private String recipeId;

    private boolean
            modelUpdating,
            isCloned,
            titleValid,
            descriptionValid = true;

    public RecipeIdentityViewModel(DataSource<RecipeIdentityEntity> identityEntityDataSource,
                                   TimeProvider timeProvider,
                                   Resources resources,
                                   TextValidationHandler validationHandler) {
        this.identityEntityDataSource = identityEntityDataSource;
        this.timeProvider = timeProvider;
        this.resources = resources;
        this.validationHandler = validationHandler;

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
//            Log.d(TAG, "start: called");
            identityEntityDataSource.getById(recipeId, this);
        } else {
            throw new RuntimeException("Recipe id cannot be null");
        }
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
//        Log.d(TAG, "startByCloningModel: start by clone called");
        isCloned = true;
        this.recipeId = newRecipeId;
        identityEntityDataSource.getById(oldRecipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity identityEntity) {
        this.identityEntity = identityEntity;

        if (isCloned) {
            this.identityEntity = cloneIdentityEntity();
            updateObservables();
//            Log.d(TAG, "onEntityLoaded: saving clone");
            save(updatedIdentityEntity());
        } else
            updateObservables();
    }

    @Override
    public void onDataNotAvailable() {
        identityEntity = createNewIdentityEntity();
//        Log.d(TAG, "onDataNotAvailable: saving new");
        save(identityEntity);
        updateObservables();
    }

    private void updateObservables() {
        modelUpdating = true;

        titleObservable.set(identityEntity.getTitle());
        descriptionObservable.set(identityEntity.getDescription());

        modelUpdating = false;
        reportRecipeModelStatus();
    }

    private void titleChanged() {
        titleErrorMessage.set(null);
        String validationResponse = validateShortText(titleObservable.get());

        titleValid = validationResponse.equals(VALIDATED);

        if (!titleValid)
            titleErrorMessage.set(validationResponse);

        reportRecipeModelStatus();
    }

    private void descriptionChanged() {
        descriptionErrorMessage.set(null);
        String validationResponse = validateLongText(descriptionObservable.get());

        descriptionValid = validationResponse.equals(VALIDATED);
        if (!descriptionValid)
            descriptionErrorMessage.set(validationResponse);

        reportRecipeModelStatus();
    }

    private String validateShortText(String textToValidate) {
        return validationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return validationHandler.validateLongText(resources, textToValidate);
    }

    private void reportRecipeModelStatus() {
        if (!modelUpdating) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.IDENTITY_MODEL,
                    isChanged(),
                    isValid()
            ));

            if (isValid() && isChanged()) {
//                Log.d(TAG, "reportRecipeModelStatus: saving updated");
                save(updatedIdentityEntity());
            }
        }
    }

    private boolean isValid() {
        return titleValid && descriptionValid;
    }

    private boolean isChanged() {
        if (identityEntity != null) {
            RecipeIdentityEntity updatedIdentityEntity = (new RecipeIdentityEntity(
                    identityEntity.getId(),
                    titleObservable.get(),
                    descriptionObservable.get(),
                    identityEntity.getCreateDate(),
                    identityEntity.getLastUpdate()
            ));
            return !identityEntity.equals(updatedIdentityEntity);
        } else
            return false;
    }

    private void save(RecipeIdentityEntity identityEntity) {
        identityEntityDataSource.save(identityEntity);
    }

    private RecipeIdentityEntity updatedIdentityEntity() {
        return new RecipeIdentityEntity(
                identityEntity.getId(),
                titleObservable.get(),
                descriptionObservable.get(),
                identityEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private RecipeIdentityEntity createNewIdentityEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIdentityEntity(
                recipeId,
                "",
                "",
                currentTime,
                currentTime);
    }

    private RecipeIdentityEntity cloneIdentityEntity() {
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
}