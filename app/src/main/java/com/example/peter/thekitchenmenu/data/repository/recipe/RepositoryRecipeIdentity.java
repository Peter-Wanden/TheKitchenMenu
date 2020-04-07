package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityModelPersistence;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentity extends Repository<RecipeIdentityModelPersistence> {

    public static RepositoryRecipeIdentity INSTANCE = null;

    private RepositoryRecipeIdentity(
            @Nonnull DataAccess<RecipeIdentityModelPersistence> remoteDataAccess,
            @Nonnull DataAccess<RecipeIdentityModelPersistence> localDataAccess) {

        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static RepositoryRecipeIdentity getInstance(
            @Nonnull DataAccess<RecipeIdentityModelPersistence> remoteDataAccess,
            @Nonnull DataAccess<RecipeIdentityModelPersistence> localDataAccess) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentity(remoteDataAccess, localDataAccess);
        }
        return INSTANCE;
    }
}
