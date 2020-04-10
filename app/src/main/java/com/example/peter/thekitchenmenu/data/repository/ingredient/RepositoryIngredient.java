package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryIngredient extends Repository<IngredientPersistenceModel> {

    private RepositoryIngredient(
            @Nonnull DomainDataAccess<IngredientPersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientPersistenceModel> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RepositoryIngredient getInstance(
            @Nonnull DomainDataAccess<IngredientPersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientPersistenceModel> localDomainDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredient(remoteDomainDataAccess, localDomainDataAccess);
        return (RepositoryIngredient) INSTANCE;
    }
}
