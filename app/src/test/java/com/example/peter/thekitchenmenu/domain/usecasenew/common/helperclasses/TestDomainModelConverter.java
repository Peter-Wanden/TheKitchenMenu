package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public class TestDomainModelConverter
        extends
        DomainModelConverter<
                        TestUseCaseInternalModel,
                        TestUseCasePersistenceModel,
                        TestRequestModel,
                        TestResponseModel> {

    public TestDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                    @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public TestUseCaseInternalModel convertPersistenceToUseCaseModel(
            @Nonnull TestUseCasePersistenceModel persistenceModel) {
        return new TestUseCaseInternalModel.Builder()
                .setUseCaseModelString(persistenceModel.getPersistenceModelString())
                .build();
    }

    @Override
    public TestUseCaseInternalModel convertRequestToUseCaseModel(
            @Nonnull TestRequestModel requestModel) {
        return new TestUseCaseInternalModel.Builder()
                .setUseCaseModelString(requestModel.getRequestModelString())
                .build();
    }

    @Override
    public TestUseCasePersistenceModel convertUseCaseToPersistenceModel(
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
            @Nonnull TestUseCasePersistenceModel persistenceModel) {

        return new TestUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public TestResponseModel convertUseCaseToResponseModel(
            @Nonnull TestUseCaseInternalModel useCaseModel) {
        return new TestResponseModel.Builder()
                .setResponseModelString(useCaseModel.getUseCaseModelString())
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

    @Override
    public TestUseCaseInternalModel getDefault() {
        return new TestUseCaseInternalModel.Builder().getDefault().build();
    }
}
