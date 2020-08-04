package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

import javax.annotation.Nonnull;

public interface DomainDataAccessRecipePortions
        extends DomainDataAccess<RecipePortionsUseCasePersistenceModel> {

    void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel> callback);

}
