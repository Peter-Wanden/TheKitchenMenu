package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientLocalDataSource;

import javax.annotation.Nonnull;

public class RecipeIngredientLocalDeleteAdapter {
    @Nonnull
    private final RecipeIngredientLocalDataSource dataSource;

    public RecipeIngredientLocalDeleteAdapter(@Nonnull RecipeIngredientLocalDataSource dataSource) {
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
