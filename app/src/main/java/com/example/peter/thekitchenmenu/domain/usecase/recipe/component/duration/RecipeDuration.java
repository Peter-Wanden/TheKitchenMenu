package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeDurationUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCaseFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public class RecipeDuration
        extends
        UseCaseElement<
                RecipeDurationUseCaseDataAccess,
                RecipeDurationUseCasePersistenceModel,
                RecipeDuration.DomainModel> {

    private static final String TAG = "tkm-" + RecipeDuration.class.getSimpleName() + ": ";

    protected static final class DomainModel
            implements
            com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel.UseCaseModel {
        private int prepTime;
        private int cookTime;

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

    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    public RecipeDuration(@Nonnull RecipeDurationUseCaseDataAccess repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          int maxPrepTime,
                          int maxCookTime) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        useCaseModel = createUseCaseModelFromDefaultValues();

        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;
    }

    @Override
    protected DomainModel createUseCaseModelFromPersistenceModel(
            @Nonnull RecipeDurationUseCasePersistenceModel persistenceModel) {

        return new DomainModel(
                persistenceModel.getPrepTime(),
                persistenceModel.getCookTime()
        );
    }

    @Override
    protected DomainModel createUseCaseModelFromDefaultValues() {
        return new DomainModel(0,0);
    }

    @Override
    protected DomainModel createUseCaseModelFromRequestModel() {
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
        if (useCaseModel.prepTime > MAX_PREP_TIME) {
            failReasons.add(RecipeDurationUseCaseFailReason.INVALID_PREP_TIME);
        }
    }

    private void validateCookTime() {
        if (useCaseModel.cookTime > MAX_COOK_TIME) {
            failReasons.add(RecipeDurationUseCaseFailReason.INVALID_COOK_TIME);
        }
    }

    @Override
    protected void save() {
        if (isChanged && isDomainModelValid()) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archivePreviousState(currentTime);
            }

            persistenceModel = new RecipeDurationUseCasePersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setPrepTime(useCaseModel.prepTime).
                    setCookTime(useCaseModel.cookTime).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }
        buildResponse();
    }

    @Override
    protected void archivePreviousState(long currentTime) {
        RecipeDurationUseCasePersistenceModel model = new RecipeDurationUseCasePersistenceModel.Builder().
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
                setPrepHours(getHours(useCaseModel.prepTime)).
                setPrepMinutes(getMinutes(useCaseModel.prepTime)).
                setTotalPrepTime(useCaseModel.prepTime).
                setCookHours(getHours(useCaseModel.cookTime)).
                setCookMinutes(getMinutes(useCaseModel.cookTime)).
                setTotalCookTime(useCaseModel.cookTime).
                setTotalTime(useCaseModel.prepTime + useCaseModel.cookTime).
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
