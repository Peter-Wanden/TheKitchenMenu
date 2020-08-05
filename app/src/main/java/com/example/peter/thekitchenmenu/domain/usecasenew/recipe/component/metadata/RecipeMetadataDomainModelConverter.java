package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public final class RecipeMetadataDomainModelConverter
        extends
        DomainModelConverter<
                        RecipeMetadataUseCaseModel,
                        RecipeMetadataUseCasePersistenceModel,
                        RecipeMetadataUseCaseRequestModel,
                        RecipeMetadataUseCaseResponseModel> {

    public RecipeMetadataDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                              @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public RecipeMetadataUseCaseModel convertPersistenceToUseCaseModel(
            @Nonnull RecipeMetadataUseCasePersistenceModel persistenceModel) {

        return new RecipeMetadataUseCaseModel.Builder()
                .setComponentState(persistenceModel.getComponentState())
                .setComponentStates(persistenceModel.getComponentStates())
                .setFailReasons(persistenceModel.getFailReasons())
                .build();
    }

    @Override
    public RecipeMetadataUseCaseModel convertRequestToUseCaseModel(
            @Nonnull RecipeMetadataUseCaseRequestModel requestModel) {
        return new RecipeMetadataUseCaseModel.Builder()
                .getDefault()
                .setComponentStates(requestModel.getComponentStates())
                .build();
    }

    @Override
    public RecipeMetadataUseCasePersistenceModel convertUseCaseToPersistenceModel(
            @Nonnull String domainId, @Nonnull RecipeMetadataUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeMetadataUseCasePersistenceModel.Builder()
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
    public RecipeMetadataUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeMetadataUseCasePersistenceModel persistenceModel) {
        return new RecipeMetadataUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public RecipeMetadataUseCaseResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeMetadataUseCaseModel useCaseModel) {
        return null;
    }

    @Override
    public RecipeMetadataUseCasePersistenceModel updatePersistenceModel(
            @Nonnull RecipeMetadataUseCasePersistenceModel persistenceModel,
            @Nonnull RecipeMetadataUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeMetadataUseCasePersistenceModel.Builder()
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
    public RecipeMetadataUseCaseModel getDefault() {
        return new RecipeMetadataUseCaseModel.Builder().getDefault().build();
    }
}
