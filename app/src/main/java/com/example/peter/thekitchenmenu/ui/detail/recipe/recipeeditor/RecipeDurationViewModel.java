package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

public class RecipeDurationViewModel
        extends
        ObservableViewModel
        implements
        DataSource.GetEntityCallback<RecipeDurationEntity>,
        RecipeModelComposite.RecipeModelActions {

    private static final int MEASUREMENT_ERROR = -1;
    private RepositoryRecipeDuration repository;
    private NumberFormatter numberFormatter;
    private Resources resources;
    private ParseIntegerFromObservableHandler intFromObservable;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;
    private TimeProvider timeProvider;

    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private String recipeId;
    private RecipeDurationEntity durationEntity;

    private String prepHoursInView = "";
    private int prepHours;
    private String prepMinutesInView = "";
    private int prepMinutes;

    private String cookHoursInView = "";
    private int cookHours;
    private String cookMinutesInView = "";
    private int cookMinutes;

    private boolean prepTimeValid = true;
    private boolean cookTimeValid = true;
    private boolean isCloned;
    private boolean updatingUi;
    private boolean dataLoading;

    public RecipeDurationViewModel(RepositoryRecipeDuration repository,
                                   NumberFormatter numberFormatter,
                                   Resources resources,
                                   TimeProvider timeProvider,
                                   ParseIntegerFromObservableHandler intFromObservable)  {
        this.repository = repository;
        this.numberFormatter = numberFormatter;
        this.resources = resources;
        this.timeProvider = timeProvider;
        this.intFromObservable = intFromObservable;
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
    public void onEntityLoaded(RecipeDurationEntity durationEntity) {
        if (isCloned)
            this.durationEntity = cloneEntity(durationEntity);
        else
            this.durationEntity = durationEntity;
        updateObservables();
    }

    private RecipeDurationEntity cloneEntity(RecipeDurationEntity toClone) {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeDurationEntity(
                recipeId,
                toClone.getPrepTime(),
                toClone.getCookTime(),
                currentTime,
                currentTime
        );
    }

    @Override
    public void onDataNotAvailable() {
        durationEntity = createNewEntity();
        save(durationEntity);
        dataLoading = false;
        updateObservables();
    }

    private RecipeDurationEntity createNewEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeDurationEntity(
                recipeId,
                0,
                0,
                currentTime,
                currentTime
        );
    }

    private void updateObservables() {
        updatingUi = true;

        prepHours = getHours(durationEntity.getPrepTime());
        prepHoursInView = String.valueOf(prepHours);
        notifyPropertyChanged(BR.prepHoursInView);

        prepMinutes = getMinutes(durationEntity.getPrepTime());
        prepMinutesInView = String.valueOf(prepMinutes);
        notifyPropertyChanged(BR.prepMinutesInView);

        cookHours = getHours(durationEntity.getCookTime());
        cookHoursInView = String.valueOf(cookHours);
        notifyPropertyChanged(BR.cookHoursInView);

        cookMinutes = getMinutes(durationEntity.getCookTime());
        cookMinutesInView = String.valueOf(cookMinutes);
        notifyPropertyChanged(BR.cookMinutesInView);

        updatingUi = false;

        if (dataLoading) {
            validatePrepTime();
            dataLoading = false;
        } else
            submitModelStatus();

        if (isCloned && isValid()) {
            save(durationEntity);
            isCloned = false;
        }
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

    @Bindable
    public String getPrepHoursInView() {
        return prepHoursInView;
    }

    public void setPrepHoursInView(String prepHoursInView) {
        if (isPrepHoursInViewChanged(prepHoursInView)) {
            if (!prepHoursInView.isEmpty()) {
                int prepHoursParsed = parseIntegerFromString(prepHoursInView);

                if (prepHoursParsed == MEASUREMENT_ERROR)
                    prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    this.prepHoursInView = prepHoursInView;
                    this.prepHours = prepHoursParsed;
                    validatePrepTime();
                }
            }
        }
    }

    private boolean isPrepHoursInViewChanged(String prepHoursInView) {
        return !this.prepHoursInView.equals(prepHoursInView);
    }

    @Bindable
    public String getPrepMinutesInView() {
        return prepMinutesInView;
    }

    public void setPrepMinutesInView(String prepMinutesInView) {
        if (isPrepMinutesInViewChanged(prepMinutesInView)) {
            if (!prepMinutesInView.isEmpty()) {
                int prepMinutesParsed = parseIntegerFromString(prepMinutesInView);

                if (prepMinutesParsed == MEASUREMENT_ERROR)
                    prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    this.prepMinutesInView = prepMinutesInView;
                    this.prepMinutes = prepMinutesParsed;
                    validatePrepTime();
                }
            }
        }
    }

    private boolean isPrepMinutesInViewChanged(String prepMinutesInView) {
        return !this.prepMinutesInView.equals(prepMinutesInView);
    }

    @Bindable
    public String getCookHoursInView() {
        return cookHoursInView;
    }

    public void setCookHoursInView(String cookHoursInView) {
        if (isCookHoursInViewChanged(cookHoursInView)) {
            if (!cookHoursInView.isEmpty()) {
                int cookHoursParsed = parseIntegerFromString(cookHoursInView);

                if (cookHoursParsed == MEASUREMENT_ERROR)
                    cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    this.cookHoursInView = cookHoursInView;
                    this.cookHours = cookHoursParsed;
                    validateCookTime();
                }
            }
        }
    }

    private boolean isCookHoursInViewChanged(String cookHoursInView) {
        return !this.cookHoursInView.equals(cookHoursInView);
    }

    @Bindable
    public String getCookMinutesInView() {
        return cookMinutesInView;
    }

    public void setCookMinutesInView(String cookMinutesInView) {
        if (isCookMinutesInViewChanged(cookMinutesInView)) {
            if (!cookMinutesInView.isEmpty()) {
                int cookMinutesParsed = parseIntegerFromString(cookMinutesInView);

                if (cookMinutesParsed == MEASUREMENT_ERROR)
                    cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    this.cookMinutesInView = cookMinutesInView;
                    this.cookMinutes = cookMinutesParsed;
                    validateCookTime();
                }
            }
        }
    }

    private boolean isCookMinutesInViewChanged(String cookMinutesInView) {
        return !this.cookMinutesInView.equals(cookMinutesInView);
    }

    private int parseIntegerFromString(String integerToParse) {
        try {
            return Integer.parseInt(integerToParse);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
        }
    }

    private String numberFormatExceptionErrorMessage() {
        return resources.getString(R.string.number_format_exception);
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

    // todo, as these two methods are implemented in all models that should be part of an interface
    // todo, possible move {@link RecipeModelComposite} to recipeValidator?
    // todo - Or, add them as listeners / requester's

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }
    private void submitModelStatus() {
        if (!updatingUi) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.DURATION_MODEL,
                    isChanged(),
                    isValid()
            ));
            if (isChanged() && isValid()) {
                RecipeDurationEntity entity = updatedDurationEntity();
                save(entity);
            }
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

    private RecipeDurationEntity updatedDurationEntity() {
        return new RecipeDurationEntity(
                durationEntity.getId(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes),
                durationEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void save(RecipeDurationEntity durationEntity) {
        repository.save(durationEntity);
        this.durationEntity = durationEntity;
    }
}
