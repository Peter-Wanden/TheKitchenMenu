package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.entity.recipeIdentity.RecipeIdentityEntityModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;

public class RecipeIdentityDomainModelConverter
        implements
        DomainModel.ModelConverter<
                RecipeIdentityEntityModel,
                RecipeIdentityUseCaseModel,
                RecipeIdentityPersistenceModel,
                RecipeIdentityRequestModel,
                RecipeIdentityResponseModel> {

    @Override
    public RecipeIdentityEntityModel convertUseCaseToEntityModel(RecipeIdentityUseCaseModel useCaseModel) {
        return null;
    }

    @Override
    public RecipeIdentityUseCaseModel convertPersistenceToDomainModel(RecipeIdentityPersistenceModel model) {
        return null;
    }

    @Override
    public RecipeIdentityUseCaseModel convertRequestToUseCaseModel(RecipeIdentityRequestModel model) {
        return null;
    }

    @Override
    public RecipeIdentityUseCaseModel convertEntityToUseCaseModel(RecipeIdentityEntityModel entityModel) {
        return null;
    }

    @Override
    public RecipeIdentityPersistenceModel createNewPersistenceModel() {
        return null;
    }

    @Override
    public RecipeIdentityPersistenceModel createArchivedPersistenceModel(RecipeIdentityPersistenceModel model) {
        return null;
    }
}
