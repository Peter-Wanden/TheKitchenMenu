package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentity extends Repository<RecipeIdentityPersistenceModel> {

    public static RepositoryRecipeIdentity INSTANCE = null;

    private RepositoryRecipeIdentity(
            @Nonnull DataAccess<RecipeIdentityPersistenceModel> remoteDataAccess,
            @Nonnull DataAccess<RecipeIdentityPersistenceModel> localDataAccess) {

        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static RepositoryRecipeIdentity getInstance(
            @Nonnull DataAccess<RecipeIdentityPersistenceModel> remoteDataAccess,
            @Nonnull DataAccess<RecipeIdentityPersistenceModel> localDataAccess) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentity(remoteDataAccess, localDataAccess);
        }
        return INSTANCE;
    }
}
