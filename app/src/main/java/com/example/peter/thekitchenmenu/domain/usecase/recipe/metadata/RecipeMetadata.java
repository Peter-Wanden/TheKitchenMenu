package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeMetaData;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Calculates and stores {@link Recipe} state information based on {@link RecipeComponentResponse}'s
 * Always use in conjunction with a {@link Recipe}
 */
public class RecipeMetadata
        extends UseCase
        implements PrimitiveDataSource.GetEntityCallback<RecipeMetadataEntity> {

    private static final String TAG = "tkm-" + RecipeMetadata.class.getSimpleName() + ": ";

    public enum RecipeState {
        DATA_UNAVAILABLE(0),
        INVALID_UNCHANGED(1),
        INVALID_CHANGED(2),
        VALID_CHANGED(3),
        VALID_UNCHANGED(4),
        COMPLETE(5);

        private final int stateLevel;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, RecipeState> recipeStates = new HashMap<>();

        RecipeState(int stateLevel) {
            this.stateLevel = stateLevel;
        }

        static {
            for (RecipeState recipeState : RecipeState.values()) {
                recipeStates.put(recipeState.stateLevel, recipeState);
            }
        }

        public static RecipeState getStateFromSeverityLevel(int severityLevel) {
            return recipeStates.get(severityLevel);
        }

        public int getStateLevel() {
            return stateLevel;
        }
    }

    public enum FailReason implements FailReasons {
        MISSING_COMPONENTS,
        INVALID_COMPONENTS,
        NONE
    }

    public enum ComponentName {
        IDENTITY,
        DURATION,
        COURSE,
        PORTIONS,
        TEXT_VALIDATOR,
        RECIPE_METADATA,
        RECIPE
    }

    public enum ComponentState {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_UNCHANGED,
        VALID_CHANGED
    }

    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipeMetaData repository;
    private RecipeMetadataPersistenceModel persistenceModel;
    @Nonnull
    private final Set<ComponentName> requiredComponents;

    private RecipeState recipeState;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String id = "";
    private String parentId = "";
    private HashMap<ComponentName, ComponentState> componentStates;
    private boolean hasRequiredComponents;
    private boolean hasInvalidModels;

    // TODO - Rename to RecipeMetaData
    //  last update - should be the time of the last updated component
    // TODO - Data layer:
    //  have uid as well as recipeId
    //  keep a copy of all metadata as it changes

    public RecipeMetadata(@Nonnull TimeProvider timeProvider,
                          @Nonnull RepositoryRecipeMetaData repository,
                          @Nonnull Set<ComponentName> requiredComponents) {

        this.timeProvider = timeProvider;
        this.repository = repository;
        this.requiredComponents = requiredComponents;

        recipeState = RecipeState.COMPLETE;
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeMetadataRequest metadataRequest = (RecipeMetadataRequest) request;
        System.out.println(TAG + metadataRequest);

        recipeState = RecipeState.COMPLETE;
        failReasons.clear();
        componentStates = metadataRequest.getModel().getComponentStates();

        if (isNewRequest(metadataRequest.getId())) {
            id = metadataRequest.getId();
            parentId = metadataRequest.getModel().getParentId();
            loadData(id);
        } else {
            calculateState();
        }
    }

    private boolean isNewRequest(String id) {
        return !this.id.equals(id);
    }

    private void loadData(String recipeId) {
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeMetadataEntity entity) {
        persistenceModel = convertPrimitiveToPersistenceModel(entity);
//        buildResponse();
    }

    private RecipeMetadataPersistenceModel convertPrimitiveToPersistenceModel(RecipeMetadataEntity entity) {
        return new RecipeMetadataPersistenceModel.Builder().
                setId(entity.getId()).
                setParentId(entity.getParentId()).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        calculateState();
    }

    private RecipeMetadataPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return RecipeMetadataPersistenceModel.Builder.getDefault().
                setId(id).
                setParentId(parentId).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void calculateState() {
        checkForRequiredComponents();
        checkForInvalidComponents();
        checkForValidComponents();

        buildResponse();
    }

    private void checkForRequiredComponents() {
        int noOfRequiredComponents = requiredComponents.size();
        int noOfRequiredComponentsSubmitted = 0;

        for (ComponentName componentName : requiredComponents) {
            if (componentStates.containsKey(componentName) &&
                    componentStates.get(componentName) != ComponentState.DATA_UNAVAILABLE) {
                noOfRequiredComponentsSubmitted++;
            } else {
                componentStates.put(componentName, ComponentState.DATA_UNAVAILABLE);
                recipeState = RecipeState.DATA_UNAVAILABLE;
                addFailReasonMissingModels();
            }
        }
        hasRequiredComponents = noOfRequiredComponentsSubmitted >= noOfRequiredComponents;
    }

    private void addFailReasonMissingModels() {
        if (!failReasons.contains(FailReason.MISSING_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_COMPONENTS);
        }
    }

    private void checkForInvalidComponents() {
        if (hasRequiredComponents) {
            for (ComponentName componentName : componentStates.keySet()) {
                ComponentState componentState = componentStates.get(componentName);

                if (ComponentState.INVALID_UNCHANGED == componentState  &&
                        recipeState.getStateLevel() > RecipeState.INVALID_UNCHANGED.stateLevel) {

                    recipeState = RecipeState.INVALID_UNCHANGED;
                    addFailReasonInvalidComponents();

                } else if (componentState == ComponentState.INVALID_CHANGED &&
                        recipeState.getStateLevel() > RecipeState.INVALID_CHANGED.stateLevel) {

                    recipeState = RecipeState.INVALID_CHANGED;
                    addFailReasonInvalidComponents();
                }
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        hasInvalidModels = true;
        failReasons.add(FailReason.INVALID_COMPONENTS);
    }

    private void checkForValidComponents() {
        if (hasRequiredComponents && !hasInvalidModels) {
            for (ComponentName componentName : componentStates.keySet()) {

                ComponentState componentState = componentStates.get(componentName);
                if (componentState == ComponentState.VALID_UNCHANGED &&
                        recipeState.getStateLevel() > RecipeState.VALID_UNCHANGED.stateLevel) {

                    recipeState = RecipeState.VALID_UNCHANGED;
                    addFailReasonNone();

                } else if (componentState == ComponentState.VALID_CHANGED &&
                        recipeState.getStateLevel() > RecipeState.VALID_CHANGED.stateLevel) {

                    recipeState = RecipeState.VALID_CHANGED;
                    addFailReasonNone();
                }
            }
        }
    }

    private void addFailReasonNone() {
        if (!failReasons.contains(FailReason.NONE)) {
            failReasons.add(FailReason.NONE);
        }
    }

    private boolean isValid() {
        return recipeState == RecipeState.VALID_UNCHANGED ||
                recipeState == RecipeState.VALID_CHANGED ||
                recipeState == RecipeState.COMPLETE;
    }

    private RecipeComponentMetadata getMetadata() {
        List<FailReasons> failReasons = new ArrayList<>();
        failReasons.add(CommonFailReason.NONE);
        return new RecipeComponentMetadata.Builder().
                setState(ComponentState.VALID_CHANGED).
                setFailReasons(failReasons).
                setCreateDate(0L).
                setLasUpdate(0L).
                build();
    }

    private void buildResponse() {
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }

        RecipeMetadataResponse response = new RecipeMetadataResponse.Builder().
                setId(id).
                setModel(getModel()).
                build();

        sendResponse(response);
    }

    private RecipeMetadataResponse.Model getModel() {
        return new RecipeMetadataResponse.Model.Builder().
                setParentId(persistenceModel.getParentId()).
                setRecipeState(RecipeState.getStateFromSeverityLevel(recipeState.getStateLevel())).
                setFailReasons(new ArrayList<>(failReasons)).
                setComponentStates(new LinkedHashMap<>(componentStates)).
                build();
    }

    private void sendResponse(RecipeMetadataResponse response) {
        System.out.println(TAG + response);
        if (isValid()) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void save(RecipeMetadataPersistenceModel model) {
        repository.save(convertPersistenceModelPrimitive(model));
    }

    private RecipeMetadataEntity convertPersistenceModelPrimitive(RecipeMetadataPersistenceModel model) {
        return new RecipeMetadataEntity(
                model.getId(),
                model.getRecipeId(),
                model.getParentId(),
                model.getCreatedBy(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
