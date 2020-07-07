package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentity extends Repository<RecipeIdentityPersistenceDomainModel> {

    public static RepositoryRecipeIdentity INSTANCE = null;

    private RepositoryRecipeIdentity(
            @Nonnull DomainDataAccess<RecipeIdentityPersistenceDomainModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeIdentityPersistenceDomainModel> localDomainDataAccess) {

        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RepositoryRecipeIdentity getInstance(
            @Nonnull DomainDataAccess<RecipeIdentityPersistenceDomainModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeIdentityPersistenceDomainModel> localDomainDataAccess) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentity(remoteDomainDataAccess, localDomainDataAccess);
        }
        return INSTANCE;
    }
}
