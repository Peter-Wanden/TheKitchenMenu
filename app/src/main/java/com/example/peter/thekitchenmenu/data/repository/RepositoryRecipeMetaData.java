package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;

import javax.annotation.Nonnull;


public class RepositoryRecipeMetaData extends Repository<RecipeMetadataEntity> {

    public static RepositoryRecipeMetaData INSTANCE = null;

    private RepositoryRecipeMetaData(
            @Nonnull PrimitiveDataSource<RecipeMetadataEntity> remoteDataSource,
            @Nonnull PrimitiveDataSource<RecipeMetadataEntity> localDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeMetaData getInstance(
            PrimitiveDataSource<RecipeMetadataEntity> remoteDataSource,
            PrimitiveDataSource<RecipeMetadataEntity> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeMetaData(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}