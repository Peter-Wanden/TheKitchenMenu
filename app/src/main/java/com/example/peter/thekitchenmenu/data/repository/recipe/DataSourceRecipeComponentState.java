package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipeComponentState extends PrimitiveDataSource<RecipeComponentStateEntity> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllCallback<RecipeComponentStateEntity> callback);

    void deleteAllByRecipeId(@Nonnull String recipeId);
}
