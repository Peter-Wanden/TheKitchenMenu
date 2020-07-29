package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public class TestDomainModelConverter
        extends
        DomainModel.Converter<
                TestUseCaseInternalModel,
                TestUseCasePersistenceModel,
                TestRequestModel,
                TestResponseModel> {

    public TestDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                    @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public TestUseCaseInternalModel convertPersistenceToDomainModel(
            @Nonnull TestUseCasePersistenceModel persistenceModel) {
        return new TestUseCaseInternalModel(persistenceModel.getPersistenceModelString());
    }

    @Override
    public TestUseCaseInternalModel convertRequestToUseCaseModel(
            @Nonnull TestRequestModel requestModel) {
        return new TestUseCaseInternalModel(requestModel.getRequestModelString());
    }

    @Override
    public TestUseCasePersistenceModel createNewPersistenceModel(
            @Nonnull String domainId,
            @Nonnull TestUseCaseInternalModel useCaseModel) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new TestUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(domainId)
                .setPersistenceModelString(useCaseModel.getUseCaseModelString())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public TestUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull TestUseCasePersistenceModel oldPersistenceModel) {
        return new TestUseCasePersistenceModel.Builder()
                .basedOnModel(oldPersistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public TestResponseModel convertUseCaseToResponseModel(
            @Nonnull TestUseCaseInternalModel model) {
        return new TestResponseModel.Builder()
                .setResponseModelString(model.getUseCaseModelString())
                .build();
    }

    @Override
    public TestUseCasePersistenceModel updatePersistenceModel(
            @Nonnull TestUseCasePersistenceModel persistenceModel,
            @Nonnull TestUseCaseInternalModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new TestUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setDataId(idProvider.getUId())
                .setPersistenceModelString(useCaseModel.getUseCaseModelString())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }
}
