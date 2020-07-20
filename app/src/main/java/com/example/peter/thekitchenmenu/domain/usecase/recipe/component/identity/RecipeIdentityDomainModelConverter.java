package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.businessentity.recipeIdentity.RecipeIdentityEntityModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import javax.annotation.Nonnull;

public class RecipeIdentityDomainModelConverter
        implements
        DomainModel.ModelConverter<
                RecipeIdentityEntityModel,
                RecipeIdentityUseCaseModel,
                RecipeIdentityPersistenceModel,
                RecipeIdentityRequestModel,
                RecipeIdentityResponseModel> {

    @Override
    public RecipeIdentityEntityModel convertUseCaseToEntityModel(
            @Nonnull RecipeIdentityUseCaseModel useCaseModel) {
        return new RecipeIdentityEntityModel(
                useCaseModel.getTitle(),
                useCaseModel.getDescription()
        );
    }

    @Override
    public RecipeIdentityUseCaseModel convertPersistenceToDomainModel(
            @Nonnull RecipeIdentityPersistenceModel persistenceModel) {
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
    public RecipeIdentityUseCaseModel convertEntityToUseCaseModel(
            @Nonnull RecipeIdentityEntityModel entityModel) {
        return null;
    }

    @Override
    public RecipeIdentityPersistenceModel createNewPersistenceModel() {
        return null;
    }

    @Override
    public RecipeIdentityPersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeIdentityPersistenceModel model) {
        return null;
    }

    @Override
    public RecipeIdentityResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeIdentityUseCaseModel model) {
        return null;
    }
}
