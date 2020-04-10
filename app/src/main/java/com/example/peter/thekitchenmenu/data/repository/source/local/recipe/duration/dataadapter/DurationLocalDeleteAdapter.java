package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;

import javax.annotation.Nonnull;

public class DurationLocalDeleteAdapter {

    @Nonnull
    private final RecipeDurationLocalDataSource dataSource;

    public DurationLocalDeleteAdapter(@Nonnull RecipeDurationLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteByDataId(@Nonnull String dataId) {
        dataSource.deleteByDataId(dataId);
    }

    public void deleteAllByDomainId(@Nonnull String domainId){
        dataSource.deleteAllByDomainId(domainId);
    }

    public void deleteAll() {
        dataSource.deleteAll();
    }
}
