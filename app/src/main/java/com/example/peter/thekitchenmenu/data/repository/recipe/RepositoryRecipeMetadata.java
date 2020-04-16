package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RepositoryRecipeMetadataLocal;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeMetadataRemote;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


public class RepositoryRecipeMetadata extends Repository<RecipeMetadataPersistenceModel> {

    private RepositoryRecipeMetadata(@Nonnull RepositoryRecipeMetadataRemote remoteDataAccess,
                                     @Nonnull RepositoryRecipeMetadataLocal localDataAccess) {
        this.remoteDomainDataAccess = remoteDataAccess;
        this.localDomainDataAccess = localDataAccess;
    }

    public static RepositoryRecipeMetadata getInstance(
            @Nonnull RepositoryRecipeMetadataRemote remoteDataAccess,
            @Nonnull RepositoryRecipeMetadataLocal localDataAccess) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeMetadata(remoteDataAccess, localDataAccess);
        }
        return (RepositoryRecipeMetadata) INSTANCE;
    }
}
