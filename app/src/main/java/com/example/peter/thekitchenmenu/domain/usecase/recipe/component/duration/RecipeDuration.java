package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class RecipeDuration extends UseCase
        implements DataAccess.GetDomainModelCallback<RecipeDurationPersistenceModel> {

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
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipeDuration repository;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String recipeId = "";
    private boolean isNewRequest;

    private RecipeDurationRequest.Model requestModel;
    private RecipeDurationPersistenceModel persistenceModel;

    public RecipeDuration(@Nonnull RepositoryRecipeDuration repository,
                          @Nonnull TimeProvider timeProvider,
                          int maxPrepTime,
                          int maxCookTime) {
        this.repository = repository;
        this.timeProvider = timeProvider;

        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;

        requestModel = new RecipeDurationRequest.Model.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeDurationRequest r = (RecipeDurationRequest) request;
        requestModel = r.getModel();
        System.out.println(TAG + r);

        if (isNewRequest(r)) {
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(RecipeDurationRequest r) {
        return isNewRequest = !this.recipeId.equals(r.getDomainId());
    }

    private void loadData(String recipeId) {
        repository.getByDataId(recipeId, this);
    }

    @Override
    public void onModelLoaded(RecipeDurationPersistenceModel persistenceModel) {
        this.persistenceModel = persistenceModel;
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
        return RecipeDurationPersistenceModel.Builder.getDefault().
                setDataId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
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
                setDataId("").
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
                setCreateDate(0L).setLasUpdate(0L).
                build();
    }

    private ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);

        if (!isValid && !isChanged()) {
            return ComponentState.INVALID_UNCHANGED;
        } else if (isValid && !isChanged()) {
            return ComponentState.VALID_UNCHANGED;
        } else if (!isValid && isChanged()) {
            return ComponentState.INVALID_CHANGED;
        } else {
            return ComponentState.VALID_CHANGED;
        }
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

    private RecipeDurationPersistenceModel updatePersistenceFromRequestModel() {
        return RecipeDurationPersistenceModel.Builder.
                basedOnPersistenceModel(persistenceModel).

                setPrepTime(getTotalMinutes(
                        requestModel.getPrepHours(),
                        requestModel.getPrepMinutes())).

                setCookTime(getTotalMinutes(
                        requestModel.getCookHours(),
                        requestModel.getCookMinutes())).

                setLastUpdate(timeProvider.getCurrentTimeInMills()).build();
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

    private void sendResponse(RecipeDurationResponse response) {
        System.out.println(TAG + response);
        resetState();

        if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void resetState() {
        failReasons.clear();
        isNewRequest = false;
    }

    private void save() {
        System.out.println(TAG + "savingPersistenceModel:" + persistenceModel);
        repository.save(persistenceModel);
    }
}
