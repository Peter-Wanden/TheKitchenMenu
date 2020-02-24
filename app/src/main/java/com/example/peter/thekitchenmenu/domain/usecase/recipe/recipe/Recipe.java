package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import javax.annotation.Nonnull;

public class Recipe
        extends UseCase<RecipeRequest, RecipeResponse>
        implements DataSource.GetEntityCallback<RecipeEntity> {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    public static final String DO_NOT_CLONE = "";

    @Nonnull
    private final RepositoryRecipe repository;
    @Nonnull
    private final TimeProvider timeProvider;

    private String id = "";
    private String parentId;
    private boolean isCloned;

    private RecipePersistenceModel persistenceModel;

    public Recipe(@Nonnull RepositoryRecipe repository,
                  @Nonnull TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    @Override
    protected void execute(RecipeRequest request) {
        System.out.println(TAG + request);

        if (isNewRequest(request.getId())) {
            extractIds(request);
        } else {
            buildResponse();
        }
    }

    private boolean isNewRequest(String id) {
        return !this.id.equals(id);
    }

    private void extractIds(RecipeRequest request) {
        if (isCloneRequest(request)) {
            id = request.getCloneToId();
            parentId = request.getId();
        } else {
            id = request.getId();
        }
        loadData(request.getId());
    }

    private boolean isCloneRequest(RecipeRequest request) {
        return isCloned = !request.getCloneToId().equals(DO_NOT_CLONE);
    }

    private void loadData(String recipeId) {
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeEntity entity) {
        persistenceModel = convertEntityToPersistenceModel(entity);

        if (isCloned) {
            save(persistenceModel);
            isCloned = false;
        }
        buildResponse();
    }

    private RecipePersistenceModel convertEntityToPersistenceModel(RecipeEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePersistenceModel.Builder().
                setId(isCloned ? id : entity.getId()).
                setParentId(isCloned ? parentId : entity.getParentId()).
                setCreatedBy(isCloned ? Constants.getUserId() : entity.getCreatedBy()).
                setCreateDate(isCloned ? currentTime : entity.getCreateDate()).
                setLastUpdate(isCloned ? currentTime : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
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
        RecipeResponse response = new RecipeResponse.Builder().
                setId(persistenceModel.getId()).
                setParentId(persistenceModel.getParentId()).
                setCreatedBy(persistenceModel.getCreatedBy()).
                setCreateDate(persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel.getLastUpdate()).
                build();

        sendResponse(response);
    }

    private void sendResponse(RecipeResponse response) {
        getUseCaseCallback().onSuccess(response);
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
