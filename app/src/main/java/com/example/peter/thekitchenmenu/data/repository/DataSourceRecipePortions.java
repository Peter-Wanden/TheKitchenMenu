package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;

import javax.annotation.Nonnull;

public interface DataSourceRecipePortions extends PrimitiveDataSource<RecipePortionsEntity> {

    void getPortionsForRecipe(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipePortionsEntity> callback);
}
