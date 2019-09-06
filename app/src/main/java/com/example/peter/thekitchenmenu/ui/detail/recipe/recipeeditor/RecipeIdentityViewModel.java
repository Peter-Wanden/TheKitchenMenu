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

    private RecipeIdentityEntity oldIdentityEntity;
    private String recipeId;
    private int prepHours;
    private int prepMinutes;
    private int cookHours;
    private int cookMinutes;

    private boolean
            modelUpdating,
            isNewIdentityEntity,
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
        isNewIdentityEntity = false;
        oldIdentityEntity = identityEntity;
        updateObservables();
    }

    private void updateObservables() {
        modelUpdating = true;

        titleObservable.set(oldIdentityEntity.getTitle());
        descriptionObservable.set(oldIdentityEntity.getDescription());
        prepHoursObservable.set(String.valueOf(getHours(oldIdentityEntity.getPrepTime())));
        prepMinutesObservable.set(String.valueOf(getMinutes(oldIdentityEntity.getPrepTime())));
        cookHoursObservable.set(String.valueOf(getHours(oldIdentityEntity.getCookTime())));
        cookMinutesObservable.set(String.valueOf(getMinutes(oldIdentityEntity.getCookTime())));

        modelUpdating = false;
        reportRecipeModelStatus();
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    @Override
    public void onDataNotAvailable() {
        isNewIdentityEntity = true;
        oldIdentityEntity = createNewIdentityEntity();
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
        if (!descriptionValid) descriptionErrorMessage.set(validationResponse);

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

        reportRecipeModelStatus();
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

            if (isValid() && isChanged())
                saveIdentityEntity();
        }
    }

    private boolean isValid() {
        return titleValid && descriptionValid && prepTimeValid && cookTimeValid;
    }

    private boolean isChanged() {
        if (oldIdentityEntity != null) {
            RecipeIdentityEntity updatedIdentityEntity = (new RecipeIdentityEntity(
                    oldIdentityEntity.getId(),
                    oldIdentityEntity.getRecipeId(),
                    titleObservable.get(),
                    descriptionObservable.get(),
                    calculateTotalInMinutes(prepHours, prepMinutes),
                    calculateTotalInMinutes(cookHours, cookMinutes),
                    oldIdentityEntity.getCreateDate(),
                    oldIdentityEntity.getLastUpdate()
            ));
            return !oldIdentityEntity.equals(updatedIdentityEntity);
        } else
            return false;
    }

    private void saveIdentityEntity() {
        RecipeIdentityEntity identityEntity;
        if (isNewIdentityEntity) {
            identityEntity = createNewIdentityEntity();
        } else {
            identityEntity = updateExistingIdentityEntity();
        }
        recipeIdentityDataSource.save(identityEntity);
    }

    private RecipeIdentityEntity updateExistingIdentityEntity() {
        return new RecipeIdentityEntity(
                oldIdentityEntity.getId(),
                recipeId,
                oldIdentityEntity.getTitle(),
                descriptionObservable.get(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes),
                oldIdentityEntity.getCreateDate(),
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
