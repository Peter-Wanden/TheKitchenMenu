package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RecipeFailReasonEntity;

import javax.annotation.Nonnull;

public interface DataSourceRecipeFailReasons extends PrimitiveDataSource<RecipeFailReasonEntity> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllCallback<RecipeFailReasonEntity> callback);

    void deleteAllByRecipeId(@Nonnull String recipeId);
}
