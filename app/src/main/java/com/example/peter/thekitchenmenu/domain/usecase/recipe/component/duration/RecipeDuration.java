package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public class RecipeDuration
        extends UseCaseElement<RecipeDurationRequest.DomainModel>
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

    private RecipeDurationPersistenceModel persistenceModel;

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

        requestDomainModel = new RecipeDurationRequest.DomainModel.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected void loadDataByDataId() {
        repository.getByDataId(useCaseDataId, this);
    }

    @Override
    protected void loadDataByDomainId() {
        repository.getActiveByDomainId(useCaseDomainId, this);
    }

    @Override
    public void dataSourceOnDomainModelLoaded(RecipeDurationPersistenceModel persistenceModel) {
        this.persistenceModel = persistenceModel;
        useCaseDataId = persistenceModel.getDataId();
        processUseCaseDomainData();
    }

    @Override
    protected void processUseCaseDomainData() {
        setupUseCase();
        validateDomainData();
        buildResponse();
    }

    @Override
    protected void processRequestDomainData() {

    }

    private void setupUseCase() {
        failReasons.clear();
    }

    @Override
    public void dataSourceOnDomainModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        buildResponse();
    }

    private RecipeDurationPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        useCaseDataId = idProvider.getUId();
        return new RecipeDurationPersistenceModel.Builder().
                getDefault().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void validateDomainData() {
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

    protected void buildResponse() {
        RecipeDurationResponse.Builder builder = new RecipeDurationResponse.Builder();
        builder.setDomainId(useCaseDomainId);

        if (ComponentState.VALID_CHANGED == getComponentState()) {
            RecipeDurationPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }

        builder.setDomainModel(getResponseDomainModel());
        builder.setDataId(useCaseDataId);

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

        return isValid ?
                (isDomainDataChanged() ?
                        ComponentState.VALID_CHANGED :
                        ComponentState.VALID_UNCHANGED)
                :
                (isDomainDataChanged() ?
                        ComponentState.INVALID_CHANGED :
                        ComponentState.INVALID_UNCHANGED);
    }

    @Override
    protected boolean isDomainDataChanged() {
        return !isNewRequest && (isPrepTimeChanged() || isCookTimeChanged());
    }

    private boolean isPrepTimeChanged() {
        return getRequestPrepTime() != persistenceModel.getPrepTime();
    }

    private boolean isCookTimeChanged() {
        return getRequestCookTime() != persistenceModel.getCookTime();
    }

    private int getRequestPrepTime() {
        return getTotalMinutes(requestDomainModel.getPrepHours(), requestDomainModel.getPrepMinutes());
    }

    private int getRequestCookTime() {
        return getTotalMinutes(requestDomainModel.getCookHours(), requestDomainModel.getCookMinutes());
    }

    private RecipeDurationPersistenceModel updatePersistenceModel() {
        return new RecipeDurationPersistenceModel.Builder().
                basedOnPersistenceModel(persistenceModel).
                setDataId(idProvider.getUId()).
                setPrepTime(getTotalMinutes(
                        requestDomainModel.getPrepHours(),
                        requestDomainModel.getPrepMinutes())).
                setCookTime(getTotalMinutes(
                        requestDomainModel.getCookHours(),
                        requestDomainModel.getCookMinutes())).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private RecipeDurationResponse.DomainModel getResponseDomainModel() {
        return isNewRequest ?
                getResponseModelFromPersistenceModel() :
                getResponseModelFromRequestDomainModel();
    }

    private RecipeDurationResponse.DomainModel getResponseModelFromRequestDomainModel() {
        return new RecipeDurationResponse.DomainModel.Builder().
                setPrepHours(requestDomainModel.getPrepHours()).
                setPrepMinutes(requestDomainModel.getPrepMinutes()).
                setTotalPrepTime(getTotalMinutes(
                        requestDomainModel.getPrepHours(),
                        requestDomainModel.getPrepMinutes())).
                setCookHours(requestDomainModel.getCookHours()).
                setCookMinutes(requestDomainModel.getCookMinutes()).
                setTotalCookTime(getTotalMinutes(
                        requestDomainModel.getCookHours(),
                        requestDomainModel.getCookMinutes())).
                setTotalTime(getTotalMinutes(
                        requestDomainModel.getPrepHours(),
                        requestDomainModel.getPrepMinutes()) +
                        getTotalMinutes(
                                requestDomainModel.getCookHours(),
                                requestDomainModel.getCookMinutes())).
                setCreateDate(persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel.getLastUpdate()).
                build();
    }

    private RecipeDurationResponse.DomainModel getResponseModelFromPersistenceModel() {
        return new RecipeDurationResponse.DomainModel.Builder().
                setPrepHours(getHours(persistenceModel.getPrepTime())).
                setPrepMinutes(getMinutes(persistenceModel.getPrepTime())).
                setTotalPrepTime(persistenceModel.getPrepTime()).
                setCookHours(getHours(persistenceModel.getCookTime())).
                setCookMinutes(getMinutes(persistenceModel.getCookTime())).
                setTotalCookTime(persistenceModel.getCookTime()).
                setTotalTime(persistenceModel.getPrepTime() + persistenceModel.getCookTime()).
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
