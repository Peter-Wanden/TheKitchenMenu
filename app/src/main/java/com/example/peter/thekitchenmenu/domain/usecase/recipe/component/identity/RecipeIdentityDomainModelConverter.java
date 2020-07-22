package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import javax.annotation.Nonnull;

public class RecipeIdentityDomainModelConverter
        implements
        DomainModel.Converter<
                        RecipeIdentityUseCaseModel,
                        RecipeIdentityUseCasePersistenceModel,
                        RecipeIdentityUseCaseRequestModel,
                        RecipeIdentityUseCaseResponseModel> {

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
            @Nonnull RecipeIdentityUseCaseRequestModel requestModel) {
        return null;
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel createNewPersistenceModel() {
        return null;
    }

    @Override
    public RecipeIdentityUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel model) {
        return null;
    }

    @Override
    public RecipeIdentityUseCaseResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeIdentityUseCaseModel model) {
        return null;
    }
}
