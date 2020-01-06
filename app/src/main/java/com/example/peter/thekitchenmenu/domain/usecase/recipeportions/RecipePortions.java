package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

public class RecipePortions
        extends UseCaseInteractor<RecipePortionsRequest, RecipePortionsResponse>
        implements DataSource.GetEntityCallback<RecipePortionsEntity> {

    private static final String TAG = "tkm-" + RecipePortions.class.getSimpleName() + ": ";

    public enum Result {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        VALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_CHANGED,
    }

    public static final String DO_NOT_CLONE = "";
    public static final int MIN_SERVINGS = 1;
    public static final int MIN_SITTINGS = 1;

    private final TimeProvider timeProvider;
    private final UniqueIdProvider idProvider;
    private final RepositoryRecipePortions repository;

    private final int maxServings;
    private final int maxSittings;

    private String recipeId = "";
    private RecipePortionsModel requestModel;
    private RecipePortionsModel responseModel;
    private boolean isCloned;

    public RecipePortions(TimeProvider timeProvider,
                          UniqueIdProvider idProvider,
                          RepositoryRecipePortions repository,
                          int maxServings,
                          int maxSittings) {
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.repository = repository;
        this.maxServings = maxServings;
        this.maxSittings = maxSittings;
        requestModel = new RecipePortionsModel.Builder().getDefault().build();
        responseModel = new RecipePortionsModel.Builder().getDefault().build();
    }

    @Override
    protected void execute(RecipePortionsRequest request) {
        requestModel = request.getModel();
        System.out.println(TAG + request);
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            sendResponse();
        }
    }

    private boolean isNewRequest(RecipePortionsRequest request) {
        return !recipeId.equals(request.getRecipeId()) ||
                requestModel.equals(new RecipePortionsModel.Builder().getDefault().build());
    }

    private void loadData(RecipePortionsRequest request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getPortionsForRecipe(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(RecipePortionsRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    @Override
    public void onEntityLoaded(RecipePortionsEntity entity) {
        requestModel = convertEntityToModel(entity);

        if (isCloned) {
            save(requestModel);
            isCloned = false;
        }

        equaliseState();
        sendResponse();
    }

    private RecipePortionsModel convertEntityToModel(RecipePortionsEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsModel.Builder().
                setId(isCloned ? idProvider.getUId() : entity.getId()).
                setRecipeId(isCloned ? recipeId : entity.getRecipeId()).
                setServings(entity.getServings()).
                setSittings(entity.getSittings()).
                setCreateDate(isCloned ? currentTime : entity.getCreateDate()).
                setLastUpdate(isCloned ? currentTime : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        requestModel = createNewModel();
        equaliseState();
        save(responseModel);
        sendResponse();
    }

    private RecipePortionsModel createNewModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsModel.Builder().
                setId(idProvider.getUId()).
                setRecipeId(recipeId).
                setServings(MIN_SERVINGS).
                setSittings(MIN_SITTINGS).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void sendResponse() {
        RecipePortionsResponse.Builder builder = new RecipePortionsResponse.Builder();

        if (!isValid() && !isChanged()) {
            builder.setResult(Result.INVALID_UNCHANGED);

        } else if (isValid() && !isChanged()) {
            builder.setResult(Result.VALID_UNCHANGED);

        } else if (!isValid() && isChanged()) {
            builder.setResult(Result.INVALID_CHANGED);

        } else if (isValid() && isChanged()) {
            builder.setResult(Result.VALID_CHANGED);

            requestModel = RecipePortionsModel.Builder.
                    basedOn(requestModel).
                    setLastUpdate(timeProvider.getCurrentTimeInMills()).
                    build();
            save(requestModel);
        }

        equaliseState();
        RecipePortionsResponse response = builder.setModel(responseModel).build();
        System.out.println(TAG + response);
        getUseCaseCallback().onSuccess(response);
    }

    private void equaliseState() {
        responseModel = requestModel;
    }

    private boolean isValid() {
        return requestModel.getServings() >= MIN_SERVINGS &&
                requestModel.getServings() <= maxServings &&
                requestModel.getSittings() >= MIN_SITTINGS &&
                requestModel.getSittings() <= maxSittings;
    }

    private boolean isChanged() {
        return !requestModel.equals(responseModel);
    }

    private void save(RecipePortionsModel model) {
        repository.save(convertModelToEntity(model));
    }

    private RecipePortionsEntity convertModelToEntity(RecipePortionsModel model) {
        return new RecipePortionsEntity(
                model.getId(),
                model.getRecipeId(),
                model.getServings(),
                model.getSittings(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
