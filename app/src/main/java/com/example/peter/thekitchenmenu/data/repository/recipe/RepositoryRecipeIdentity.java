package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentity extends Repository<RecipeIdentityEntity> {

    public static RepositoryRecipeIdentity INSTANCE = null;

    private RepositoryRecipeIdentity(
            @Nonnull PrimitiveDataSource<RecipeIdentityEntity> remoteDataSource,
            @Nonnull PrimitiveDataSource<RecipeIdentityEntity> localDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeIdentity getInstance(
            PrimitiveDataSource<RecipeIdentityEntity> remoteDataSource,
            PrimitiveDataSource<RecipeIdentityEntity> localDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentity(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}
