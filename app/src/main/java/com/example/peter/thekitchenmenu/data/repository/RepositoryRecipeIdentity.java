package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;

public class RepositoryRecipeIdentity extends Repository<RecipeIdentityEntity> {

    public static RepositoryRecipeIdentity INSTANCE = null;

    private RepositoryRecipeIdentity(
            @NonNull DataSource<RecipeIdentityEntity> remoteDataSource,
            @NonNull DataSource<RecipeIdentityEntity> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeIdentity getInstance(
            DataSource<RecipeIdentityEntity> remoteDataSource,
            DataSource<RecipeIdentityEntity> localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeIdentity(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}
