package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import javax.annotation.Nonnull;

public class DataAccessRecipeDuration extends DataAccess<RecipeDurationPersistenceModel> {

    public static DataAccessRecipeDuration INSTANCE = null;

    private DataAccessRecipeDuration(
            @Nonnull DomainDataAccess<RecipeDurationPersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeDurationPersistenceModel> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static DataAccessRecipeDuration getInstance(
            DomainDataAccess<RecipeDurationPersistenceModel> remoteDomainDataAccess,
            DomainDataAccess<RecipeDurationPersistenceModel> localDomainDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new DataAccessRecipeDuration(remoteDomainDataAccess, localDomainDataAccess);
        return INSTANCE;
    }
}
