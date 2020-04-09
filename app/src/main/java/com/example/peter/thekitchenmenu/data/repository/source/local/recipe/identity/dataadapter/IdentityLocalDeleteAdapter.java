package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;

import javax.annotation.Nonnull;

public class IdentityLocalDeleteAdapter {

    @Nonnull
    RecipeIdentityLocalDataSource dataSource;

    public IdentityLocalDeleteAdapter(@Nonnull RecipeIdentityLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteByDataId(@Nonnull String dataId) {
        dataSource.deleteByDataId(dataId);
    }

    public void deleteAllByDomainId(String domainId) {
        dataSource.deleteAllByDomainId(domainId);
    }

    public void deleteAll() {
        dataSource.deleteAll();
    }
}
