package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

public class RecipeIdentity
        extends UseCaseInteractor<RecipeIdentityRequest, RecipeIdentityResponse>
        implements DataSource.GetEntityCallback<RecipeIdentityEntity> {

    private static final String TAG = "tkm-" + RecipeIdentity.class.getSimpleName() + " ";

    public static final String DO_NOT_CLONE = "";

    public enum Result {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        VALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_CHANGED,
    }

    private RepositoryRecipeIdentity repository;
    private TimeProvider timeProvider;
    private RecipeIdentityModel requestModel;
    private RecipeIdentityModel responseModel;

    private String recipeId = "";
    private boolean isCloned;

    public RecipeIdentity(RepositoryRecipeIdentity repository, TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        requestModel = new RecipeIdentityModel.Builder().getDefault().build();
        responseModel = new RecipeIdentityModel.Builder().getDefault().build();
    }

    @Override
    protected void execute(RecipeIdentityRequest request) {
        requestModel = request.getModel();
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            sendResponse();
        }
    }

    private boolean isNewRequest(RecipeIdentityRequest request) {
        return !recipeId.equals(request.getRecipeId()) ||
                requestModel.equals(new RecipeIdentityModel.Builder().getDefault().build());
    }

    private void loadData(RecipeIdentityRequest request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getById(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(RecipeIdentityRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity entity) {
        requestModel = convertEntityToModel(entity);

        if (isCloned) {
            save(requestModel);
            isCloned = false;
        }
        responseModel = requestModel;
        sendResponse();
    }

    private RecipeIdentityModel convertEntityToModel(RecipeIdentityEntity entity) {
        long time = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityModel.Builder().
                setId(isCloned ? recipeId : entity.getId()).
                setTitle(entity.getTitle() == null ? "" : entity.getTitle()).
                setDescription(entity.getDescription() == null ? "" : entity.getDescription()).
                setCreateDate(isCloned ? time : entity.getCreateDate()).
                setLastUpdate(isCloned ? time : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        requestModel = createNewModel();
        responseModel = requestModel;

        RecipeIdentityResponse response = new RecipeIdentityResponse.Builder().
                setModel(responseModel).
                setRecipeId(recipeId).
                setResult(Result.DATA_UNAVAILABLE).
                build();

        getUseCaseCallback().onSuccess(response);
    }

    private RecipeIdentityModel createNewModel() {
        long time = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityModel.Builder().getDefault().
                setId(recipeId).
                setCreateDate(time).
                setLastUpdate(time).
                build();
    }

    private void sendResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setRecipeId(recipeId);

        if (!isValid() && !isModelChanged()) {
            builder.setResult(Result.INVALID_UNCHANGED);
        } else if (isValid() && !isModelChanged()) {
            builder.setResult(Result.VALID_UNCHANGED);
        } else if (!isValid() && isModelChanged()) {
            builder.setResult(Result.INVALID_CHANGED);
        } else if (isValid() && isModelChanged()) {
            builder.setResult(Result.VALID_CHANGED);
            save(requestModel);
        }

        responseModel = requestModel;
        RecipeIdentityResponse response = builder.setModel(responseModel).build();
        getUseCaseCallback().onSuccess(response);
    }

    private boolean isModelChanged() {
        return !requestModel.equals(responseModel);
    }

    private boolean isValid() {
        if (requestModel.equals(new RecipeIdentityModel.Builder().getDefault().build())) {
            return false;
        } else {
            return !requestModel.getTitle().isEmpty();
        }
    }

    private void save(RecipeIdentityModel model) {
        repository.save(convertModelToEntity(model));
    }

    // todo - move model / entity conversions to the data layer
    private RecipeIdentityEntity convertModelToEntity(RecipeIdentityModel model) {
        return new RecipeIdentityEntity(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
