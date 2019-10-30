package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

public class RecipePortionsViewModel
        extends
        ViewModel
        implements
        RecipeModelComposite.RecipeModelActions,
        DataSource.GetEntityCallback<RecipePortionsEntity> {

    private static final String TAG = "tkm-RecipePortionsVM";

    private Resources resources;
    private TimeProvider timeProvider;
    private UniqueIdProvider idProvider;
    private RepositoryRecipePortions repository;
    private ParseIntegerFromObservableHandler intFromObservable;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> servingsObservable = new ObservableField<>();
    public final ObservableField<String> sittingsObservable = new ObservableField<>();
    public final ObservableField<String> portionsObservable = new ObservableField<>();

    public final ObservableField<String> servingsErrorMessage = new ObservableField<>();
    public final ObservableField<String> sittingsErrorMessage = new ObservableField<>();

    private int servings;
    private int sittings;
    private String recipeId;
    private boolean isCloned;
    private boolean servingsValid;
    private boolean sittingsValid;
    private RecipePortionsEntity portionsEntity;

    private boolean updatingObservables;

    public RecipePortionsViewModel(RepositoryRecipePortions repository,
                                   TimeProvider timeProvider,
                                   UniqueIdProvider idProvider,
                                   Resources resources,
                                   ParseIntegerFromObservableHandler intFromObservable) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.resources = resources;
        this.intFromObservable = intFromObservable;

        servingsObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!servingsObservable.get().isEmpty()) {
                    servingsChanged();
                }
            }
        });
        sittingsObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!sittingsObservable.get().isEmpty())
                    sittingsChanged();
            }
        });
    }

    void setModelValidationSubmitter(
            RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (recipeId != null) {
            this.recipeId = recipeId;
            repository.getPortionsForRecipe(recipeId, this);
        } else {
            throw new RuntimeException("Recipe id cannot be null");
        }
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        isCloned = true;
        this.recipeId = newRecipeId;
        repository.getPortionsForRecipe(oldRecipeId, this);
    }

    private RecipePortionsEntity cloneEntity() {
        isCloned = false;
        long currentTimeStamp = timeProvider.getCurrentTimestamp();
        return new RecipePortionsEntity(
                idProvider.getUId(),
                recipeId,
                portionsEntity.getServings(),
                portionsEntity.getSittings(),
                currentTimeStamp,
                currentTimeStamp
        );
    }

    @Override
    public void onEntityLoaded(RecipePortionsEntity portionsEntity) {
        this.portionsEntity = portionsEntity;

        if (isCloned) {
            this.portionsEntity = cloneEntity();
            updateObservables();
            save(this.portionsEntity);
        } else {
            updateObservables();
        }
    }

    @Override
    public void onDataNotAvailable() {
        portionsEntity = createEntity(recipeId);
        save(portionsEntity);
        updateObservables();
    }

    private RecipePortionsEntity createEntity(String recipeId) {
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
        updatingObservables = true;
        servingsObservable.set(String.valueOf(portionsEntity.getServings()));
        sittingsObservable.set(String.valueOf(portionsEntity.getSittings()));
        updatingObservables = false;
        submitModelStatus();
    }

    private void servingsChanged() {
        servingsErrorMessage.set(null);
        int oldValue = servings;
        servings = parseIntegerFromObservableField(servingsObservable, servings);

        if (servings > 0) validateServings();
        if (servings != oldValue) submitModelStatus();
    }

    private void validateServings() {
        int minServings = resources.getInteger(R.integer.recipe_min_servings);
        int maxServings = resources.getInteger(R.integer.recipe_max_servings);
        String errorMessage = resources.getString(R.string.input_error_recipe_servings,
                minServings, maxServings);

        servingsValid = servings >= minServings && servings <= maxServings;
        if (!servingsValid)
            servingsErrorMessage.set(errorMessage);
        updatePortions();
    }

    private void sittingsChanged() {
        sittingsErrorMessage.set(null);
        int oldValue = sittings;
        sittings = parseIntegerFromObservableField(sittingsObservable, sittings);

        if (sittings > 0) validateSittings();
        if (sittings != oldValue) submitModelStatus();
    }

    private void validateSittings() {
        int minSittings = resources.getInteger(R.integer.recipe_min_sittings);
        int maxSittings = resources.getInteger(R.integer.recipe_max_sittings);
        String errorMessage = resources.getString(R.string.input_error_recipe_sittings,
                minSittings, maxSittings);

        sittingsValid = sittings >= minSittings && sittings <= maxSittings;
        if (!sittingsValid)
            sittingsErrorMessage.set(errorMessage);
        updatePortions();
    }

    private int parseIntegerFromObservableField(ObservableField<String> observable, int oldValue) {
        return intFromObservable.parseInt(resources, observable, oldValue);
    }

    private void updatePortions() {
        int portions = servings * sittings;
        portionsObservable.set(String.valueOf(portions));
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
        if (!updatingObservables) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.PORTIONS_MODEL,
                    isChanged(),
                    isValid()));

            if (isChanged() && isValid())
                save(updatedEntity());
        }
    }

    private boolean isValid() {
        return servingsValid && sittingsValid;
    }

    private boolean isChanged() {
        if (portionsEntity != null) {
            RecipePortionsEntity updatedEntity = new RecipePortionsEntity(
                    portionsEntity.getId(),
                    portionsEntity.getRecipeId(),
                    sittings,
                    servings,
                    portionsEntity.getCreateDate(),
                    portionsEntity.getLastUpdate()
            );
            return !portionsEntity.equals(updatedEntity);
        } else
            return false;
    }

    private void save(RecipePortionsEntity entity) {
        portionsEntity = entity;
        repository.save(entity);
    }
}
