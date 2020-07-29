package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public class RecipeIdentityDomainModelConverter
        extends
        DomainModel.Converter<
                RecipeIdentityUseCaseModel,
                RecipeIdentityUseCasePersistenceModel,
                RecipeIdentityRequestModel,
                RecipeIdentityResponseModel> {

    public RecipeIdentityDomainModelConverter(
            @Nonnull TimeProvider timeProvider,
            @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public RecipeIdentityUseCaseModel convertPersistenceToDomainModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel persistenceModel) {
        return new RecipeIdentityUseCaseModel(
                persistenceModel.getTitle(),
                persistenceModel.getDescription()
        );
    }

    @Override
    public RecipeIdentityUseCaseModel convertRequestToUseCaseModel(
            @Nonnull RecipeIdentityRequestModel requestModel) {
        return null;
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel createNewPersistenceModel(
            @Nonnull String domainId,
            @Nonnull RecipeIdentityUseCaseModel useCaseModel) {
        return null;
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel updatePersistenceModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel persistenceModel,
            @Nonnull RecipeIdentityUseCaseModel useCaseModel) {
        return null;
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel model) {
        return null;
    }

    @Override
    public RecipeIdentityResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeIdentityUseCaseModel model) {
        return null;
    }
}
