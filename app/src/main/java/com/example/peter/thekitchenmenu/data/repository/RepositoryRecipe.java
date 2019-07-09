package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipe extends Repository<RecipeEntity> {

    public static RepositoryRecipe INSTANCE = null;

    private RepositoryRecipe(
            @NonNull DataSource<RecipeEntity> remoteDataSource,
            @NonNull DataSource<RecipeEntity> localDataSource) {

        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static RepositoryRecipe getInstance(
            DataSource<RecipeEntity> remoteDataSource,
            DataSource<RecipeEntity> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipe(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}