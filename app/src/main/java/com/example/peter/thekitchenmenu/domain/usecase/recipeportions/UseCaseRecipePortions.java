package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.Objects;

import javax.annotation.Nonnull;

public class UseCaseRecipePortions
        extends UseCaseInteractor<UseCaseRecipePortions.Request, UseCaseRecipePortions.Response>
        implements DataSource.GetEntityCallback<RecipePortionsEntity> {

    public static final String DO_NOT_CLONE = "";
    public static final int MIN_SERVINGS = 1;
    public static final int MIN_SITTINGS = 1;

    private final TimeProvider timeProvider;
    private final UniqueIdProvider idProvider;
    private final RepositoryRecipePortions repository;

    private final int maxServings;
    private final int maxSittings;

    private String recipeId = "";
    private Model requestModel = Model.Builder.getDefault().build();
    private Model responseModel = Model.Builder.getDefault().build();
    private boolean isCloned;

    public UseCaseRecipePortions(TimeProvider timeProvider,
                                 UniqueIdProvider idProvider,
                                 RepositoryRecipePortions repository,
                                 int maxServings,
                                 int maxSittings) {
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.repository = repository;
        this.maxServings = maxServings;
        this.maxSittings = maxSittings;
    }

    @Override
    protected void execute(Request request) {
        requestModel = request.getModel();
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            sendResponse();
        }
    }

    private boolean isNewRequest(Request request) {
        return !recipeId.equals(request.getRecipeId()) ||
                requestModel.equals(Model.Builder.getDefault().build());
    }

    private void loadData(Request request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getPortionsForRecipe(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(Request request) {
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

    private Model convertEntityToModel(RecipePortionsEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new Model.Builder().
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

    private Model createNewModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new Model.Builder().
                setId(idProvider.getUId()).
                setRecipeId(recipeId).
                setServings(MIN_SERVINGS).
                setSittings(MIN_SITTINGS).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void equaliseState() {
        responseModel = requestModel;
    }

    private void sendResponse() {
        Response.Builder builder = new Response.Builder();

        if (!isValid() && !isChanged()) {
            builder.setResult(Result.INVALID_UNCHANGED);
        } else if (isValid() && !isChanged()) {
            builder.setResult(Result.VALID_UNCHANGED);
        } else if (!isValid() && isChanged()) {
            builder.setResult(Result.INVALID_CHANGED);
        } else if (isValid() && isChanged()) {
            builder.setResult(Result.VALID_CHANGED);
            save(requestModel);
        }

        equaliseState();
        Response response = builder.setModel(responseModel).build();
        getUseCaseCallback().onSuccess(response);
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

    private void save(Model model) {
        repository.save(convertModelToEntity(model));
    }

    private RecipePortionsEntity convertModelToEntity(Model model) {
        return new RecipePortionsEntity(
                model.getId(),
                model.getRecipeId(),
                model.getServings(),
                model.getSittings(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }

    public static final class Model implements PersistenceModel {
        @Nonnull
        private final String id;
        @Nonnull
        private final String recipeId;
        private final int servings;
        private final int sittings;
        private final long createDate;
        private final long lastUpdate;

        private Model(@Nonnull String id,
                      @Nonnull String recipeId,
                      int servings,
                      int sittings,
                      long createDate,
                      long lastUpdate) {
            this.id = id;
            this.recipeId = recipeId;
            this.servings = servings;
            this.sittings = sittings;
            this.createDate = createDate;
            this.lastUpdate = lastUpdate;
        }

        @Override
        @Nonnull
        public String getId() {
            return id;
        }

        @Nonnull
        public String getRecipeId() {
            return recipeId;
        }

        public int getServings() {
            return servings;
        }

        public int getSittings() {
            return sittings;
        }

        public long getCreateDate() {
            return createDate;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return servings == model.servings &&
                    sittings == model.sittings &&
                    createDate == model.createDate &&
                    lastUpdate == model.lastUpdate &&
                    id.equals(model.id) &&
                    recipeId.equals(model.recipeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, recipeId, servings, sittings, createDate, lastUpdate);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "id='" + id + '\'' +
                    ", recipeId='" + recipeId + '\'' +
                    ", servings=" + servings +
                    ", sittings=" + sittings +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }

        public static class Builder {
            private String id;
            private String recipeId;
            private int servings;
            private int sittings;
            private long createDate;
            private long lastUpdate;

            public static Builder basedOn(@Nonnull Model oldModel) {
                return new Builder().
                        setId(oldModel.getId()).
                        setRecipeId(oldModel.getRecipeId()).
                        setServings(oldModel.getServings()).
                        setSittings(oldModel.getSittings()).
                        setCreateDate(oldModel.getCreateDate()).
                        setLastUpdate(oldModel.getLastUpdate());
            }

            public static Builder getDefault() {
                return new Builder().
                        setServings(MIN_SERVINGS).
                        setSittings(MIN_SITTINGS);
            }

            public Builder setId(@Nonnull String id) {
                this.id = id;
                return this;
            }

            public Builder setRecipeId(@Nonnull String recipeId) {
                this.recipeId = recipeId;
                return this;
            }

            public Builder setServings(int servings) {
                this.servings = servings;
                return this;
            }

            public Builder setSittings(int sittings) {
                this.sittings = sittings;
                return this;
            }

            public Builder setCreateDate(long createDate) {
                this.createDate = createDate;
                return this;
            }

            public Builder setLastUpdate(long lastUpdate) {
                this.lastUpdate = lastUpdate;
                return this;
            }

            public Model build() {
                return new Model(
                        id,
                        recipeId,
                        servings,
                        sittings,
                        createDate,
                        lastUpdate
                );
            }
        }
    }

    public static final class Request implements UseCaseInteractor.Request {
        @Nonnull
        private final String recipeId;
        @Nonnull
        private final String cloneToRecipeId;
        @Nonnull
        private final Model model;

        private Request(@Nonnull String recipeId,
                        @Nonnull String cloneToRecipeId,
                        @Nonnull Model model) {
            this.recipeId = recipeId;
            this.cloneToRecipeId = cloneToRecipeId;
            this.model = model;
        }

        @Nonnull
        public String getRecipeId() {
            return recipeId;
        }

        @Nonnull
        public String getCloneToRecipeId() {
            return cloneToRecipeId;
        }

        @Nonnull
        public Model getModel() {
            return model;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request request = (Request) o;
            return recipeId.equals(request.recipeId) &&
                    cloneToRecipeId.equals(request.cloneToRecipeId) &&
                    model.equals(request.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(recipeId, cloneToRecipeId, model);
        }

        @Override
        public String toString() {
            return "Request{" +
                    "recipeId='" + recipeId + '\'' +
                    ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                    ", model=" + model +
                    '}';
        }

        public static class Builder {
            private String recipeId;
            private String cloneToRecipeId;
            private Model model;

            public Builder getDefault() {
                return new Builder().
                        setRecipeId("").
                        setCloneToRecipeId("").
                        setModel(Model.Builder.
                                getDefault().
                                build());
            }

            public static Builder basedOnRequest(@Nonnull Request request) {
                return new Builder().
                        setRecipeId(request.getRecipeId()).
                        setCloneToRecipeId(request.getCloneToRecipeId()).
                        setModel(request.getModel());
            }

            public Builder setRecipeId(String recipeId) {
                this.recipeId = recipeId;
                return this;
            }

            public Builder setCloneToRecipeId(String cloneToRecipeId) {
                this.cloneToRecipeId = cloneToRecipeId;
                return this;
            }

            public Builder setModel(Model model) {
                this.model = model;
                return this;
            }

            public Request build() {
                return new Request(
                        recipeId,
                        cloneToRecipeId,
                        model
                );
            }
        }
    }

    public static final class Response implements UseCaseInteractor.Response {
        @Nonnull
        private final Result result;
        @Nonnull
        private final Model model;

        private Response(@Nonnull Result result, @Nonnull Model model) {
            this.result = result;
            this.model = model;
        }

        @Nonnull
        public Result getResult() {
            return result;
        }

        @Nonnull
        public Model getModel() {
            return model;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response response = (Response) o;
            return result == response.result &&
                    model.equals(response.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(result, model);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result=" + result +
                    ", model=" + model +
                    '}';
        }

        public static class Builder {
            private Result result;
            private Model model;

            public static Builder getDefault() {
                return new Builder().
                        setResult(Result.INVALID_UNCHANGED).
                        setModel(Model.Builder.
                                getDefault().
                                build());
            }

            public Builder setResult(Result result) {
                this.result = result;
                return this;
            }

            public Builder setModel(Model model) {
                this.model = model;
                return this;
            }

            public Response build() {
                return new Response(
                        result,
                        model
                );
            }
        }
    }
}
