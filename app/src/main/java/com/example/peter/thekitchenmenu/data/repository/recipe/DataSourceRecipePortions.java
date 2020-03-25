package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipePortions extends PrimitiveDataSource<RecipePortionsEntity> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetEntityCallback<RecipePortionsEntity> callback);
}
