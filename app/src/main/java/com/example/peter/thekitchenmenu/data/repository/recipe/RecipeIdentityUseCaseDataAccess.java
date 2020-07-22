package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class RecipeIdentityUseCaseDataAccess extends DataAccess<RecipeIdentityUseCasePersistenceModel> {

    public static RecipeIdentityUseCaseDataAccess INSTANCE = null;

    private RecipeIdentityUseCaseDataAccess(
            @Nonnull DomainDataAccess<RecipeIdentityUseCasePersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeIdentityUseCasePersistenceModel> localDomainDataAccess) {

        this.remoteDomainDataAccess = remoteDomainDataAccess;
        this.localDomainDataAccess = localDomainDataAccess;
    }

    public static RecipeIdentityUseCaseDataAccess getInstance(
            @Nonnull DomainDataAccess<RecipeIdentityUseCasePersistenceModel> remoteDomainDataAccess,
            @Nonnull DomainDataAccess<RecipeIdentityUseCasePersistenceModel> localDomainDataAccess) {

        if (INSTANCE == null) {
            INSTANCE = new RecipeIdentityUseCaseDataAccess(remoteDomainDataAccess, localDomainDataAccess);
        }
        return INSTANCE;
    }
}
