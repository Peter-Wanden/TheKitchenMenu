package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentity extends Repository<RecipeIdentityPersistenceModel> {

    public static RepositoryRecipeIdentity INSTANCE = null;

    private RepositoryRecipeIdentity(
            @Nonnull DataSource<RecipeIdentityPersistenceModel> remoteDataSource,
            @Nonnull DataSource<RecipeIdentityPersistenceModel> localDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeIdentity getInstance(
            DataSource<RecipeIdentityPersistenceModel> remoteDataSource,
            DataSource<RecipeIdentityPersistenceModel> localDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentity(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}
