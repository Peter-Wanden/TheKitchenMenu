package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public class RecipeDuration
        extends UseCaseBase
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
    private String recipeDomainId = "";
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
        requestModel = r.getModel();
        System.out.println(TAG + "Request No:" + accessCount + " - " + r);

        if (requestIsEmpty()) {
            if (isUseCaseEmpty()) {
                isNewRequest = true;
                sendEmptyResponse();
            } else {
                isNewRequest = false;
                respondWithCurrentData();
            }
        } else if (isUseCaseEmpty()) {
            isNewRequest = true;
            extractIds();
            loadData(recipeDomainId);

        } else if (isRequestToUpdateData()) {
            System.out.println(TAG + "isRequestToModifyData");
            isNewRequest = false;
            setupUseCase();
            processDomainModelChanges();

        } else {
            isNewRequest = true;
            loadData(r.getDomainId());
        }
    }

    private boolean requestIsEmpty() {
        return ((UseCaseMessageModelDataId) getRequest()).getDomainId().equals("");
    }

    private boolean isUseCaseEmpty() {
        return recipeDomainId.equals("");
    }

    private void sendEmptyResponse() {
        RecipeDurationResponse response = new RecipeDurationResponse.Builder().getDefault().build();
        getUseCaseCallback().onUseCaseError(response);
    }

    private void respondWithCurrentData() {
        buildResponse();
    }

    private void extractIds() {
        UseCaseMessageModelDataId r = (UseCaseMessageModelDataId) getRequest();
        dataId = r.getDataId();
        recipeDomainId = r.getDomainId();
        System.out.println(TAG + "extractIds: dataId=" + dataId + " domainId=" + recipeDomainId);
    }

    private boolean isRequestToUpdateData() {
        return ((UseCaseMessageModelDataId) getRequest()).getDomainId().equals(recipeDomainId);
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

    private void processDomainModelChanges() {
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
                setDomainId(recipeDomainId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void setupUseCase() {
        failReasons.clear();
        isNewRequest = false;
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
//        RecipeDurationResponse response = new RecipeDurationResponse.Builder().
//                setDataId(dataId).
//                setDomainId(recipeDomainId).
//                setMetadata(getMetadata()).
//                setDomainModel(getResponseModel()).
//                build();
//
//        if (response.getMetadata().getComponentState() == ComponentState.VALID_CHANGED) {
//            persistenceModel = updatePersistenceFromRequestModel();
//            save();
//        }
//        sendResponse(response);


        RecipeDurationResponse.Builder builder = new RecipeDurationResponse.Builder();
        builder.setDomainId(recipeDomainId);

        if (ComponentState.VALID_CHANGED == getComponentState()) {
            RecipeDurationPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }

        builder.setDomainModel(getResponseModel());
        builder.setDomainId(dataId);

        sendResponse(builder.build());
    }

    private UseCaseMetadataModel getMetadata(RecipeDurationPersistenceModel m) {
        return new UseCaseMetadataModel.Builder().
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

    private RecipeDurationPersistenceModel updatePersistenceModel() {
        return new RecipeDurationPersistenceModel.Builder().
                basedOnPersistenceModel(persistenceModel).
                setDataId(idProvider.getUId()).
                setPrepTime(getTotalMinutes(
                        requestModel.getPrepHours(),
                        requestModel.getPrepMinutes())).
                setCookTime(getTotalMinutes(
                        requestModel.getCookHours(),
                        requestModel.getCookMinutes())).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
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
            getUseCaseCallback().onUseCaseSuccess(r);
        } else {
            getUseCaseCallback().onUseCaseError(r);
        }
    }

    private void save() {
        repository.save(persistenceModel);
    }
}
