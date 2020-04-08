package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityModelPersistence;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentity extends Repository<RecipeIdentityModelPersistence> {

    public static RepositoryRecipeIdentity INSTANCE = null;

    private RepositoryRecipeIdentity(
            @Nonnull DomainDataAccess<RecipeIdentityModelPersistence> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeIdentityModelPersistence> localDomainDataAccess) {

        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RepositoryRecipeIdentity getInstance(
            @Nonnull DomainDataAccess<RecipeIdentityModelPersistence> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeIdentityModelPersistence> localDomainDataAccess) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentity(remoteDomainDataAccess, localDomainDataAccess);
        }
        return INSTANCE;
    }
}
