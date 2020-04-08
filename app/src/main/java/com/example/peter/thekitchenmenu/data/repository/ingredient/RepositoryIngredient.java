package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientModelPersistence;

import javax.annotation.Nonnull;

public class RepositoryIngredient extends Repository<IngredientModelPersistence> {

    private RepositoryIngredient(
            @Nonnull DomainDataAccess<IngredientModelPersistence> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientModelPersistence> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RepositoryIngredient getInstance(
            @Nonnull DomainDataAccess<IngredientModelPersistence> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientModelPersistence> localDomainDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredient(remoteDomainDataAccess, localDomainDataAccess);
        return (RepositoryIngredient) INSTANCE;
    }
}
