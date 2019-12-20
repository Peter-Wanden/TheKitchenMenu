package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

public class RecipeDurationEditorViewModel
        extends
        ObservableViewModel
        implements
        DataSource.GetEntityCallback<RecipeDurationEntity>,
        RecipeModelComposite.RecipeModelActions {

    private static final int MEASUREMENT_ERROR = -1;

    private Resources resources;
    private TimeProvider timeProvider;
    private RepositoryRecipeDuration repository;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private String recipeId;
    private RecipeDurationEntity durationEntity;

    private int prepHours;
    private String prepHoursInView = "";
    private int prepMinutes;
    private String prepMinutesInView = "";
    private boolean prepTimeValid = true;

    private int cookHours;
    private String cookHoursInView = "";
    private int cookMinutes;
    private String cookMinutesInView = "";
    private boolean cookTimeValid = true;

    private boolean isCloned;
    private boolean dataLoading;
    private boolean updatingUi;

    public RecipeDurationEditorViewModel(RepositoryRecipeDuration repository,
                                         Resources resources,
                                         TimeProvider timeProvider)  {
        this.repository = repository;
        this.resources = resources;
        this.timeProvider = timeProvider;
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
        if (recipeId == null || !recipeId.equals(newRecipeId)) {
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
    public void onEntityLoaded(RecipeDurationEntity durationEntity) {
        if (isCloned) {
            this.durationEntity = cloneEntity(durationEntity);
        } else {
            this.durationEntity = durationEntity;
        }
        updateObservables();
    }

    private RecipeDurationEntity cloneEntity(RecipeDurationEntity toClone) {
        long currentTime = timeProvider.getCurrentTimeInMills();
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
        long currentTime = timeProvider.getCurrentTimeInMills();
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
        updatePrepTime(durationEntity.getPrepTime());
        updateCookTime(durationEntity.getCookTime());
        updatingUi = false;

        if (dataLoading) {
            validatePrepTime();
            validateCookTime();
            dataLoading = false;
        } else {
            submitModelStatus(isChanged(), isValid());
            return;
        }

        if (isCloned && isValid()) {
            save(durationEntity);
            isCloned = false;
        }
    }

    @Bindable
    public String getPrepHoursInView() {
        return prepHoursInView;
    }

    public void setPrepHoursInView(String prepHoursInView) {
        if (!updatingUi) {
            if (isPrepHoursInViewChanged(prepHoursInView)) {
                if (!prepHoursInView.isEmpty()) {
                    int prepHoursParsed = parseIntegerFromString(prepHoursInView);

                    if (prepHoursParsed == MEASUREMENT_ERROR)
                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        prepHours = prepHoursParsed;
                        validatePrepTime();
                    }
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
        if (!updatingUi) {
            if (isPrepMinutesInViewChanged(prepMinutesInView)) {
                if (!prepMinutesInView.isEmpty()) {
                    int prepMinutesParsed = parseIntegerFromString(prepMinutesInView);

                    if (prepMinutesParsed == MEASUREMENT_ERROR)
                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        prepMinutes = prepMinutesParsed;
                        validatePrepTime();
                    }
                }
            }
        }
    }

    private boolean isPrepMinutesInViewChanged(String prepMinutesInView) {
        return !this.prepMinutesInView.equals(prepMinutesInView);
    }

    private void validatePrepTime() {
        prepTimeErrorMessage.set(null);
        int maxAllowedPrepTime = resources.getInteger(R.integer.recipe_max_prep_time_in_minutes);
        int totalPrepTime = calculateTotalInMinutes(prepHours, prepMinutes);
        String errorMessage = resources.getString(R.string.input_error_recipe_prep_time_too_long);

        prepTimeValid = totalPrepTime <= maxAllowedPrepTime;
        if (!prepTimeValid) {
            prepTimeErrorMessage.set(errorMessage);
        }
        updatePrepTime(totalPrepTime);
        saveValidChanges();
    }

    private void updatePrepTime(int totalPrepTime) {
        prepHours = getHours(totalPrepTime);
        prepHoursInView = String.valueOf(prepHours);
        notifyPropertyChanged(BR.prepHoursInView);

        prepMinutes = getMinutes(totalPrepTime);
        prepMinutesInView = String.valueOf(prepMinutes);
        notifyPropertyChanged(BR.prepMinutesInView);
    }

    @Bindable
    public String getCookHoursInView() {
        return cookHoursInView;
    }

    public void setCookHoursInView(String cookHoursInView) {
        if (!updatingUi) {
            if (isCookHoursInViewChanged(cookHoursInView)) {
                if (!cookHoursInView.isEmpty()) {
                    int cookHoursParsed = parseIntegerFromString(cookHoursInView);

                    if (cookHoursParsed == MEASUREMENT_ERROR)
                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        this.cookHours = cookHoursParsed;
                        validateCookTime();
                    }
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
        if (!updatingUi) {
            if (isCookMinutesInViewChanged(cookMinutesInView)) {
                if (!cookMinutesInView.isEmpty()) {
                    int cookMinutesParsed = parseIntegerFromString(cookMinutesInView);

                    if (cookMinutesParsed == MEASUREMENT_ERROR)
                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        cookMinutes = cookMinutesParsed;
                        validateCookTime();
                    }
                }
            }
        }
    }

    private boolean isCookMinutesInViewChanged(String cookMinutesInView) {
        return !this.cookMinutesInView.equals(cookMinutesInView);
    }

    private void validateCookTime() {
        cookTimeErrorMessage.set(null);
        int maxAllowedCookTime = resources.getInteger(R.integer.recipe_max_cook_time_in_minutes);
        int totalCookTime = calculateTotalInMinutes(cookHours, cookMinutes);
        // todo - make the time in this string a calculation
        String errorMessage = resources.getString(R.string.input_error_recipe_cook_time_too_long);

        cookTimeValid = totalCookTime <= maxAllowedCookTime;

        if (!cookTimeValid) {
            cookTimeErrorMessage.set(errorMessage);
        }
        updateCookTime(totalCookTime);
        saveValidChanges();
    }

    private void updateCookTime(int totalCookTime) {
        cookHours = getHours(totalCookTime);
        cookHoursInView = String.valueOf(cookHours);
        notifyPropertyChanged(BR.cookHoursInView);

        cookMinutes = getMinutes(totalCookTime);
        cookMinutesInView = String.valueOf(cookMinutes);
        notifyPropertyChanged(BR.cookMinutesInView);
    }

    private int parseIntegerFromString(String integerToParse) {
        try {
            return Integer.parseInt(integerToParse);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
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

    private String numberFormatExceptionErrorMessage() {
        return resources.getString(R.string.number_format_exception);
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    private void saveValidChanges() {
        RecipeDurationEntity entity = updatedEntity();
        boolean isChanged = isChanged();
        boolean isValid = isValid();

        if (isChanged && isValid) {
            save(entity);
        }
        equaliseState(entity);
        submitModelStatus(isChanged, isValid);
    }

    private void submitModelStatus(boolean isChanged, boolean isValid) {
        if (!updatingUi) {
            modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStatus(
                    RecipeValidator.ModelName.DURATION_MODEL,
                    isChanged,
                    isValid
            ));
        }
    }

    private void equaliseState(RecipeDurationEntity durationEntity) {
        this.durationEntity = durationEntity;
    }

    private boolean isChanged() {
        if (durationEntity != null) {
            RecipeDurationEntity latestData = new RecipeDurationEntity(
                    durationEntity.getId(),
                    calculateTotalInMinutes(prepHours, prepMinutes),
                    calculateTotalInMinutes(cookHours, cookMinutes),
                    durationEntity.getCreateDate(),
                    durationEntity.getLastUpdate()
            );
            return !durationEntity.equals(latestData);
        } else {
            return false;
        }
    }

    private boolean isValid() {
        return prepTimeValid && cookTimeValid;
    }

    private RecipeDurationEntity updatedEntity() {
        return new RecipeDurationEntity(
                durationEntity.getId(),
                calculateTotalInMinutes(prepHours, prepMinutes),
                calculateTotalInMinutes(cookHours, cookMinutes),
                durationEntity.getCreateDate(),
                timeProvider.getCurrentTimeInMills()
        );
    }

    private void save(RecipeDurationEntity durationEntity) {
        repository.save(durationEntity);
    }
}
