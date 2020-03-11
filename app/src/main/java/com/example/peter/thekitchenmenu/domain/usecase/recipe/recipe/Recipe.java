package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class Recipe extends UseCase implements DataSource.GetEntityCallback<RecipeEntity> {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    // TODO - Rename to RecipeMetaData
    //  last update - should be the time of the last updated component
    //  keep recipe state, creator, etc. for fast access of statistics when searching
    // TODO - Data layer:
    //  have uid as well as recipeId
    //  keep a copy of all data as it changes

    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipe repository;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String id = "";
    private String parentId;

    private RecipePersistenceModel persistenceModel;

    public Recipe(@Nonnull TimeProvider timeProvider,
                  @Nonnull RepositoryRecipe repository) {
        this.timeProvider = timeProvider;
        this.repository = repository;
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeRequest recipeRequest = (RecipeRequest) request;
        System.out.println(TAG + recipeRequest);

        if (isNewRequest(recipeRequest.getId())) {
            id = recipeRequest.getId();
            loadData(id);
        } else {
            buildResponse();
        }
    }

    private boolean isNewRequest(String id) {
        return !this.id.equals(id);
    }

    private void loadData(String recipeId) {
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeEntity entity) {
        persistenceModel = convertEntityToPersistenceModel(entity);
        buildResponse();
    }

    private RecipePersistenceModel convertEntityToPersistenceModel(RecipeEntity entity) {
        return new RecipePersistenceModel.Builder().
                setId(entity.getId()).
                setParentId(entity.getParentId()).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        buildResponse();
    }

    private RecipePersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return RecipePersistenceModel.Builder.getDefault().
                setId(id).
                setParentId("").
                setCreatedBy(Constants.getUserId()).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void buildResponse() {
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }

        RecipeResponse response = new RecipeResponse.Builder().
                setMetadata(getMetadata()).
                setModel(getResponseModel()).
                build();

        sendResponse(response);
    }

    private RecipeResponse.Metadata getMetadata() {
        List<FailReasons> failReasons = new ArrayList<>();
        failReasons.add(CommonFailReason.NONE);
        return new RecipeResponse.Metadata.Builder().
                setState(RecipeStateCalculator.ComponentState.VALID_CHANGED).
                setFailReasons(failReasons).
                setCreateDate(0L).
                setLasUpdate(0L).
                build();
    }

    private RecipeResponse.Model getResponseModel() {
        return new RecipeResponse.Model.Builder().setParentId("").build();
    }

    private void sendResponse(RecipeResponse response) {
        if (failReasons.contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void save(RecipePersistenceModel model) {
        repository.save(convertPersistenceModelToEntity(model));
    }

    private RecipeEntity convertPersistenceModelToEntity(RecipePersistenceModel model) {
        return new RecipeEntity(
                model.getId(),
                model.getParentId(),
                model.getCreatedBy(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
