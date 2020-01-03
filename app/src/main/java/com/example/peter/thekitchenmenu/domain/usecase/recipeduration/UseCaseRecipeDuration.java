package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class UseCaseRecipeDuration
        extends UseCaseInteractor<UseCaseRecipeDuration.Request, UseCaseRecipeDuration.Response>
        implements DataSource.GetEntityCallback<RecipeDurationEntity> {

    private static final String TAG = "tkm-" + UseCaseRecipeDuration.class.getSimpleName() + ":";

    public enum FailReason {
        INVALID_PREP_TIME,
        INVALID_COOK_TIME,
        NONE
    }

    public static final String DO_NOT_CLONE = "";

    private final TimeProvider timeProvider;
    private final RepositoryRecipeDuration repository;
    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    private String recipeId = "";
    private Model requestModel = Model.Builder.getDefault().build();
    private Model responseModel = Model.Builder.getDefault().build();
    private boolean isCloned;

    public UseCaseRecipeDuration(RepositoryRecipeDuration repository,
                                 TimeProvider timeProvider,
                                 int maxPrepTime,
                                 int maxCookTime) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;
    }

    @Override
    protected void execute(Request request) {
        requestModel = request.getModel();
        System.out.println(TAG + " requestModelBeforeProcessing:" + requestModel);
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            calculateValues();
        }
    }

    private boolean isNewRequest(Request request) {
        return !recipeId.equals(request.getRecipeId());
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
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    @Override
    public void onEntityLoaded(RecipeDurationEntity entity) {
        requestModel = convertEntityToModel(entity);

        if (isCloned) {
            save(requestModel);
            isCloned = false;
        }

        equaliseState();
        buildResponse();
    }

    private Model convertEntityToModel(RecipeDurationEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new Model.Builder().
                setId(isCloned ? recipeId : entity.getId()).
                setPrepHours(getHours(entity.getPrepTime())).
                setPrepMinutes(getMinutes(entity.getPrepTime())).
                setTotalPrepTime(entity.getPrepTime()).
                setCookHours(getHours(entity.getCookTime())).
                setCookMinutes(getMinutes(entity.getCookTime())).
                setTotalCookTime(entity.getCookTime()).
                setTotalTime(entity.getPrepTime() + entity.getCookTime()).
                setCreateDate(isCloned ? currentTime : entity.getCreateDate()).
                setLastUpdate(isCloned ? currentTime : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        requestModel = createNewModel();
        equaliseState();
        save(responseModel);
        buildResponse();
    }

    private Model createNewModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return Model.Builder.
                getDefault().
                setId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void calculateValues() {
        int totalPrepTime = calculateTotalMinutes(requestModel.getPrepHours(),
                requestModel.getPrepMinutes());

        int totalCookTime = calculateTotalMinutes(requestModel.getCookHours(),
                requestModel.getCookMinutes());

        requestModel = Model.Builder.
                basedOn(requestModel).
                setPrepHours(getHours(totalPrepTime)).
                setPrepMinutes(getMinutes(totalPrepTime)).
                setTotalPrepTime(totalPrepTime).
                setCookHours(getHours(totalCookTime)).
                setCookMinutes(getMinutes(totalCookTime)).
                setTotalCookTime(totalCookTime).
                build();

        buildResponse();
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    private int calculateTotalMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void buildResponse() {
        Response.Builder builder = new Response.Builder()
                .setFailReasons(getFailReasons())
                .setResult(getResult());

        equaliseState();

        Response response = builder.
                setModel(responseModel).
                build();

        sendResponse(response);
    }

    private void equaliseState() {
        responseModel = requestModel;
    }

    private void sendResponse(Response response) {
        if (response.getResult() == Result.VALID_CHANGED ||
                response.getResult() == Result.VALID_UNCHANGED) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private List<FailReason> getFailReasons() {
        List<FailReason> failReasons = new LinkedList<>();

        if (requestModel.getTotalPrepTime() > MAX_PREP_TIME) {
            failReasons.add(FailReason.INVALID_PREP_TIME);
        }
        if (requestModel.getTotalCookTime() > MAX_COOK_TIME) {
            failReasons.add(FailReason.INVALID_COOK_TIME);
        }
        if (isValid()) {
            failReasons.add(FailReason.NONE);
        }
        return failReasons;
    }

    private Result getResult() {
        if (!isValid() && !isChanged()) {
            return Result.INVALID_UNCHANGED;

        } else if (isValid() && !isChanged()) {
            return Result.VALID_UNCHANGED;

        } else if (!isValid() && isChanged()) {
            return Result.INVALID_CHANGED;

        } else {
            save(requestModel);
            return Result.VALID_CHANGED;
        }
    }

    private boolean isValid() {
        return requestModel.getTotalPrepTime() <= MAX_PREP_TIME &&
                requestModel.getTotalCookTime() <= MAX_COOK_TIME;
    }

    private boolean isChanged() {
        return !requestModel.equals(responseModel);
    }

    private void save(Model model) {
        repository.save(convertModelToEntity(model));
    }

    private RecipeDurationEntity convertModelToEntity(Model model) {
        return new RecipeDurationEntity(
                model.getId(),
                model.getTotalPrepTime(),
                model.getTotalCookTime(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }

    public static final class Model implements PersistenceModel {
        @Nonnull
        private final String id;
        private final int prepHours;
        private final int prepMinutes;
        private final int totalPrepTime;
        private final int cookHours;
        private final int cookMinutes;
        private final int totalCookTime;
        private final int totalTime;
        private final long createDate;
        private final long lastUpdate;

        private Model(@Nonnull String id,
                      int prepHours,
                      int prepMinutes,
                      int totalPrepTime,
                      int cookHours,
                      int cookMinutes,
                      int totalCookTime,
                      int totalTime,
                      long createDate,
                      long lastUpdate) {
            this.id = id;
            this.prepHours = prepHours;
            this.prepMinutes = prepMinutes;
            this.totalPrepTime = totalPrepTime;
            this.cookHours = cookHours;
            this.cookMinutes = cookMinutes;
            this.totalCookTime = totalCookTime;
            this.totalTime = totalTime;
            this.createDate = createDate;
            this.lastUpdate = lastUpdate;
        }

        @Override
        @Nonnull
        public String getId() {
            return id;
        }

        public int getPrepHours() {
            return prepHours;
        }

        public int getPrepMinutes() {
            return prepMinutes;
        }

        public int getTotalPrepTime() {
            return totalPrepTime;
        }

        public int getCookHours() {
            return cookHours;
        }

        public int getCookMinutes() {
            return cookMinutes;
        }

        public int getTotalCookTime() {
            return totalCookTime;
        }

        public int getTotalTime() {
            return totalTime;
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
            return prepHours == model.prepHours &&
                    prepMinutes == model.prepMinutes &&
                    totalPrepTime == model.totalPrepTime &&
                    cookHours == model.cookHours &&
                    cookMinutes == model.cookMinutes &&
                    totalCookTime == model.totalCookTime &&
                    totalTime == model.totalTime &&
                    createDate == model.createDate &&
                    lastUpdate == model.lastUpdate &&
                    id.equals(model.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, prepHours, prepMinutes, totalPrepTime, cookHours, cookMinutes,
                    totalCookTime, totalTime, createDate, lastUpdate);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "id='" + id + '\'' +
                    ", prepHours=" + prepHours +
                    ", prepMinutes=" + prepMinutes +
                    ", totalPrepTime=" + totalPrepTime +
                    ", cookHours=" + cookHours +
                    ", cookMinutes=" + cookMinutes +
                    ", totalCookTime=" + totalCookTime +
                    ", totalTime=" + totalTime +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }

        public static class Builder {
            private String id;
            private int prepHours;
            private int prepMinutes;
            private int totalPrepTime;
            private int cookHours;
            private int cookMinutes;
            private int totalCookTime;
            private int totalTime;
            private long createDate;
            private long lastUpdate;

            public static Builder basedOn(@Nonnull Model model) {
                return new Builder().
                        setId(model.getId()).
                        setPrepHours(model.getPrepHours()).
                        setPrepMinutes(model.getPrepMinutes()).
                        setTotalPrepTime(model.getTotalPrepTime()).
                        setCookHours(model.getCookHours()).
                        setCookMinutes(model.getCookMinutes()).
                        setTotalCookTime(model.getTotalCookTime()).
                        setTotalTime(model.getTotalTime()).
                        setCreateDate(model.getCreateDate()).
                        setLastUpdate(model.getLastUpdate());
            }

            public static Builder getDefault() {
                return new Builder().
                        setId("").
                        setPrepHours(0).
                        setPrepMinutes(0).
                        setTotalPrepTime(0).
                        setCookHours(0).
                        setCookMinutes(0).
                        setTotalCookTime(0).
                        setTotalTime(0).
                        setCreateDate(0L).
                        setLastUpdate(0L);
            }

            public Builder setId(String id) {
                this.id = id;
                return this;
            }

            public Builder setPrepHours(int prepHours) {
                this.prepHours = prepHours;
                return this;
            }

            public Builder setPrepMinutes(int prepMinutes) {
                this.prepMinutes = prepMinutes;
                return this;
            }

            public Builder setTotalPrepTime(int totalPrepTime) {
                this.totalPrepTime = totalPrepTime;
                return this;
            }

            public Builder setCookHours(int cookHours) {
                this.cookHours = cookHours;
                return this;
            }

            public Builder setCookMinutes(int cookMinutes) {
                this.cookMinutes = cookMinutes;
                return this;
            }

            public Builder setTotalCookTime(int totalCookTime) {
                this.totalCookTime = totalCookTime;
                return this;
            }

            public Builder setTotalTime(int totalTime) {
                this.totalTime = totalTime;
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
                        prepHours,
                        prepMinutes,
                        totalPrepTime,
                        cookHours,
                        cookMinutes,
                        totalCookTime,
                        totalTime,
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

        public Request(@Nonnull String recipeId,
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

        public static class Builder {
            private String recipeId;
            private String cloneToRecipeId;
            private Model model;

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
        private final List<FailReason> failReasons;
        @Nonnull
        private final Model model;

        public Response(@Nonnull Result result,
                        @Nonnull List<FailReason> failReasons,
                        @Nonnull Model model) {
            this.result = result;
            this.failReasons = failReasons;
            this.model = model;
        }

        @Nonnull
        public Result getResult() {
            return result;
        }

        @Nonnull
        public List<FailReason> getFailReasons() {
            return failReasons;
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
                    failReasons.equals(response.failReasons) &&
                    model.equals(response.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(result, failReasons, model);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result=" + result +
                    ", failReasons=" + failReasons +
                    ", model=" + model +
                    '}';
        }

        public static class Builder {
            private Result result;
            private List<FailReason> failReasons;
            private Model model;

            public static Builder getDefault() {
                return new Builder().
                        setResult(Result.INVALID_UNCHANGED).
                        setFailReasons(getDefaultFailReasons()).
                        setModel(Model.Builder.
                                getDefault().
                                build());
            }

            public Builder setResult(Result result) {
                this.result = result;
                return this;
            }

            public Builder setFailReasons(List<FailReason> failReasons) {
                this.failReasons = failReasons;
                return this;
            }

            public Builder setModel(Model model) {
                this.model = model;
                return this;
            }

            public Response build() {
                return new Response(
                        result,
                        failReasons,
                        model
                );
            }

            private static List<FailReason> getDefaultFailReasons() {
                List<FailReason> defaultFailReasons = new LinkedList<>();
                defaultFailReasons.add(FailReason.NONE);
                return defaultFailReasons;
            }
        }
    }
}
