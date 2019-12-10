package com.example.peter.thekitchenmenu.domain.usecase.recipeIdentity;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UseCaseRecipeIdentity
        extends UseCaseInteractor<UseCaseRecipeIdentity.Request, UseCaseRecipeIdentity.Response>
        implements DataSource.GetEntityCallback<RecipeIdentityEntity> {

    private static final String TAG = "tkm-" + UseCaseRecipeIdentity.class.getSimpleName() + " ";
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
    private Model requestModel = new Model.Builder().getDefault().build();
    private Model responseModel = new Model.Builder().getDefault().build();

    private String recipeId = "";
    private boolean isCloned;

    public UseCaseRecipeIdentity(RepositoryRecipeIdentity repository, TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    @Override
    protected void execute(Request request) {
        requestModel = request.getModel();

        if (isNewRequest(request)) {
            loadData(request);
            return;
        }
        if (isModelChanged()) {
            sendResponse();
        }
    }

    private boolean isNewRequest(Request request) {
        return !recipeId.equals(request.getRecipeId()) || requestModel.equals(
                new Model.Builder().getDefault().build());
    }

    private void loadData(Request request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getById(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(Request request) {
        return !request.getCloneToRecipeId().isEmpty();
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity entity) {
        if (isCloned) {
            requestModel = cloneModelFromEntity(entity);
            save(requestModel);
            isCloned = false;
        } else {
            requestModel = convertEntityToModel(entity);
        }
        responseModel = requestModel;
        sendResponse();
    }

    private Model cloneModelFromEntity(RecipeIdentityEntity entityToClone) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new Model.Builder().
                setId(recipeId).
                setTitle(entityToClone.getTitle()).
                setDescription(entityToClone.getDescription()).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private Model convertEntityToModel(RecipeIdentityEntity entity) {
        return new Model.Builder().
                setId(entity.getId()).
                setTitle(entity.getTitle()).
                setDescription(entity.getDescription()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        responseModel = createNewModel();

        Response response = new Response.Builder().
                setModel(responseModel).
                setRecipeId(recipeId).
                setResult(Result.DATA_UNAVAILABLE).build();

        getUseCaseCallback().onError(response);
    }

    private Model createNewModel() {
        long time = timeProvider.getCurrentTimeInMills();
        return new Model.Builder().getDefault().
                setId(recipeId).
                setCreateDate(time).
                setLastUpdate(time).
                build();
    }

    private void sendResponse() {
        Response.Builder builder = new Response.Builder().setRecipeId(recipeId);

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
        Response response = builder.setModel(responseModel).build();

        getUseCaseCallback().onSuccess(response);
    }

    private boolean isModelChanged() {
        return !requestModel.equals(responseModel);
    }

    private boolean isValid() {
        if (requestModel.equals(new Model.Builder().getDefault().build())) {
            return false;
        } else {
            return !requestModel.getTitle().isEmpty();
        }
    }

    private void save(Model model) {
        repository.save(convertModelToEntity(model));
    }

    // todo - move model / entity conversions to the data layer
    private RecipeIdentityEntity convertModelToEntity(Model model) {
        return new RecipeIdentityEntity(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }

    public static final class Request implements UseCaseInteractor.Request {

        @Nonnull
        private final String recipeId;
        @Nonnull
        private final String cloneToRecipeId;
        @Nullable
        private final Model model;

        public Request(@Nonnull String recipeId,
                       @Nonnull String cloneToRecipeId,
                       @Nullable Model model) {
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

        @Nullable
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
                    Objects.equals(model, request.model);
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
                        setModel(new Model.Builder().getDefault().build());
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
        private final String recipeId;
        @Nonnull
        private final Model model;
        private final Result result;

        public Response(@Nonnull String recipeId,
                        @Nonnull Model model,
                        @Nonnull Result result) {
            this.recipeId = recipeId;
            this.model = model;
            this.result = result;
        }

        @Nonnull
        public String getRecipeId() {
            return recipeId;
        }

        @Nonnull
        public Model getModel() {
            return model;
        }

        public Result getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "recipeId='" + recipeId + '\'' +
                    ", model=" + model +
                    ", response=" + result +
                    '}';
        }

        public static class Builder {
            private String recipeId;
            private Model model;
            private Result result;

            public static Builder basedOn(@Nonnull Response oldResponse) {
                return new Builder().
                        setRecipeId(oldResponse.getRecipeId()).
                        setModel(oldResponse.getModel()).
                        setResult(oldResponse.getResult());
            }

            public Builder getDefault() {
                return new Builder().
                        setRecipeId("").
                        setModel(new Model.Builder().
                                getDefault().
                                build());
            }

            public Builder setRecipeId(String recipeId) {
                this.recipeId = recipeId;
                return this;
            }

            public Builder setModel(Model model) {
                this.model = model;
                return this;
            }

            public Builder setResult(Result result) {
                this.result = result;
                return this;
            }

            public Response build() {
                return new Response(
                        recipeId,
                        model,
                        result
                );
            }
        }
    }

    public static class Model {

        @Nonnull
        private final String id;
        @Nonnull
        private final String title;
        @Nullable
        private final String description;
        private final long createDate;
        private final long lastUpdate;

        public Model(@Nonnull String id,
                     @Nonnull String title,
                     @Nullable String description,
                     long createDate,
                     long lastUpdate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.createDate = createDate;
            this.lastUpdate = lastUpdate;
        }

        @Nonnull
        public String getId() {
            return id;
        }

        @Nonnull
        public String getTitle() {
            return title;
        }

        @Nullable
        public String getDescription() {
            return description;
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
            Model that = (Model) o;
            return createDate == that.createDate &&
                    lastUpdate == that.lastUpdate &&
                    id.equals(that.id) &&
                    title.equals(that.title) &&
                    Objects.equals(description, that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, title, description, createDate, lastUpdate);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }

        public static class Builder {
            private String id;
            private String title;
            private String description;
            private long createDate;
            private long lastUpdate;

            public static Builder basedOn(@Nonnull Model oldModel) {
                return new Builder().
                        setId(oldModel.getId()).
                        setTitle(oldModel.getTitle()).
                        setDescription(oldModel.getDescription()).
                        setCreateDate(oldModel.getCreateDate()).
                        setLastUpdate(oldModel.getLastUpdate());
            }

            public Builder getDefault() {
                return new Builder().
                        setId("").
                        setTitle("").
                        setDescription("").
                        setCreateDate(0).
                        setLastUpdate(0);
            }

            public Builder setId(String id) {
                this.id = id;
                return this;
            }

            public Builder setTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder setDescription(String description) {
                this.description = description;
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
                        title,
                        description,
                        createDate,
                        lastUpdate
                );
            }
        }
    }
}
