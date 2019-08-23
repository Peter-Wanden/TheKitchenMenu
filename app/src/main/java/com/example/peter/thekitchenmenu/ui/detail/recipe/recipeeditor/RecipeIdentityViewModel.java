package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservable;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.*;

public class RecipeIdentityViewModel extends ViewModel {

//    private static final String TAG = "tkm-RecipeIdentityVM";

    private Resources resources;
    private TextValidationHandler validationHandler;
    private ParseIntegerFromObservable intFromObservable;

    private final MediatorLiveData<RecipeIdentityModelMetaData> recipeIdentityModelMetaData =
            new MediatorLiveData<>();

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

    private RecipeIdentityModel uneditedIdentityModel = new RecipeIdentityModel(
            "", "", 0, 0
    );

    private int prepHours;
    private int prepMinutes;
    private int cookHours;
    private int cookMinutes;

    private boolean
            updatingModel,
            titleValidated,
            descriptionValidated,
            prepTimeValidated,
            cookTimeValidated;

    public RecipeIdentityViewModel(Resources resources,
                                   TextValidationHandler validationHandler,
                                   ParseIntegerFromObservable intFromObservable) {
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

    MediatorLiveData<RecipeIdentityModelMetaData> getRecipeIdentityModelMetaData() {
        return recipeIdentityModelMetaData;
    }

    void onStart(RecipeEntity recipeEntity) {
        if (recipeEntity != null) {
            updatingModel = true;
            setUneditedModel(recipeEntity);

            titleObservable.set(recipeEntity.getTitle());
            descriptionObservable.set(recipeEntity.getDescription());
            prepHoursObservable.set(String.valueOf(getHours(recipeEntity.getPreparationTime())));
            prepMinutesObservable.set(String.valueOf(getMinutes(recipeEntity.getPreparationTime())));
            cookHoursObservable.set(String.valueOf(getHours(recipeEntity.getCookingTime())));
            cookMinutesObservable.set(String.valueOf(getMinutes(recipeEntity.getCookingTime())));
            updatingModel = false;
        } else {
            throw new RuntimeException("Recipe cannot be null");
        }
    }

    private void setUneditedModel(RecipeEntity recipeEntity) {
        uneditedIdentityModel = new RecipeIdentityModel(
                recipeEntity.getTitle(),
                recipeEntity.getDescription(),
                recipeEntity.getPreparationTime(),
                recipeEntity.getCookingTime()
        );
    }

    private void titleChanged() {
        titleErrorMessage.set(null);
        String validationResponse = validateShortText(titleObservable.get());

        titleValidated = validationResponse.equals(VALIDATED);
        if (!titleValidated) titleErrorMessage.set(validationResponse);

        checkValidationStatus();
    }

    private void descriptionChanged() {
        descriptionErrorMessage.set(null);
        String validationResponse = validateLongText(descriptionObservable.get());

        descriptionValidated = validationResponse.equals(VALIDATED);
        if (!descriptionValidated) descriptionErrorMessage.set(validationResponse);

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

        prepTimeValidated = totalPrepTime <= maxAllowedPrepTime;
        if (!prepTimeValidated) prepTimeErrorMessage.set(errorMessage);

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

        cookTimeValidated = totalCookTime <= maxAllowedCookTime;
        if (!cookTimeValidated) cookTimeErrorMessage.set(errorMessage);

        checkValidationStatus();
    }

    private int parseIntegerFromObservableField(ObservableField<String> observable, int oldValue) {
        return intFromObservable.parseInt(resources, observable, oldValue);
    }

    private int calculateTotalInMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void checkValidationStatus() {
        updateRecipeWithIdentityModel(
                titleValidated &&
                descriptionValidated &&
                prepTimeValidated &&
                cookTimeValidated);
    }

    private void updateRecipeWithIdentityModel(boolean allFieldsValidated) {
        if (!updatingModel)
            recipeIdentityModelMetaData.setValue(new RecipeIdentityModelMetaData(
                    getLatestModel(),
                    isModelChanged(),
                    allFieldsValidated
            ));
    }

    private RecipeIdentityModel getLatestModel() {
        return new RecipeIdentityModel(
                titleObservable.get(),
                descriptionObservable.get(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes)
        );
    }

    private boolean isModelChanged() {
        return !uneditedIdentityModel.equals(getLatestModel());
    }
}
