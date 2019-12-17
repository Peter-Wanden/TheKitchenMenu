package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientDuplicateChecker.NO_DUPLICATE_FOUND;

public class UseCaseIngredient
        extends
        UseCaseInteractor<UseCaseIngredient.Request, UseCaseIngredient.Response>
        implements
        DataSource.GetEntityCallback<IngredientEntity> {

    public enum Result {
        DATA_UNAVAILABLE,
        UNEDITABLE,
        IS_DUPLICATE,
        UNCHANGED_INVALID,
        UNCHANGED_VALID,
        CHANGED_INVALID,
        CHANGED_VALID
    }

    private static final String TAG = "tkm-" + UseCaseIngredient.class.getSimpleName() + ":";

    public static final String CREATE_NEW_INGREDIENT = "";

    private RepositoryIngredient repository;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;
    private IngredientDuplicateChecker duplicateChecker;

    private boolean isDuplicate;
    private Model requestModel = new Model.Builder().getDefault().build();
    private Model responseModel = new Model.Builder().getDefault().build();

    public UseCaseIngredient(RepositoryIngredient repository,
                             UniqueIdProvider idProvider,
                             TimeProvider timeProvider,
                             IngredientDuplicateChecker duplicateChecker) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.duplicateChecker = duplicateChecker;
    }

    @Override
    protected void execute(Request request) {
        System.out.println(TAG + request);
        requestModel = request.getModel();
        String ingredientId = request.getModel().getIngredientId();

        if (isCreateNew(ingredientId)) {
            System.out.println(TAG + "isNew");
            requestModel = createNewIngredientModel();
            sendResponse();
        } else if (isEditExisting(ingredientId)) {
            System.out.println(TAG + "isEdit");
            loadData(ingredientId);
        } else {
            System.out.println(TAG + "isAlreadyLoaded");
            checkForDuplicates();
        }
    }

    private boolean isCreateNew(String ingredientId) {
        return ingredientId.equals(CREATE_NEW_INGREDIENT);
    }

    private boolean isEditExisting(String ingredientId) {
        Model model = new Model.Builder().getDefault().setIngredientId(ingredientId).build();
        return this.requestModel.equals(model);
    }

    private void loadData(String ingredientId) {
        repository.getById(ingredientId, this);
    }

    @Override
    public void onEntityLoaded(IngredientEntity entity) {
        requestModel = convertEntityToModel(entity);
        responseModel = requestModel;
        sendResponse();
    }

    private Model convertEntityToModel(IngredientEntity entity) {
        return new Model(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getConversionFactor(),
                entity.getCreatedBy(),
                entity.getCreateDate(),
                entity.getLastUpdate()
        );
    }

    @Override
    public void onDataNotAvailable() {
        responseModel = createNewIngredientModel();
        requestModel = responseModel;
        Response response = new Response(
                Result.DATA_UNAVAILABLE,
                responseModel
        );
        getUseCaseCallback().onError(response);
    }

    private Model createNewIngredientModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        String id = idProvider.getUId();

        return new Model.Builder().
                getDefault().
                setIngredientId(id).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void checkForDuplicates() {
        System.out.println(TAG + "responseModel=" + responseModel);
        System.out.println(TAG + "requestModel =" + requestModel);
        System.out.println(TAG + "descChanged=" + isDescriptionChanged());
        System.out.println(TAG + "nameChanged=" + isNameChanged());
        if (isNameChanged()) {
            duplicateChecker.checkForDuplicatesAndNotify(
                    requestModel.getName(),
                    requestModel.getIngredientId(),

                    duplicateId -> {
                        isDuplicate = !duplicateId.equals(NO_DUPLICATE_FOUND);

                        if (!isDuplicate) {
                            requestModel = getUpdatedModel();
                            System.out.println(TAG + "NameIsChanged=" + requestModel);
                            save();
                        }
                        sendResponse();
                    }
            );
        } else if (isDescriptionChanged()) {
            requestModel = getUpdatedModel();
            System.out.println(TAG + "descriptionIsChanged=" + requestModel);
            save();
            sendResponse();
        }
    }

    private Model getUpdatedModel() {
        Model.Builder builder = Model.Builder.basedOn(requestModel);

        if (isChanged()) {
            builder.setLastUpdate(timeProvider.getCurrentTimeInMills());
        }
        return builder.build();
    }

    private void save() {
        repository.save(convertModelToEntity(requestModel));
    }

    private IngredientEntity convertModelToEntity(Model model) {
        return new IngredientEntity(
                model.getIngredientId(),
                model.getName(),
                model.getDescription(),
                model.getConversionFactor(),
                Constants.getUserId().getValue(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }

    private void sendResponse() {
        Response response = new Response(
                getResult(),
                requestModel
        );
        equaliseModelState();
        System.out.println(TAG + "finalResponseModel=" + response);
        getUseCaseCallback().onSuccess(response);
    }

    private Result getResult() {
        if (!isEditable()) {
            return Result.UNEDITABLE;
        }
        if (isDuplicate) {
            return Result.IS_DUPLICATE;
        }
        if (isChanged()) {
            if (isNameValid()) {
                return Result.CHANGED_VALID;
            }
            return Result.CHANGED_INVALID;
        } else {
            if (isNameValid()) {
                return Result.UNCHANGED_VALID;
            } else {
                return Result.UNCHANGED_INVALID;
            }
        }
    }

    private void equaliseModelState() {
        requestModel = responseModel;
    }

    private boolean isNameChanged() {
        return !requestModel.getName().equals(responseModel.getName());
    }

    private boolean isNameValid() {
        return !requestModel.getName().isEmpty();
    }

    private boolean isDescriptionChanged() {
        return !requestModel.getDescription().equals(responseModel.getDescription());
    }

    private boolean isChanged() {
        return (isNameChanged() || isDescriptionChanged()) ||
                (isNameChanged() && isDescriptionChanged());
    }

    private boolean isEditable() {
        return Constants.getUserId().getValue().equals(requestModel.getUserId());
    }

    public static final class Model {
        @Nonnull
        private final String ingredientId;
        @Nonnull
        private final String name;
        @Nonnull
        private final String description;
        private final double conversionFactor;
        @Nonnull
        private final String userId;
        private final long createDate;
        private final long lastUpdate;

        private Model(@Nonnull String ingredientId,
                     @Nonnull String name,
                     @Nonnull String description,
                     double conversionFactor,
                     @Nonnull String userId,
                     long createDate,
                     long lastUpdate) {
            this.ingredientId = ingredientId;
            this.name = name;
            this.description = description;
            this.conversionFactor = conversionFactor;
            this.userId = userId;
            this.createDate = createDate;
            this.lastUpdate = lastUpdate;
        }

        @Nonnull
        public String getIngredientId() {
            return ingredientId;
        }

        @Nonnull
        public String getName() {
            return name;
        }

        @Nonnull
        public String getDescription() {
            return description;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }

        @Nonnull
        public String getUserId() {
            return userId;
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
            return Double.compare(model.conversionFactor, conversionFactor) == 0 &&
                    createDate == model.createDate &&
                    lastUpdate == model.lastUpdate &&
                    ingredientId.equals(model.ingredientId) &&
                    name.equals(model.name) &&
                    description.equals(model.description) &&
                    userId.equals(model.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ingredientId, name, description, conversionFactor, userId,
                    createDate, lastUpdate);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "ingredientId='" + ingredientId + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", conversionFactor=" + conversionFactor +
                    ", userId='" + userId + '\'' +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }

        public static class Builder {
            private String ingredientId;
            private String name;
            private String description;
            private double conversionFactor;
            private String userId;
            private long createDate;
            private long lastUpdate;

            public static Builder basedOn(Model model) {
                return new Builder().
                        setIngredientId(model.getIngredientId()).
                        setName(model.getName()).
                        setDescription(model.getDescription()).
                        setConversionFactor(model.getConversionFactor()).
                        setUserId(model.getUserId()).
                        setCreateDate(model.getCreateDate()).
                        setLastUpdate(model.getLastUpdate());
            }

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setIngredientId(String ingredientId) {
                this.ingredientId = ingredientId;
                return this;
            }

            public Builder setDescription(String description) {
                this.description = description;
                return this;
            }

            public Builder setConversionFactor(double conversionFactor) {
                this.conversionFactor = conversionFactor;
                return this;
            }

            public Builder setUserId(String userId) {
                this.userId = userId;
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

            public Builder getDefault() {
                ingredientId = "";
                name = "";
                description = "";
                conversionFactor = 0;
                userId = Constants.getUserId().getValue();
                createDate = 0L;
                lastUpdate = 0L;

                return this;
            }

            public Model build() {
                return new Model(
                        ingredientId,
                        name,
                        description,
                        conversionFactor,
                        userId,
                        createDate,
                        lastUpdate);
            }
        }

    }

    public static final class Request implements UseCaseInteractor.Request {
        @Nonnull
        private final Model model;

        public Request(@Nonnull Model model) {
            this.model = model;
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
            return model.equals(request.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(model);
        }

        @Override
        public String toString() {
            return "Request{" +
                    "model=" + model +
                    '}';
        }
    }

    public static final class Response implements UseCaseInteractor.Response {
        @Nonnull
        private final Result result;
        @Nonnull
        private final Model model;

        public Response(@Nonnull Result result, @Nonnull Model model) {
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
    }
}
