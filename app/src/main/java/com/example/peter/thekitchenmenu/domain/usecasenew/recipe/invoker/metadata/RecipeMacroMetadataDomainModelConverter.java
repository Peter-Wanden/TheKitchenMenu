package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public final class RecipeMacroMetadataDomainModelConverter
        extends
        DomainModelConverter<
                RecipeMacroMetadataUseCaseModel,
                RecipeMacroMetadataUseCasePersistenceModel,
                RecipeMacroMetadataUseCaseRequestModel,
                RecipeMacroMetadataUseCaseResponseModel> {

    public RecipeMacroMetadataDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                                   @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public RecipeMacroMetadataUseCaseModel convertPersistenceToUseCaseModel(
            @Nonnull RecipeMacroMetadataUseCasePersistenceModel persistenceModel) {

        return new RecipeMacroMetadataUseCaseModel.Builder()
                .setComponentState(persistenceModel.getComponentState())
                .setComponentStates(persistenceModel.getComponentStates())
                .setFailReasons(persistenceModel.getFailReasons())
                .build();
    }

    @Override
    public RecipeMacroMetadataUseCaseModel convertRequestToUseCaseModel(
            @Nonnull RecipeMacroMetadataUseCaseRequestModel requestModel) {
        return new RecipeMacroMetadataUseCaseModel.Builder()
                .getDefault()
                .setComponentStates(requestModel.getComponentStates())
                .build();
    }

    @Override
    public RecipeMacroMetadataUseCasePersistenceModel convertUseCaseToPersistenceModel(
            @Nonnull String domainId, @Nonnull RecipeMacroMetadataUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeMacroMetadataUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(domainId)
                .setComponentState(useCaseModel.getComponentState())
                .setComponentStates(useCaseModel.getComponentStates())
                .setFailReasons(useCaseModel.getFailReasons())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeMacroMetadataUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeMacroMetadataUseCasePersistenceModel persistenceModel) {
        return new RecipeMacroMetadataUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public RecipeMacroMetadataUseCaseResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeMacroMetadataUseCaseModel useCaseModel) {
        return new RecipeMacroMetadataUseCaseResponseModel.Builder()
                .setComponentState(useCaseModel.getComponentState())
                .setComponentStates(useCaseModel.getComponentStates())
                .setFailReasons(useCaseModel.getFailReasons())
                .build();
    }

    @Override
    public RecipeMacroMetadataUseCasePersistenceModel updatePersistenceModel(
            @Nonnull RecipeMacroMetadataUseCasePersistenceModel persistenceModel,
            @Nonnull RecipeMacroMetadataUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeMacroMetadataUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setDataId(idProvider.getUId())
                .setComponentState(useCaseModel.getComponentState())
                .setComponentStates(useCaseModel.getComponentStates())
                .setFailReasons(useCaseModel.getFailReasons())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeMacroMetadataUseCaseModel getDefault() {
        return new RecipeMacroMetadataUseCaseModel.Builder().getDefault().build();
    }
}
