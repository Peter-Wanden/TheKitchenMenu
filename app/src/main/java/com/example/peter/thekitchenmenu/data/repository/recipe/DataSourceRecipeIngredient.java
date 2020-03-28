package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipeIngredient extends DataSource<RecipeIngredientModel> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull DataSource.GetAllCallback<RecipeIngredientEntity> callback);

    void getAllByProductId(@Nonnull String productId,
                           @Nonnull GetAllCallback<RecipeIngredientEntity> callback);

    void getAllByIngredientId(@Nonnull String ingredientId,
                              @Nonnull GetAllCallback<RecipeIngredientEntity> callback);
}
