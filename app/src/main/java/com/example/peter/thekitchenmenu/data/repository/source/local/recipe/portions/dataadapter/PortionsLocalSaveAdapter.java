package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import javax.annotation.Nonnull;

public class PortionsLocalSaveAdapter {

    @Nonnull
    private final RecipePortionsLocalDataSource dataSource;
    @Nonnull
    private final PortionsModelConverterParent converter;

    public PortionsLocalSaveAdapter(@Nonnull RecipePortionsLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new PortionsModelConverterParent();
    }

    public void save(RecipePortionsPersistenceModel model) {
        dataSource.save(converter.convertToPrimitive(model));
    }
}
