package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

public class RecipeDurationViewModel
        extends
        ViewModel
        implements
        DataSource.GetEntityCallback<RecipeDurationEntity>,
        RecipeModelComposite.RecipeModelActions {

    private DataSource<RecipeDurationEntity> durationEntityDataSource;
    private Resources resources;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;
    private ParseIntegerFromObservableHandler intFromObservable;
    private TimeProvider timeProvider;

    public final ObservableField<String> prepHoursObservable = new ObservableField<>();
    public final ObservableField<String> prepMinutesObservable = new ObservableField<>();
    public final ObservableField<String> cookHoursObservable = new ObservableField<>();
    public final ObservableField<String> cookMinutesObservable = new ObservableField<>();

    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private String recipeId;
    private RecipeDurationEntity durationEntity;
    private int prepHours;
    private int prepMinutes;
    private int cookHours;
    private int cookMinutes;

    private boolean prepTimeValid = true;
    private boolean cookTimeValid = true;
    private boolean isCloned;
    private boolean observablesUpdating;

    public RecipeDurationViewModel(DataSource<RecipeDurationEntity> durationEntityDataSource,
                                   Resources resources,
                                   TimeProvider timeProvider,
                                   ParseIntegerFromObservableHandler intFromObservable)  {
        this.durationEntityDataSource = durationEntityDataSource;
        this.resources = resources;
        this.timeProvider = timeProvider;
        this.intFromObservable = intFromObservable;

        prepHoursObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!prepHoursObservable.get().isEmpty())
                    updatePrepHours();
            }
        });
        prepMinutesObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!prepMinutesObservable.get().isEmpty())
                    updatePrepMinutes();
            }
        });
        cookHoursObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!cookHoursObservable.get().isEmpty())
                    updateCookHours();
            }
        });
        cookMinutesObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!cookMinutesObservable.get().isEmpty())
                    updateCookMinutes();
            }
        });
    }

    @Override
    public void start(String recipeId) {
        this.recipeId = recipeId;
        durationEntityDataSource.getById(recipeId, this);
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        isCloned = true;
        this.recipeId = newRecipeId;
        durationEntityDataSource.getById(oldRecipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeDurationEntity durationEntity) {
        this.durationEntity = durationEntity;
        if (isCloned) {
            this.durationEntity = cloneDurationEntity();
            save(updatedDurationEntity());
        }
        updateObservables();
    }

    @Override
    public void onDataNotAvailable() {
        durationEntity = createNewDurationEntity();
        save(durationEntity);
        updateObservables();
    }

    private RecipeDurationEntity createNewDurationEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeDurationEntity(
                recipeId,
                0,
                0,
                currentTime,
                currentTime
        );
    }

    private RecipeDurationEntity cloneDurationEntity() {
        isCloned = false;
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeDurationEntity(
                recipeId,
                durationEntity.getPrepTime(),
                durationEntity.getCookTime(),
                currentTime,
                currentTime
        );
    }

    private void updateObservables() {
        observablesUpdating = true;
        prepHoursObservable.set(String.valueOf(getHours(durationEntity.getPrepTime())));
        prepMinutesObservable.set(String.valueOf(getMinutes(durationEntity.getPrepTime())));
        cookHoursObservable.set(String.valueOf(getHours(durationEntity.getCookTime())));
        cookMinutesObservable.set(String.valueOf(getMinutes(durationEntity.getCookTime())));
        observablesUpdating = false;
        submitModelStatus();
    }

    private RecipeDurationEntity updatedDurationEntity() {
        return new RecipeDurationEntity(
                durationEntity.getId(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes),
                durationEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    private int calculateTotalInMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void updatePrepHours() {
        prepHours = parseIntegerFromObservableField(prepHoursObservable, prepHours);
        if (prepHours > 0)
            validatePrepTime();
    }

    private void updatePrepMinutes() {
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

        submitModelStatus();
    }

    private void updateCookHours() {
        cookHours = parseIntegerFromObservableField(cookHoursObservable, cookHours);
        if (cookHours > 0)
            validateCookTime();
    }

    private void updateCookMinutes() {
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

        submitModelStatus();
    }

    private int parseIntegerFromObservableField(ObservableField<String> observable, int oldValue) {
        return intFromObservable.parseInt(resources, observable, oldValue);
    }

    // todo, as these two methods are implemented in all models that should be part of an interface
    // todo, possible move {@link RecipeModelComposite} to recipeValidator?
    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    private void submitModelStatus() {
        if (!observablesUpdating) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.DURATION_MODEL,
                    isChanged(),
                    isValid()
            ));
            if (isChanged() && isValid())
                save(updatedDurationEntity());
        }
    }

    private boolean isChanged() {
        if (durationEntity != null) {
            RecipeDurationEntity latestDurationModel = new RecipeDurationEntity(
                    durationEntity.getId(),
                    calculateTotalInMinutes(prepHours, prepMinutes),
                    calculateTotalInMinutes(cookHours, cookMinutes),
                    durationEntity.getCreateDate(),
                    durationEntity.getLastUpdate()
            );

            return !durationEntity.equals(latestDurationModel);
        } else
            return false;
    }

    private boolean isValid() {
        return prepTimeValid && cookTimeValid;
    }

    private void save(RecipeDurationEntity durationEntity) {
        durationEntityDataSource.save(durationEntity);
        this.durationEntity = durationEntity;
    }
}
