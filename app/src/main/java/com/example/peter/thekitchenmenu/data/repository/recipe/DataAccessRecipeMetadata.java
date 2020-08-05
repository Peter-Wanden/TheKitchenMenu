package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RepositoryRecipeMetadataLocal;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeMetadataRemote;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata.RecipeMetadataUseCasePersistenceModel;

import javax.annotation.Nonnull;


public class DataAccessRecipeMetadata
        extends
        DataAccess<RecipeMetadataUseCasePersistenceModel> {

    protected static DataAccess<RecipeMetadataUseCasePersistenceModel> INSTANCE = null;

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
