package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeDuration
        extends
        UseCaseElement<
                RecipeDurationPersistenceModel,
                RecipeDuration.DomainModel,
                RepositoryRecipeDuration> {

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

    protected static final class DomainModel
            implements
            UseCaseDomainModel {
        private int prepTime;
        private int cookTime;

        public DomainModel() {
        }

        public DomainModel(int prepTime, int cookTime) {
            this.prepTime = prepTime;
            this.cookTime = cookTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DomainModel)) return false;

            DomainModel that = (DomainModel) o;

            if (prepTime != that.prepTime) return false;
            return cookTime == that.cookTime;
        }

        @Override
        public int hashCode() {
            int result = prepTime;
            result = 31 * result + cookTime;
            return result;
        }

        @Nonnull
        @Override
        public String toString() {
            return "DomainModel{" +
                    "prepTime=" + prepTime +
                    ", cookTime=" + cookTime +
                    '}';
        }
    }

    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final TimeProvider timeProvider;

    public RecipeDuration(@Nonnull RepositoryRecipeDuration repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          int maxPrepTime,
                          int maxCookTime) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;

        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;

        activeDomainModel = new DomainModel();
        updatedDomainModel = new DomainModel();
    }

    @Override
    protected void createDomainModelsFromPersistenceModel(
            @Nonnull RecipeDurationPersistenceModel persistenceModel) {

        updatedDomainModel = new DomainModel(
                persistenceModel.getPrepTime(),
                persistenceModel.getCookTime()
        );

        activeDomainModel = new DomainModel(
                persistenceModel.getPrepTime(),
                persistenceModel.getCookTime()
        );
    }

    @Override
    protected void createUpdatedDomainModelFromDefaultValues() {
        updatedDomainModel = new DomainModel();
    }

    @Override
    protected void createUpdatedDomainModelFromRequestModel() {
        RecipeDurationRequest request = (RecipeDurationRequest) getRequest();
        RecipeDurationRequest.DomainModel requestModel = request.getDomainModel();

        updatedDomainModel = new DomainModel(
                getTotalMinutes(requestModel.getPrepHours(), requestModel.getPrepMinutes()),
                getTotalMinutes(requestModel.getCookHours(), requestModel.getCookMinutes())
        );
    }

    @Override
    protected void initialiseUseCaseForUpdatedDomainModelProcessing() {
        failReasons.clear();
        isChanged = !activeDomainModel.equals(updatedDomainModel);
        validateUpdatedDomainModelElements();
    }

    @Override
    protected void validateUpdatedDomainModelElements() {
        validatePrepTime();
        validateCookTime();
        save();
    }

    private void validatePrepTime() {
        if (updatedDomainModel.prepTime > MAX_PREP_TIME) {
            failReasons.add(FailReason.INVALID_PREP_TIME);
        }
        activeDomainModel.prepTime = updatedDomainModel.prepTime;
    }

    private void validateCookTime() {
        if (updatedDomainModel.cookTime > MAX_COOK_TIME) {
            failReasons.add(FailReason.INVALID_COOK_TIME);
        }
        activeDomainModel.cookTime = updatedDomainModel.cookTime;
    }

    @Override
    protected void save() {
        System.out.println(TAG + "save: " +
                "\n  - isChanged=" + isChanged +
                "\n  - isDomainModelValid=" + isDomainModelValid() +
                "\n  - failReasons=" + failReasons
        );

        if (isChanged && isDomainModelValid()) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archivePersistenceModel(currentTime);
            }

            persistenceModel = new RecipeDurationPersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setPrepTime(activeDomainModel.prepTime).
                    setCookTime(activeDomainModel.cookTime).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }
        buildResponse();
    }

    @Override
    protected void archivePersistenceModel(long currentTime) {
        RecipeDurationPersistenceModel archiveModel = new RecipeDurationPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

        repository.save(archiveModel);
    }

    protected void buildResponse() {
        RecipeDurationResponse response = new RecipeDurationResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel()).
                build();

        sendResponse(response);
    }

    private RecipeDurationResponse.DomainModel getResponseModel() {
        return new RecipeDurationResponse.DomainModel.Builder().
                setPrepHours(getHours(activeDomainModel.prepTime)).
                setPrepMinutes(getMinutes(activeDomainModel.prepTime)).
                setTotalPrepTime(activeDomainModel.prepTime).
                setCookHours(getHours(activeDomainModel.cookTime)).
                setCookMinutes(getMinutes(activeDomainModel.cookTime)).
                setTotalCookTime(activeDomainModel.cookTime).
                setTotalTime(activeDomainModel.prepTime + activeDomainModel.cookTime).
                build();
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
}
