package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;

import javax.annotation.Nonnull;

public class PortionsLocalDeleteAdapter {

    @Nonnull
    RecipePortionsLocalDataSource dataSource;

    public PortionsLocalDeleteAdapter(@Nonnull RecipePortionsLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteByDataId(@Nonnull String dataId) {
        dataSource.deleteByDataId(dataId);
    }

    public void deleteAllByDomainId(@Nonnull String domainId) {
        dataSource.deleteAllByDomainId(domainId);
    }

    public void deleteAll() {
        dataSource.deleteAll();
    }
}
