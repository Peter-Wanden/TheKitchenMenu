package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public class RecipeDuration
        extends UseCase
        implements GetDomainModelCallback<RecipeDurationPersistenceModel> {

    private static final String TAG = "tkm-" + RecipeDuration.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        INVALID_PREP_TIME(250),
        INVALID_COOK_TIME(251);

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

    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    @Nonnull
    private final RepositoryRecipeDuration repository;
    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String dataId = "";
    private String recipeId = "";
    private boolean isNewRequest;

    private RecipeDurationRequest.Model requestModel;
    private RecipeDurationPersistenceModel persistenceModel;

    private int accessCount;

    public RecipeDuration(@Nonnull RepositoryRecipeDuration repository,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull UniqueIdProvider idProvider,
                          int maxPrepTime,
                          int maxCookTime) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;

        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;

        requestModel = new RecipeDurationRequest.Model.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        accessCount++;
        RecipeDurationRequest r = (RecipeDurationRequest) request;
        System.out.println(TAG + "Request No:" + accessCount + " - " + r);

        if (r.getDomainId() == null || r.getDomainId().isEmpty()) {
            throw new IllegalArgumentException("domain id cannot be empty or null!");
        }

        requestModel = r.getModel();

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            setupUseCase();
            processRequest();
        }
    }

    private boolean isNewRequest(RecipeDurationRequest r) {
        return isNewRequest = !r.getDomainId().equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getActiveByDomainId(recipeId, this);
    }

    @Override
    public void onModelLoaded(RecipeDurationPersistenceModel persistenceModel) {
        this.persistenceModel = persistenceModel;

        if (!dataId.equals(persistenceModel.getDataId())) {
            dataId = persistenceModel.getDataId();
        }

        validateData();
        buildResponse();
    }

    @Override
    public void onModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        buildResponse();
    }

    private RecipeDurationPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        dataId = idProvider.getUId();
        return new RecipeDurationPersistenceModel.Builder().
                getDefault().
                setDataId(dataId).
                setDomainId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void setupUseCase() {
        failReasons.clear();
        isNewRequest = false;
    }

    private void processRequest() {
        validateData();
        buildResponse();
    }

    private void validateData() {
        int prepTime;
        int cookTime;
        if (isNewRequest) {
            prepTime = persistenceModel.getPrepTime();
            cookTime = persistenceModel.getCookTime();
        } else {
            prepTime = getRequestPrepTime();
            cookTime = getRequestCookTime();
        }
        if (prepTime > MAX_PREP_TIME) {
            failReasons.add(FailReason.INVALID_PREP_TIME);
        }
        if (cookTime > MAX_COOK_TIME) {
            failReasons.add(FailReason.INVALID_COOK_TIME);
        }
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    private int getTotalMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void buildResponse() {
        RecipeDurationResponse response = new RecipeDurationResponse.Builder().
                setDataId(dataId).
                setDomainId(recipeId).
                setMetadata(getMetadata()).
                setModel(getResponseModel()).
                build();

        if (response.getMetadata().getState() == ComponentState.VALID_CHANGED) {
            persistenceModel = updatePersistenceFromRequestModel();
            save();
        }
        sendResponse(response);
    }

    private UseCaseMetadata getMetadata() {
        return new UseCaseMetadata.Builder().
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(0L).
                setLasUpdate(0L).
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
        return !isNewRequest && (isPrepTimeChanged() || isCookTimeChanged());
    }

    private boolean isPrepTimeChanged() {
        return getRequestPrepTime() != persistenceModel.getPrepTime();
    }

    private boolean isCookTimeChanged() {
        return getRequestCookTime() != persistenceModel.getCookTime();
    }

    private int getRequestPrepTime() {
        return getTotalMinutes(requestModel.getPrepHours(), requestModel.getPrepMinutes());
    }

    private int getRequestCookTime() {
        return getTotalMinutes(requestModel.getCookHours(), requestModel.getCookMinutes());
    }

    // todo, needs a new dataId
    private RecipeDurationPersistenceModel updatePersistenceFromRequestModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeDurationPersistenceModel.Builder().
                basedOnPersistenceModel(persistenceModel).

                setDataId(idProvider.getUId()).

                setPrepTime(
                        getTotalMinutes(
                                requestModel.getPrepHours(),
                                requestModel.getPrepMinutes())).

                setCookTime(
                        getTotalMinutes(
                                requestModel.getCookHours(),
                                requestModel.getCookMinutes())).

                setCreateDate(currentTime).
                setLastUpdate(currentTime).

                build();
    }

    private RecipeDurationResponse.Model getResponseModel() {
        return new RecipeDurationResponse.Model.Builder().
                setPrepHours(isNewRequest ?
                        getHours(persistenceModel.getPrepTime()) :
                        requestModel.getPrepHours()).

                setPrepMinutes(isNewRequest ?
                        getMinutes(persistenceModel.getPrepTime()) :
                        requestModel.getPrepMinutes()).

                setTotalPrepTime(isNewRequest ?
                        persistenceModel.getPrepTime() :
                        getTotalMinutes(
                                requestModel.getPrepHours(),
                                requestModel.getPrepMinutes())).

                setCookHours(isNewRequest ?
                        getHours(persistenceModel.getCookTime()) :
                        requestModel.getCookHours()).

                setCookMinutes(isNewRequest ?
                        getMinutes(persistenceModel.getCookTime()) :
                        requestModel.getCookMinutes()).

                setTotalCookTime(isNewRequest ?
                        persistenceModel.getCookTime() :
                        getTotalMinutes(
                                requestModel.getCookHours(),
                                requestModel.getCookMinutes())).

                setTotalTime(isNewRequest ?
                        persistenceModel.getPrepTime() + persistenceModel.getCookTime() :
                        getTotalMinutes(
                                requestModel.getPrepHours(),
                                requestModel.getPrepMinutes()) +
                                getTotalMinutes(
                                        requestModel.getCookHours(),
                                        requestModel.getCookMinutes())).

                setCreateDate(persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel.getLastUpdate()).
                build();
    }

    private void sendResponse(RecipeDurationResponse r) {
        System.out.println(TAG + "Response No:" + accessCount + " - " + r);

        if (r.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(r);
        } else {
            getUseCaseCallback().onError(r);
        }
    }

    private void save() {
        repository.save(persistenceModel);
    }
}
