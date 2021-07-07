package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;

import javax.annotation.Nonnull;

public class IngredientLocalDeleteAdapter {

    @Nonnull
    private final IngredientLocalDataSource dataSource;

    public IngredientLocalDeleteAdapter(@Nonnull IngredientLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteByDataId(String dataId) {
        dataSource.deleteByDataId(dataId);
    }

    public void deleteAllByDomainId(String domainId) {
        dataSource.deleteAllByDomainId(domainId);
    }

    public void deleteAll() {
        dataSource.deleteAll();
    }
}
