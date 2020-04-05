package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RecipeMetadataLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeMetadataRemoteDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;


public class RepositoryRecipeMetadata extends Repository<RecipeMetadataPersistenceModel> {

    private RepositoryRecipeMetadata(@Nonnull RecipeMetadataRemoteDataAccess remoteDataAccess,
                                     @Nonnull RecipeMetadataLocalDataAccess localDataAccess) {
        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static RepositoryRecipeMetadata getInstance(
            @Nonnull RecipeMetadataRemoteDataAccess remoteDataAccess,
            @Nonnull RecipeMetadataLocalDataAccess localDataAccess) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeMetadata(remoteDataAccess, localDataAccess);
        }
        return (RepositoryRecipeMetadata) INSTANCE;
    }
}
