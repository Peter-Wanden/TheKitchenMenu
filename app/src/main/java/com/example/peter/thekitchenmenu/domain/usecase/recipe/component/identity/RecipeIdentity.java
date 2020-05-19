package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.TextType;

public class RecipeIdentity
        extends UseCase
        implements DomainDataAccess.GetDomainModelCallback<RecipeIdentityPersistenceModel> {

    private static final String TAG = "tkm-" + RecipeIdentity.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        TITLE_TOO_SHORT(300),
        TITLE_TOO_LONG(301),
        DESCRIPTION_TOO_SHORT(302),
        DESCRIPTION_TOO_LONG(303);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason s : FailReason.values())
                options.put(s.id, s);
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }

    private static final TextType TITLE_TEXT_TYPE = TextType.SHORT_TEXT;
    private static final TextType DESCRIPTION_TEXT_TYPE = TextType.LONG_TEXT;

    @Nonnull
    private final RepositoryRecipeIdentity repository;
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private final TextValidator textValidator;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String dataId = "";
    private String recipeId = "";
    private boolean isNewRequest;

    private RecipeIdentityRequest.Model requestModel;
    private RecipeIdentityPersistenceModel persistenceModel;

    private int accessCount; // For testing

    public RecipeIdentity(@Nonnull RepositoryRecipeIdentity repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull UseCaseHandler handler,
                          @Nonnull TextValidator textValidator) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.handler = handler;
        this.textValidator = textValidator;

        requestModel = new RecipeIdentityRequest.Model.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        accessCount++;
        RecipeIdentityRequest r = (RecipeIdentityRequest) request;
        requestModel = r.getModel();
        System.out.println(TAG + "Request No:" + accessCount + " - " + r);

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            setupComponent();
            processChanges();
        }
    }

    private boolean isNewRequest(RecipeIdentityRequest r) {
        return isNewRequest = !r.getDomainId().equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getActiveByDomainId(recipeId, this);
    }

    @Override
    public void onModelLoaded(RecipeIdentityPersistenceModel model) {
        persistenceModel = model;
        dataId = model.getDataId();
        processChanges();
    }

    @Override
    public void onModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);

        buildResponse();
    }

    private RecipeIdentityPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        dataId = idProvider.getUId();

        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId(dataId).
                setDomainId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void setupComponent() {
        failReasons.clear();
        isNewRequest = false;
    }

    private void processChanges() {
        validateData();
        buildResponse();
    }

    private void validateData() {
        validateTitle();
    }

    private void validateTitle() {
        TextValidatorRequest request = new TextValidatorRequest(
                TITLE_TEXT_TYPE,
                new TextValidatorModel(getTitle())
        );
        handler.execute(
                textValidator,
                request,
                new UseCase.Callback<TextValidatorResponse>() {
                    @Override
                    public void onUseCaseSuccess(TextValidatorResponse response) {
                        validateDescription();
                    }

                    @Override
                    public void onUseCaseError(TextValidatorResponse response) {
                        addTitleFailReasonFromTextValidator(response.getFailReason());
                        validateDescription();
                    }
                }
        );
    }

    private String getTitle() {
        return isNewRequest ? persistenceModel.getTitle() : requestModel.getTitle();
    }

    private void addTitleFailReasonFromTextValidator(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(FailReason.TITLE_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(FailReason.TITLE_TOO_LONG);
        }
    }

    private void validateDescription() {
        TextValidatorRequest request = new TextValidatorRequest(
                DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(getDescription())
        );
        handler.execute(
                textValidator,
                request,
                new UseCase.Callback<TextValidatorResponse>() {
                    @Override
                    public void onUseCaseSuccess(TextValidatorResponse response) {
                        if (failReasons.isEmpty()) {
                            failReasons.add(CommonFailReason.NONE);
                        }
                    }

                    @Override
                    public void onUseCaseError(TextValidatorResponse response) {
                        addDescriptionFailReasons(response.getFailReason());
                    }
                }
        );
    }

    private String getDescription() {
        return isNewRequest ? persistenceModel.getDescription() : requestModel.getDescription();
    }

    private void addDescriptionFailReasons(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
        }
    }

    private void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder();
        builder.setDomainId(recipeId);

        if (ComponentState.VALID_CHANGED == getComponentState()) {
            RecipeIdentityPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }

        builder.setModel(getResponseModel());
        builder.setDataId(dataId);

        sendResponse(builder.build());
    }

    private UseCaseMetadata getMetadata(RecipeIdentityPersistenceModel m) {
        return new UseCaseMetadata.Builder().
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(m.getCreateDate()).
                setLasUpdate(m.getLastUpdate()).
                build();
    }

    private ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);

        return isValid
                ?
                (isChanged() ? ComponentState.VALID_CHANGED : ComponentState.VALID_UNCHANGED)
                :
                (isChanged() ? ComponentState.INVALID_CHANGED : ComponentState.INVALID_UNCHANGED);
    }

    private boolean isChanged() {
        return !isNewRequest && (isTitleChanged() || isDescriptionChanged());
    }

    private boolean isTitleChanged() {
        return !persistenceModel.getTitle().toLowerCase().
                equals(requestModel.getTitle().toLowerCase().trim());
    }

    private boolean isDescriptionChanged() {
        return !persistenceModel.getDescription().toLowerCase().trim().
                equals(requestModel.getDescription().toLowerCase().trim());
    }

    private RecipeIdentityPersistenceModel updatePersistenceModel() {
        return new RecipeIdentityPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setDataId(idProvider.getUId()).
                setTitle(requestModel.getTitle()).
                setDescription(requestModel.getDescription()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private RecipeIdentityResponse.Model getResponseModel() {
        return new RecipeIdentityResponse.Model.Builder().
                setTitle(isNewRequest ?
                        persistenceModel.getTitle() :
                        requestModel.getTitle()).

                setDescription(isNewRequest ?
                        persistenceModel.getDescription() :
                        requestModel.getDescription()).
                build();
    }

    private void sendResponse(RecipeIdentityResponse r) {
        System.out.println(TAG + "Response No:" + accessCount + " - " + r);

        if (r.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onUseCaseSuccess(r);
        } else {
            getUseCaseCallback().onUseCaseError(r);
        }
    }

    private void save() {
        repository.save(persistenceModel);
    }
}
