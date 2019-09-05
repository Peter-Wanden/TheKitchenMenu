package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeIdentity;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.*;

public class RecipeIdentityViewModel
        extends ViewModel
        implements DataSource.GetEntityCallback<RecipeIdentityEntity> {

    private Resources resources;
    private TextValidationHandler validationHandler;
    private ParseIntegerFromObservableHandler intFromObservable;
    private DataSourceRecipeIdentity recipeIdentityDataSource;
    private TimeProvider timeProvider;
    private UniqueIdProvider idProvider;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> titleObservable = new ObservableField<>("");
    public final ObservableField<String> descriptionObservable = new ObservableField<>("");
    public final ObservableField<String> prepHoursObservable = new ObservableField<>("");
    public final ObservableField<String> prepMinutesObservable = new ObservableField<>("");
    public final ObservableField<String> cookHoursObservable = new ObservableField<>("");
    public final ObservableField<String> cookMinutesObservable = new ObservableField<>("");

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private RecipeIdentityEntity existingIdentityEntity;
    private String recipeId;
    private int prepHours;
    private int prepMinutes;
    private int cookHours;
    private int cookMinutes;

    private boolean
            isNewIdentityModel,
            titleValid,
            descriptionValid = true,
            prepTimeValid = true,
            cookTimeValid = true;

    public RecipeIdentityViewModel(DataSourceRecipeIdentity recipeIdentityDataSource,
                                   TimeProvider timeProvider,
                                   UniqueIdProvider idProvider,
                                   Resources resources,
                                   TextValidationHandler validationHandler,
                                   ParseIntegerFromObservableHandler intFromObservable) {
        this.recipeIdentityDataSource = recipeIdentityDataSource;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.resources = resources;
        this.validationHandler = validationHandler;
        this.intFromObservable = intFromObservable;

        titleObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                titleChanged();
            }
        });
        descriptionObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                descriptionChanged();
            }
        });
        prepHoursObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getPrepHours();
            }
        });
        prepMinutesObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getPrepMinutes();
            }
        });
        cookHoursObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getCookHours();
            }
        });
        cookMinutesObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getCookMinutes();
            }
        });
    }

    void onStart(String recipeId) {
        if (recipeId != null) {
            this.recipeId = recipeId;
            recipeIdentityDataSource.getByRecipeId(recipeId, this);
        } else {
            throw new RuntimeException("Recipe id cannot be null");
        }
    }

    void setModelSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity identityEntity) {
        isNewIdentityModel = false;
        existingIdentityEntity = identityEntity;
        updateObservables();
    }

    private void updateObservables() {
        titleObservable.set(existingIdentityEntity.getTitle());
        descriptionObservable.set(existingIdentityEntity.getDescription());
        prepHoursObservable.set(String.valueOf(getHours(existingIdentityEntity.getPrepTime())));
        prepMinutesObservable.set(String.valueOf(getMinutes(existingIdentityEntity.getPrepTime())));
        cookHoursObservable.set(String.valueOf(getHours(existingIdentityEntity.getCookTime())));
        cookMinutesObservable.set(String.valueOf(getMinutes(existingIdentityEntity.getCookTime())));
    }

    @Override
    public void onDataNotAvailable() {
        isNewIdentityModel = true;
        existingIdentityEntity = createNewIdentityEntity();
    }

    private void titleChanged() {
        titleErrorMessage.set(null);
        String validationResponse = validateShortText(titleObservable.get());

        titleValid = validationResponse.equals(VALIDATED);
        if (!titleValid)
            titleErrorMessage.set(validationResponse);

        checkValidationStatus();
    }

    private void descriptionChanged() {
        descriptionErrorMessage.set(null);
        String validationResponse = validateLongText(descriptionObservable.get());

        descriptionValid = validationResponse.equals(VALIDATED);
        if (!descriptionValid) descriptionErrorMessage.set(validationResponse);

        checkValidationStatus();
    }

    private String validateShortText(String textToValidate) {
        return validationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return validationHandler.validateLongText(resources, textToValidate);
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }


    private void getPrepHours() {
        prepHours = parseIntegerFromObservableField(prepHoursObservable, prepHours);
        validatePrepTime();
    }

    private void getPrepMinutes() {
        prepMinutes = parseIntegerFromObservableField(prepMinutesObservable, prepMinutes);
        validatePrepTime();
    }

    private void validatePrepTime() {
        prepTimeErrorMessage.set(null);
        int maxAllowedPrepTime = resources.getInteger(R.integer.recipe_max_prep_time_in_minutes);
        int totalPrepTime = calculateTotalInMinutes(prepHours, prepMinutes);
        String errorMessage = resources.getString(R.string.input_error_recipe_prep_time_too_long);

        prepTimeValid = totalPrepTime <= maxAllowedPrepTime;
        if (!prepTimeValid) prepTimeErrorMessage.set(errorMessage);

        checkValidationStatus();
    }

    private void getCookHours() {
        cookHours = parseIntegerFromObservableField(cookHoursObservable, cookHours);
        validateCookTime();
    }

    private void getCookMinutes() {
        cookMinutes = parseIntegerFromObservableField(cookMinutesObservable, cookMinutes);
        validateCookTime();
    }

    private void validateCookTime() {
        cookTimeErrorMessage.set(null);
        int maxAllowedCookTime = resources.getInteger(R.integer.recipe_max_cook_time_in_minutes);
        int totalCookTime = calculateTotalInMinutes(cookHours, cookMinutes);
        // todo - make the time in this string a calculation
        String errorMessage = resources.getString(R.string.input_error_recipe_cook_time_too_long);

        cookTimeValid = totalCookTime <= maxAllowedCookTime;
        if (!cookTimeValid) cookTimeErrorMessage.set(errorMessage);

        checkValidationStatus();
    }

    private int parseIntegerFromObservableField(ObservableField<String> observable, int oldValue) {
        return intFromObservable.parseInt(resources, observable, oldValue);
    }

    private int calculateTotalInMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void checkValidationStatus() {
        saveIdentityModel(
                titleValid &&
                        descriptionValid &&
                        prepTimeValid &&
                        cookTimeValid);
    }

    private void saveIdentityModel(boolean allFieldsValidated) {
        if (allFieldsValidated) {
            RecipeIdentityEntity identityEntity;
            if (isNewIdentityModel) {
                identityEntity = createNewIdentityEntity();
            } else {
                identityEntity = updateExistingIdentityEntity();
            }
            recipeIdentityDataSource.save(identityEntity);
        }
    }

    private RecipeIdentityEntity updateExistingIdentityEntity() {
        return new RecipeIdentityEntity(
                existingIdentityEntity.getId(),
                recipeId,
                existingIdentityEntity.getTitle(),
                descriptionObservable.get(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes),
                existingIdentityEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private RecipeIdentityEntity createNewIdentityEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();

        return new RecipeIdentityEntity(
                idProvider.getUId(),
                recipeId,
                titleObservable.get(),
                descriptionObservable.get(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes),
                currentTime,
                currentTime);
    }
}
