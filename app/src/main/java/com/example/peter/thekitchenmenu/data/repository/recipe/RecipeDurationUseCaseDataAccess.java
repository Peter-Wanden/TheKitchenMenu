package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class RecipeDurationUseCaseDataAccess extends DataAccess<RecipeDurationUseCasePersistenceModel> {

    public static RecipeDurationUseCaseDataAccess INSTANCE = null;

    private RecipeDurationUseCaseDataAccess(
            @Nonnull DomainDataAccess<RecipeDurationUseCasePersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeDurationUseCasePersistenceModel> localDomainDataAccess) {
        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RecipeDurationUseCaseDataAccess getInstance(
            DomainDataAccess<RecipeDurationUseCasePersistenceModel> remoteDomainDataAccess,
            DomainDataAccess<RecipeDurationUseCasePersistenceModel> localDomainDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RecipeDurationUseCaseDataAccess(remoteDomainDataAccess, localDomainDataAccess);
        return INSTANCE;
    }
}
