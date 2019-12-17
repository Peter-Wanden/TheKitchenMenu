package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

public class RecipePortionsEditorViewModel
        extends
        ObservableViewModel
        implements
        RecipeModelComposite.RecipeModelActions,
        DataSource.GetEntityCallback<RecipePortionsEntity> {

    private static final String TAG = "tkm-" + RecipePortionsEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    private Resources resources;
    private TimeProvider timeProvider;
    private UniqueIdProvider idProvider;
    private RepositoryRecipePortions repository;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> servingsErrorMessage = new ObservableField<>();
    public final ObservableField<String> sittingsErrorMessage = new ObservableField<>();

    private String recipeId;
    private RecipePortionsEntity portionsEntity;

    private int servings;
    private String servingsInView = "";
    private boolean servingsValid;

    private int sittings;
    private String sittingsInView = "";
    private boolean sittingsValid;

    private boolean isCloned;
    private boolean dataLoading;
    private boolean updatingUi;

    public RecipePortionsEditorViewModel(RepositoryRecipePortions repository,
                                         TimeProvider timeProvider,
                                         UniqueIdProvider idProvider,
                                         Resources resources) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.resources = resources;
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
            this.recipeId = newRecipeId;
            getData(oldRecipeId);
        }
    }

    private void getData(String recipeId) {
        dataLoading = true;
        repository.getPortionsForRecipe(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipePortionsEntity portionsEntity) {
        if (isCloned) {
            this.portionsEntity = cloneEntity(portionsEntity);
        } else {
            this.portionsEntity = portionsEntity;
        }
        updateObservables();
    }

    private RecipePortionsEntity cloneEntity(RecipePortionsEntity toClone) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsEntity(
                idProvider.getUId(),
                recipeId,
                toClone.getServings(),
                toClone.getSittings(),
                currentTime,
                currentTime
        );
    }

    @Override
    public void onDataNotAvailable() {
        portionsEntity = createNewEntity();
        servingsValid = true;
        sittingsValid = true;
        save(portionsEntity);
        dataLoading = false;
        updateObservables();
    }

    private RecipePortionsEntity createNewEntity() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        int minServings = resources.getInteger(R.integer.recipe_min_servings);
        int minSittings = resources.getInteger(R.integer.recipe_min_sittings);
        return new RecipePortionsEntity(
                idProvider.getUId(),
                recipeId,
                minServings,
                minSittings,
                currentTime,
                currentTime
        );
    }

    private void updateObservables() {
        updatingUi = true;
        updateServings(portionsEntity.getServings());
        updateSittings(portionsEntity.getSittings());
        updatingUi = false;

        if (dataLoading) {
            validateServings();
            validateSittings();
            dataLoading = false;
        } else {
            submitModelStatus(isChanged(), isValid());
            return;
        }

        if (isCloned && isValid()) {
            save(portionsEntity);
            isCloned = false;
        }
    }

    @Bindable
    public String getServingsInView() {
        return servingsInView;
    }

    public void setServingsInView(String servingsInView) {
        if (isServingsInViewChanged(servingsInView)) {
            if (!servingsInView.isEmpty()) {
                int servingsParsed = parseIntegerFromString(servingsInView);

                if (servingsParsed == MEASUREMENT_ERROR)
                    servingsErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    servings = servingsParsed;
                    validateServings();
                }
            }
        }
    }

    private boolean isServingsInViewChanged(String servingsInView) {
        return !this.servingsInView.equals(servingsInView);
    }

    private void validateServings() {
        servingsErrorMessage.set(null);
        int minServings = resources.getInteger(R.integer.recipe_min_servings);
        int maxServings = resources.getInteger(R.integer.recipe_max_servings);
        String errorMessage = resources.getString(R.string.input_error_recipe_servings,
                minServings, maxServings);

        servingsValid = servings >= minServings && servings <= maxServings;
        if (!servingsValid) {
            servingsErrorMessage.set(errorMessage);
        }
        updateServings(servings);
        saveValidChanges();
    }

    private void updateServings(int servings) {
        this.servings = servings;
        servingsInView = String.valueOf(servings);
        notifyPropertyChanged(BR.servingsInView);
        notifyPropertyChanged(BR.portionsInView);
    }

    @Bindable
    public String getSittingsInView() {
        return sittingsInView;
    }

    public void setSittingsInView(String sittingsInView) {
        if (isSittingsInViewChanged(sittingsInView)) {
            if (!sittingsInView.isEmpty()) {
                int sittingsParsed = parseIntegerFromString(sittingsInView);

                if (sittingsParsed == MEASUREMENT_ERROR)
                    sittingsErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    sittings = sittingsParsed;
                    validateSittings();
                }
            }
        }
    }

    private boolean isSittingsInViewChanged(String sittingsInView) {
        return !this.sittingsInView.equals(sittingsInView);
    }

    private void validateSittings() {
        sittingsErrorMessage.set(null);
        int minSittings = resources.getInteger(R.integer.recipe_min_sittings);
        int maxSittings = resources.getInteger(R.integer.recipe_max_sittings);
        String errorMessage = resources.getString(R.string.input_error_recipe_sittings,
                minSittings, maxSittings);

        sittingsValid = sittings >= minSittings && sittings <= maxSittings;
        if (!sittingsValid) {
            sittingsErrorMessage.set(errorMessage);
        }
        updateSittings(sittings);
        saveValidChanges();
    }

    private void updateSittings(int sittings) {
        this.sittings = sittings;
        sittingsInView = String.valueOf(sittings);
        notifyPropertyChanged(BR.sittingsInView);
        notifyPropertyChanged(BR.portionsInView);
    }

    @Bindable
    public String getPortionsInView() {
        return String.valueOf(servings * sittings);
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

    void setModelValidationSubmitter(
            RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    private void saveValidChanges() {
        RecipePortionsEntity entity = updatedEntity();
        boolean isChanged = isChanged();
        boolean isValid = isValid();

        if (isChanged() && isValid()) {
            save(entity);
        }
        equaliseState(entity);
        submitModelStatus(isChanged, isValid);
    }

    private void submitModelStatus(boolean isChanged, boolean isValid) {
        if (!updatingUi) {
            modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStatus(
                    RecipeValidator.ModelName.PORTIONS_MODEL,
                    isChanged,
                    isValid
            ));
        }
    }

    private void equaliseState(RecipePortionsEntity entity) {
        this.portionsEntity = entity;
    }

    private boolean isChanged() {
        if (portionsEntity != null) {
            RecipePortionsEntity latestData = new RecipePortionsEntity(
                    portionsEntity.getId(),
                    portionsEntity.getRecipeId(),
                    sittings,
                    servings,
                    portionsEntity.getCreateDate(),
                    portionsEntity.getLastUpdate()
            );
            return !portionsEntity.equals(latestData);
        } else {
            return false;
        }
    }

    private boolean isValid() {
        return servingsValid && sittingsValid;
    }

    private RecipePortionsEntity updatedEntity() {
        return new RecipePortionsEntity(
                portionsEntity.getId(),
                portionsEntity.getRecipeId(),
                servings,
                sittings,
                portionsEntity.getCreateDate(),
                timeProvider.getCurrentTimeInMills()
        );
    }

    private void save(RecipePortionsEntity entity) {
        repository.save(entity);
    }
}
