package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeFailReasonPrimitive;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import javax.annotation.Nonnull;

public class RepositoryRecipeFailReasons
        extends Repository<RecipeFailReasonPrimitive>
        implements DataSourceRecipeFailReasons {

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeFailReasonPrimitive> callback) {

    }

    @Override
    public void deleteAllByRecipeId(@Nonnull String recipeId) {

    }
}
