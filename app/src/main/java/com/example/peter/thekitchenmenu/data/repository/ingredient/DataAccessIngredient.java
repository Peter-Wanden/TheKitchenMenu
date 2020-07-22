package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import javax.annotation.Nonnull;

public class DataAccessIngredient extends DataAccess<IngredientPersistenceModel> {

    public static DataAccessIngredient INSTANCE = null;

    private DataAccessIngredient(
            @Nonnull DomainDataAccess<IngredientPersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientPersistenceModel> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static DataAccessIngredient getInstance(
            @Nonnull DomainDataAccess<IngredientPersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<IngredientPersistenceModel> localDomainDataAccess) {

        if (INSTANCE == null) {
            INSTANCE = new DataAccessIngredient(remoteDomainDataAccess, localDomainDataAccess);
        }
        return INSTANCE;
    }
}
