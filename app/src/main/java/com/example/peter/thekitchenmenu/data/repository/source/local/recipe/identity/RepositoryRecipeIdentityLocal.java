package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentityLocal
        implements DomainDataAccess<RecipeIdentityPersistenceModel> {

    private static volatile RepositoryRecipeIdentityLocal INSTANCE;

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceModel> callback) {

    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceModel> callback) {

    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceModel> callback) {

    }

    @Override
    public void save(@Nonnull RecipeIdentityPersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
