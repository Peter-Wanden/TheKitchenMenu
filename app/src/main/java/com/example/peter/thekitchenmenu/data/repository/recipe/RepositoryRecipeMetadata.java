package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.LocalRepositoryRecipeMetadataAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeMetadataRemoteDomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;


public class RepositoryRecipeMetadata extends Repository<RecipeMetadataPersistenceModel> {

    private RepositoryRecipeMetadata(@Nonnull RecipeMetadataRemoteDomainDataAccess remoteDataAccess,
                                     @Nonnull LocalRepositoryRecipeMetadataAdapter localDataAccess) {
        this.remoteDomainDataAccess = remoteDataAccess;
        this.localDomainDataAccess = localDataAccess;
    }

    public static RepositoryRecipeMetadata getInstance(
            @Nonnull RecipeMetadataRemoteDomainDataAccess remoteDataAccess,
            @Nonnull LocalRepositoryRecipeMetadataAdapter localDataAccess) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeMetadata(remoteDataAccess, localDataAccess);
        }
        return (RepositoryRecipeMetadata) INSTANCE;
    }
}
