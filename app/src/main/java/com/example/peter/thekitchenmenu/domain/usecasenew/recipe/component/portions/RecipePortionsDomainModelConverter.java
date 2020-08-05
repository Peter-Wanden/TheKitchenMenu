package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public final class RecipePortionsDomainModelConverter
        extends
        DomainModelConverter<
                        RecipePortionsUseCaseModel,
                        RecipePortionsUseCasePersistenceModel,
                        RecipePortionsUseCaseRequestModel,
                        RecipePortionsUseCaseResponseModel> {
    public RecipePortionsDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                              @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public RecipePortionsUseCaseModel convertPersistenceToUseCaseModel(
            @Nonnull RecipePortionsUseCasePersistenceModel persistenceModel) {

        return new RecipePortionsUseCaseModel.Builder()
                .setServings(persistenceModel.getServings())
                .setSittings(persistenceModel.getSittings())
                .build();
    }

    @Override
    public RecipePortionsUseCaseModel convertRequestToUseCaseModel(
            @Nonnull RecipePortionsUseCaseRequestModel requestModel) {
        return new RecipePortionsUseCaseModel.Builder()
                .setServings(requestModel.getServings())
                .setSittings(requestModel.getSittings())
                .build();
    }

    @Override
    public RecipePortionsUseCasePersistenceModel convertUseCaseToPersistenceModel(
            @Nonnull String domainId,
            @Nonnull RecipePortionsUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(domainId)
                .setServings(useCaseModel.getServings())
                .setSittings(useCaseModel.getSittings())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipePortionsUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipePortionsUseCasePersistenceModel persistenceModel) {
        return new RecipePortionsUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public RecipePortionsUseCaseResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipePortionsUseCaseModel useCaseModel) {
        return new RecipePortionsUseCaseResponseModel.Builder()
                .setServings(useCaseModel.getServings())
                .setSittings(useCaseModel.getSittings())
                .setPortions(useCaseModel.getServings() * useCaseModel.getSittings())
                .build();
    }

    @Override
    public RecipePortionsUseCasePersistenceModel updatePersistenceModel(
            @Nonnull RecipePortionsUseCasePersistenceModel persistenceModel,
            @Nonnull RecipePortionsUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(persistenceModel.getDomainId())
                .setServings(useCaseModel.getServings())
                .setSittings(useCaseModel.getSittings())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipePortionsUseCaseModel getDefault() {
        return new RecipePortionsUseCaseModel.Builder().getDefault().build();
    }
}
