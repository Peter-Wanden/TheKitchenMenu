package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryIngredient extends Repository<IngredientPersistenceDomainModel> {

    public static RepositoryIngredient INSTANCE = null;

    private RepositoryIngredient(
            @Nonnull DomainDataAccess<IngredientPersistenceDomainModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientPersistenceDomainModel> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RepositoryIngredient getInstance(
            @Nonnull DomainDataAccess<IngredientPersistenceDomainModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientPersistenceDomainModel> localDomainDataAccess) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryIngredient(remoteDomainDataAccess, localDomainDataAccess);
        }
        return INSTANCE;
    }
}
