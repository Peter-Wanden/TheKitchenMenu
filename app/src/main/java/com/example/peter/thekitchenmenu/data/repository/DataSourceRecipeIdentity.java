package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

public interface DataSourceRecipeIdentity extends DataSource<RecipeIdentityEntity> {

    void getByRecipeId(@NonNull String recipeId,
                       @NonNull GetEntityCallback<RecipeIdentityEntity> callback);
}
