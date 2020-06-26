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
                RepositoryRecipeDuration,
                RecipeDurationPersistenceModel,
                RecipeDuration.DomainModel> {

    private static final String TAG = "tkm-" + RecipeDuration.class.getSimpleName() + ": ";

    protected static final class DomainModel
            implements
            UseCaseDomainModel {
        private int prepTime;
        private int cookTime;

        private DomainModel() {
        }

        private DomainModel(int prepTime, int cookTime) {
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

    public enum FailReason
            implements
            FailReasons {
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

    public RecipeDuration(@Nonnull RepositoryRecipeDuration repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          int maxPrepTime,
                          int maxCookTime) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        domainModel = new DomainModel();

        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;
    }

    @Override
    protected DomainModel createDomainModelFromPersistenceModel(
            @Nonnull RecipeDurationPersistenceModel persistenceModel) {

        return new DomainModel(
                persistenceModel.getPrepTime(),
                persistenceModel.getCookTime()
        );
    }

    @Override
    protected DomainModel createDomainModelFromDefaultValues() {
        return new DomainModel();
    }

    @Override
    protected DomainModel createDomainModelFromRequestModel() {
        RecipeDurationRequest.DomainModel requestModel = ((RecipeDurationRequest) getRequest()).
                getDomainModel();

        return new DomainModel(
                getTotalMinutes(requestModel.getPrepHours(), requestModel.getPrepMinutes()),
                getTotalMinutes(requestModel.getCookHours(), requestModel.getCookMinutes())
        );
    }

    @Override
    protected void validateDomainModelElements() {
        validatePrepTime();
        validateCookTime();
        save();
    }

    private void validatePrepTime() {
        if (domainModel.prepTime > MAX_PREP_TIME) {
            failReasons.add(FailReason.INVALID_PREP_TIME);
        }
    }

    private void validateCookTime() {
        if (domainModel.cookTime > MAX_COOK_TIME) {
            failReasons.add(FailReason.INVALID_COOK_TIME);
        }
    }

    @Override
    protected void save() {
        if (isChanged && isDomainModelValid()) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archiveExistingPersistenceModel(currentTime);
            }

            persistenceModel = new RecipeDurationPersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setPrepTime(domainModel.prepTime).
                    setCookTime(domainModel.cookTime).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }
        buildResponse();
    }

    @Override
    protected void archiveExistingPersistenceModel(long currentTime) {
        RecipeDurationPersistenceModel model = new RecipeDurationPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

        System.out.println(TAG + "archivePersistenceModel= " + model);

        repository.save(model);
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
                setPrepHours(getHours(domainModel.prepTime)).
                setPrepMinutes(getMinutes(domainModel.prepTime)).
                setTotalPrepTime(domainModel.prepTime).
                setCookHours(getHours(domainModel.cookTime)).
                setCookMinutes(getMinutes(domainModel.cookTime)).
                setTotalCookTime(domainModel.cookTime).
                setTotalTime(domainModel.prepTime + domainModel.cookTime).
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
