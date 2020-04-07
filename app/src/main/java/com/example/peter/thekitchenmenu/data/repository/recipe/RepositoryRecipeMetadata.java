package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RecipeMetadataLocalDomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeMetadataRemoteDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataModelPersistence;

import javax.annotation.Nonnull;


public class RepositoryRecipeMetadata extends Repository<RecipeMetadataModelPersistence> {

    private RepositoryRecipeMetadata(@Nonnull RecipeMetadataRemoteDataAccess remoteDataAccess,
                                     @Nonnull RecipeMetadataLocalDomainDataAccess localDataAccess) {
        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static RepositoryRecipeMetadata getInstance(
            @Nonnull RecipeMetadataRemoteDataAccess remoteDataAccess,
            @Nonnull RecipeMetadataLocalDomainDataAccess localDataAccess) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeMetadata(remoteDataAccess, localDataAccess);
        }
        return (RepositoryRecipeMetadata) INSTANCE;
    }
}
