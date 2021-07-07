package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource;

import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceParent;

import javax.annotation.Nonnull;

public interface RecipeIngredientLocal
        extends PrimitiveDataSourceParent<RecipeIngredientEntity> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback);

    void getAllByIngredientId(@Nonnull String ingredientId,
                              @Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback);

    void getAllByProductId(@Nonnull String productDomainId,
                           @Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback);

}
