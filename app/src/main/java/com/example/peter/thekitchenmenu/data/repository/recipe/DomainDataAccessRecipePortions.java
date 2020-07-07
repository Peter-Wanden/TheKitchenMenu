package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceDomainModel;

import javax.annotation.Nonnull;

public interface DomainDataAccessRecipePortions
        extends DomainDataAccess<RecipePortionsPersistenceDomainModel> {

    void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceDomainModel> callback);

}
