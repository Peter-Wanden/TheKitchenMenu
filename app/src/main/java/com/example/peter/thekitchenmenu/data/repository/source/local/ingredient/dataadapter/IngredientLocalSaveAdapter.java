package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import javax.annotation.Nonnull;

public class IngredientLocalSaveAdapter {

    @Nonnull
    private final IngredientLocalDataSource dataSource;
    @Nonnull
    private final IngredientLocalModelToDatabaseEntityConverterParent converter;

    public IngredientLocalSaveAdapter(@Nonnull IngredientLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new IngredientLocalModelToDatabaseEntityConverterParent();
    }

    public void save(IngredientPersistenceModel model) {
        dataSource.save(converter.convertParentDomainModelToEntity(model));
    }
}
