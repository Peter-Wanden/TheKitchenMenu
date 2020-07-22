package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RepositoryRecipeMetadataLocal;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeMetadataRemote;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;


public class DataAccessRecipeMetadata
        extends
        DataAccess<RecipeMetadataPersistenceModel> {

    protected static DataAccess<RecipeMetadataPersistenceModel> INSTANCE = null;

    private DataAccessRecipeMetadata(@Nonnull RepositoryRecipeMetadataRemote remoteDataAccess,
                                     @Nonnull RepositoryRecipeMetadataLocal localDataAccess) {
        this.remoteDomainDataAccess = remoteDataAccess;
        this.localDomainDataAccess = localDataAccess;
    }

    public static DataAccessRecipeMetadata getInstance(
            @Nonnull RepositoryRecipeMetadataRemote remoteDataAccess,
            @Nonnull RepositoryRecipeMetadataLocal localDataAccess) {
        if (INSTANCE == null) {
            INSTANCE = new DataAccessRecipeMetadata(remoteDataAccess, localDataAccess);
        }
        return (DataAccessRecipeMetadata) INSTANCE;
    }
}
