package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RepositoryRecipeMetadataLocal;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeMetadataRemote;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata.RecipeMacroMetadataUseCasePersistenceModel;

import javax.annotation.Nonnull;


public class RecipeMetadataUseCaseDataAccess
        extends
        DataAccess<RecipeMacroMetadataUseCasePersistenceModel> {

    protected static DataAccess<RecipeMacroMetadataUseCasePersistenceModel> INSTANCE = null;

    private RecipeMetadataUseCaseDataAccess(@Nonnull RepositoryRecipeMetadataRemote remoteDataAccess,
                                            @Nonnull RepositoryRecipeMetadataLocal localDataAccess) {
        this.remoteDomainDataAccess = remoteDataAccess;
        this.localDomainDataAccess = localDataAccess;
    }

    public static RecipeMetadataUseCaseDataAccess getInstance(
            @Nonnull RepositoryRecipeMetadataRemote remoteDataAccess,
            @Nonnull RepositoryRecipeMetadataLocal localDataAccess) {
        if (INSTANCE == null) {
            INSTANCE = new RecipeMetadataUseCaseDataAccess(remoteDataAccess, localDataAccess);
        }
        return (RecipeMetadataUseCaseDataAccess) INSTANCE;
    }
}
