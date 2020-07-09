package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeDuration extends Repository<RecipeDurationPersistenceModel> {

    public static RepositoryRecipeDuration INSTANCE = null;

    private RepositoryRecipeDuration(
            @Nonnull DomainDataAccess<RecipeDurationPersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeDurationPersistenceModel> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RepositoryRecipeDuration getInstance(
            DomainDataAccess<RecipeDurationPersistenceModel> remoteDomainDataAccess,
            DomainDataAccess<RecipeDurationPersistenceModel> localDomainDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeDuration(remoteDomainDataAccess, localDomainDataAccess);
        return INSTANCE;
    }
}
