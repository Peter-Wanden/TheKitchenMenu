package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public final class RecipeIdentityDomainModelConverter
        extends
        DomainModelConverter<
                        RecipeIdentityUseCaseModel,
                        RecipeIdentityUseCasePersistenceModel,
                        RecipeIdentityUseCaseRequestModel,
                        RecipeIdentityUseCaseResponseModel> {

    public RecipeIdentityDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                              @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public RecipeIdentityUseCaseModel convertPersistenceToUseCaseModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel persistenceModel) {
        return new RecipeIdentityUseCaseModel.Builder()
                .setTitle(persistenceModel.getTitle())
                .setDescription(persistenceModel.getDescription())
                .build();
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel convertUseCaseToPersistenceModel(
            @Nonnull String domainId,
            @Nonnull RecipeIdentityUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(domainId)
                .setTitle(useCaseModel.getTitle())
                .setDescription(useCaseModel.getDescription())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeIdentityUseCaseModel convertRequestToUseCaseModel(
            @Nonnull RecipeIdentityUseCaseRequestModel requestModel) {
        return new RecipeIdentityUseCaseModel.Builder()
                .setTitle(requestModel.getTitle())
                .setDescription(requestModel.getDescription())
                .build();
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel updatePersistenceModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel persistenceModel,
            @Nonnull RecipeIdentityUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(persistenceModel.getDomainId())
                .setTitle(useCaseModel.getTitle())
                .setDescription(useCaseModel.getDescription())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel persistenceModel) {
        return new RecipeIdentityUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public RecipeIdentityUseCaseResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeIdentityUseCaseModel useCaseModel) {
        return new RecipeIdentityUseCaseResponseModel.Builder()
                .setTitle(useCaseModel.getTitle())
                .setDescription(useCaseModel.getDescription())
                .build();
    }

    @Override
    public RecipeIdentityUseCaseModel getDefault() {
        return new RecipeIdentityUseCaseModel.Builder()
                .getDefault()
                .build();
    }
}
