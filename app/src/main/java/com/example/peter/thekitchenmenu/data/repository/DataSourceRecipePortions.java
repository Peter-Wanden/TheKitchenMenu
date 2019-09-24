package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;

public interface DataSourceRecipePortions extends DataSource<RecipePortionsEntity> {

    void getPortionsForRecipe(@NonNull String recipeId,
                              @NonNull GetEntityCallback<RecipePortionsEntity> callback);
}
