package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeDuration extends Repository<RecipeDurationPersistenceDomainModel> {

    public static RepositoryRecipeDuration INSTANCE = null;

    private RepositoryRecipeDuration(
            @Nonnull DomainDataAccess<RecipeDurationPersistenceDomainModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeDurationPersistenceDomainModel> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RepositoryRecipeDuration getInstance(
            DomainDataAccess<RecipeDurationPersistenceDomainModel> remoteDomainDataAccess,
            DomainDataAccess<RecipeDurationPersistenceDomainModel> localDomainDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeDuration(remoteDomainDataAccess, localDomainDataAccess);
        return INSTANCE;
    }
}
