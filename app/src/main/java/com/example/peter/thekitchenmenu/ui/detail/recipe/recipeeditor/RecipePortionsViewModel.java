package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipePortions;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

public class RecipePortionsViewModel
        extends
        ViewModel
        implements
        RecipeModelComposite.RecipeModelActions,
        DataSource.GetEntityCallback<RecipePortionsEntity> {

    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;
    private DataSourceRecipePortions dataSource;
    private TimeProvider timeProvider;
    private UniqueIdProvider idProvider;
    private Resources resources;
    private ParseIntegerFromObservableHandler intFromObservable;

    public final ObservableField<String> servingsObservable = new ObservableField<>();
    public final ObservableField<String> sittingsObservable = new ObservableField<>();
    public final ObservableField<String> portionsObservable = new ObservableField<>();

    public final ObservableField<String> servingsErrorMessage = new ObservableField<>();
    public final ObservableField<String> sittingsErrorMessage = new ObservableField<>();

    private RecipePortionsEntity portionsEntity;
    private String recipeId;
    private int servings;
    private int sittings;
    private boolean servingsValid;
    private boolean sittingsValid;

    private boolean observablesUpdating;

    public RecipePortionsViewModel(DataSourceRecipePortions dataSource,
                                   TimeProvider timeProvider,
                                   UniqueIdProvider idProvider,
                                   Resources resources,
                                   ParseIntegerFromObservableHandler intFromObservable) {
        this.dataSource = dataSource;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.resources = resources;
        this.intFromObservable = intFromObservable;

        servingsObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!servingsObservable.get().isEmpty())
                    updateServings();
            }
        });
        sittingsObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!sittingsObservable.get().isEmpty())
                    updateSittings();
            }
        });
    }

    @Override
    public void start(String recipeId) {
        this.recipeId = recipeId;
        dataSource.getPortionsForRecipe(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipePortionsEntity portionsEntity) {
        this.portionsEntity = portionsEntity;
        updateObservables();
    }

    @Override
    public void onDataNotAvailable() {
        portionsEntity = createNewPortionsEntity(recipeId);
        save(portionsEntity);
        updateObservables();
    }

    private RecipePortionsEntity createNewPortionsEntity(String recipeId) {
        long currentTime = timeProvider.getCurrentTimestamp();
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
        observablesUpdating = true;
        servingsObservable.set(String.valueOf(portionsEntity.getServings()));
        sittingsObservable.set(String.valueOf(portionsEntity.getSittings()));
        observablesUpdating = false;
        submitModelStatus();
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        if (!observablesUpdating) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.PORTIONS_MODEL,
                    isChanged(),
                    isValid()
            ));

            if (isChanged() && isValid())
                save(updatedEntity());
        }
    }

    private boolean isChanged() {
        if (portionsEntity != null) {
            RecipePortionsEntity latestPortionsEntity = new RecipePortionsEntity(
                    portionsEntity.getId(),
                    portionsEntity.getRecipeId(),
                    Integer.valueOf(servingsObservable.get()),
                    Integer.valueOf(sittingsObservable.get()),
                    portionsEntity.getCreateDate(),
                    portionsEntity.getLastUpdate()
            );

            return !portionsEntity.equals(latestPortionsEntity);
        } else
            return false;
    }

    private boolean isValid() {
        return servingsValid && sittingsValid;
    }

    private void updateServings() {
        servings = parseIntegerFromObservableField(servingsObservable, servings);
        if (servings > 0)
            validateServings();
    }

    private void validateServings() {
        servingsErrorMessage.set(null);
        int minServings = resources.getInteger(R.integer.recipe_min_servings);
        int maxServings = resources.getInteger(R.integer.recipe_max_servings);
        String errorMessage = resources.getString(R.string.input_error_recipe_servings,
                minServings, maxServings);

        servingsValid = servings >= minServings && servings <= maxServings;

        if (!servingsValid)
            servingsErrorMessage.set(errorMessage);

        System.out.println("servingsValid=" + servingsValid);
        updatePortions();
        submitModelStatus();
    }

    private void updateSittings() {
        sittings = parseIntegerFromObservableField(sittingsObservable, sittings);
        if (sittings > 0)
            validateSittings();
    }

    private void validateSittings() {
        sittingsErrorMessage.set(null);
        int minSittings = resources.getInteger(R.integer.recipe_min_sittings);
        int maxSittings = resources.getInteger(R.integer.recipe_max_sittings);
        String errorMessage = resources.getString(R.string.input_error_recipe_sittings,
                minSittings, maxSittings);

        sittingsValid = sittings >= minSittings && sittings <= maxSittings;

        if (!sittingsValid)
            sittingsErrorMessage.set(errorMessage);

        System.out.println("sittingsValid=" + sittingsValid);
        updatePortions();
        submitModelStatus();
    }

    private int parseIntegerFromObservableField(ObservableField<String> observable, int oldValue) {
        return intFromObservable.parseInt(resources, observable, oldValue);
    }

    private void updatePortions() {
        int portions = servings + sittings;
        portionsObservable.set(String.valueOf(portions));
    }

    void setModelValidationSubmitter(
            RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    private RecipePortionsEntity updatedEntity() {
        return new RecipePortionsEntity(
                portionsEntity.getId(),
                portionsEntity.getRecipeId(),
                servings,
                sittings,
                portionsEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void submitModelStatus() {
        if (!observablesUpdating) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.PORTIONS_MODEL,
                    isChanged(),
                    isValid()
            ));
            if (isChanged() && isValid())
                save(updatedEntity());
        }
    }

    private void save(RecipePortionsEntity entity) {
        dataSource.save(entity);
    }
}
