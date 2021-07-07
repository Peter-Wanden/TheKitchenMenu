package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

import javax.annotation.Nonnull;

public class RecipeIngredientLocalSaveAdapter {
    @Nonnull
    private final RecipeIngredientLocalDataSource dataSource;
    @Nonnull
    private final RecipeIngredientModelToDatabaseEntityConverterParent converter;

    public RecipeIngredientLocalSaveAdapter(@Nonnull RecipeIngredientLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new RecipeIngredientModelToDatabaseEntityConverterParent();
    }

    public void save(RecipeIngredientPersistenceModel model) {
        dataSource.save(converter.convertParentDomainModelToEntity(model));
    }
}
