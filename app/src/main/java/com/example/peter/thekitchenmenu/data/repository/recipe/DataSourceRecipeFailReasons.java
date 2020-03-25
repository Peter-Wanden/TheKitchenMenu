package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeFailReasonPrimitive;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipeFailReasons extends PrimitiveDataSource<RecipeFailReasonPrimitive> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllCallback<RecipeFailReasonPrimitive> callback);

    void deleteAllByRecipeId(@Nonnull String recipeId);
}
