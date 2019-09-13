package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;
import android.util.Log;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
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
    private ParseIntegerFromObservableHandler intFromObservable;
    private DataSource<RecipeIdentityEntity> recipeIdentityDataSource;
    private TimeProvider timeProvider;
    private RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> titleObservable = new ObservableField<>("");
    public final ObservableField<String> descriptionObservable = new ObservableField<>("");
    public final ObservableField<String> prepHoursObservable = new ObservableField<>();
    public final ObservableField<String> prepMinutesObservable = new ObservableField<>();
    public final ObservableField<String> cookHoursObservable = new ObservableField<>();
    public final ObservableField<String> cookMinutesObservable = new ObservableField<>();

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private RecipeIdentityEntity identityEntity;
    private String recipeId;
    private int prepHours;
    private int prepMinutes;
    private int cookHours;
    private int cookMinutes;

    private boolean
            modelUpdating,
            isCloned,
            titleValid,
            descriptionValid = true,
            prepTimeValid = true,
            cookTimeValid = true;

    public RecipeIdentityViewModel(DataSource<RecipeIdentityEntity> recipeIdentityDataSource,
                                   TimeProvider timeProvider,
                                   Resources resources,
                                   TextValidationHandler validationHandler,
                                   ParseIntegerFromObservableHandler intFromObservable) {
        this.recipeIdentityDataSource = recipeIdentityDataSource;
        this.timeProvider = timeProvider;
        this.resources = resources;
        this.validationHandler = validationHandler;
        this.intFromObservable = intFromObservable;

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
        prepHoursObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!prepHoursObservable.get().isEmpty())
                    getPrepHours();
            }
        });
        prepMinutesObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!prepMinutesObservable.get().isEmpty())
                    getPrepMinutes();
            }
        });
        cookHoursObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!cookHoursObservable.get().isEmpty())
                    getCookHours();
            }
        });
        cookMinutesObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!cookMinutesObservable.get().isEmpty())
                    getCookMinutes();
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
            Log.d(TAG, "start: called");
            recipeIdentityDataSource.getById(recipeId, this);
        } else {
            throw new RuntimeException("Recipe id cannot be null");
        }
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        Log.d(TAG, "startByCloningModel: start by clone called");
        isCloned = true;
        this.recipeId = newRecipeId;
        recipeIdentityDataSource.getById(oldRecipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity identityEntity) {
        this.identityEntity = identityEntity;

        if (isCloned) {
            this.identityEntity = cloneIdentityEntity();
            updateObservables();
            Log.d(TAG, "onEntityLoaded: saving clone");
            save(updatedIdentityEntity());
        } else
            updateObservables();
    }

    @Override
    public void onDataNotAvailable() {
        identityEntity = createNewIdentityEntity();
        Log.d(TAG, "onDataNotAvailable: saving new");
        save(identityEntity);
        updateObservables();
    }

    private void updateObservables() {
        modelUpdating = true;

        titleObservable.set(identityEntity.getTitle());
        descriptionObservable.set(identityEntity.getDescription());
        prepHoursObservable.set(String.valueOf(getHours(identityEntity.getPrepTime())));
        prepMinutesObservable.set(String.valueOf(getMinutes(identityEntity.getPrepTime())));
        cookHoursObservable.set(String.valueOf(getHours(identityEntity.getCookTime())));
        cookMinutesObservable.set(String.valueOf(getMinutes(identityEntity.getCookTime())));

        modelUpdating = false;
        reportRecipeModelStatus();
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
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


    private void getPrepHours() {
        prepHours = parseIntegerFromObservableField(prepHoursObservable, prepHours);
        if (prepHours > 0)
            validatePrepTime();
    }

    private void getPrepMinutes() {
        prepMinutes = parseIntegerFromObservableField(prepMinutesObservable, prepMinutes);
        if (prepMinutes > 0)
            validatePrepTime();
    }

    private void validatePrepTime() {
        prepTimeErrorMessage.set(null);
        int maxAllowedPrepTime = resources.getInteger(R.integer.recipe_max_prep_time_in_minutes);
        int totalPrepTime = calculateTotalInMinutes(prepHours, prepMinutes);
        String errorMessage = resources.getString(R.string.input_error_recipe_prep_time_too_long);

        prepTimeValid = totalPrepTime <= maxAllowedPrepTime;

        if (!prepTimeValid)
            prepTimeErrorMessage.set(errorMessage);

        reportRecipeModelStatus();
    }

    private void getCookHours() {
        cookHours = parseIntegerFromObservableField(cookHoursObservable, cookHours);
        if (cookHours > 0)
            validateCookTime();
    }

    private void getCookMinutes() {
        cookMinutes = parseIntegerFromObservableField(cookMinutesObservable, cookMinutes);
        if (cookMinutes > 0)
            validateCookTime();
    }

    private void validateCookTime() {
        cookTimeErrorMessage.set(null);
        int maxAllowedCookTime = resources.getInteger(R.integer.recipe_max_cook_time_in_minutes);
        int totalCookTime = calculateTotalInMinutes(cookHours, cookMinutes);
        // todo - make the time in this string a calculation
        String errorMessage = resources.getString(R.string.input_error_recipe_cook_time_too_long);

        cookTimeValid = totalCookTime <= maxAllowedCookTime;
        if (!cookTimeValid)
            cookTimeErrorMessage.set(errorMessage);

        reportRecipeModelStatus();
    }

    private int parseIntegerFromObservableField(ObservableField<String> observable, int oldValue) {
        return intFromObservable.parseInt(resources, observable, oldValue);
    }

    private int calculateTotalInMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void reportRecipeModelStatus() {
        if (!modelUpdating) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.IDENTITY_MODEL,
                    isChanged(),
                    isValid()
            ));

            if (isValid() && isChanged()) {
                Log.d(TAG, "reportRecipeModelStatus: saving updated");
                save(updatedIdentityEntity());
            }
        }
    }

    private boolean isValid() {
        return titleValid && descriptionValid && prepTimeValid && cookTimeValid;
    }

    private boolean isChanged() {
        if (identityEntity != null) {
            RecipeIdentityEntity updatedIdentityEntity = (new RecipeIdentityEntity(
                    identityEntity.getId(),
                    titleObservable.get(),
                    descriptionObservable.get(),
                    calculateTotalInMinutes(prepHours, prepMinutes),
                    calculateTotalInMinutes(cookHours, cookMinutes),
                    identityEntity.getCreateDate(),
                    identityEntity.getLastUpdate()
            ));
            return !identityEntity.equals(updatedIdentityEntity);
        } else
            return false;
    }

    private void save(RecipeIdentityEntity identityEntity) {
        recipeIdentityDataSource.save(identityEntity);
    }

    private RecipeIdentityEntity updatedIdentityEntity() {
        return new RecipeIdentityEntity(
                identityEntity.getId(),
                titleObservable.get(),
                descriptionObservable.get(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes),
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
                0,
                0,
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
                identityEntity.getPrepTime(),
                identityEntity.getCookTime(),
                currentTime,
                currentTime
        );
    }
}
