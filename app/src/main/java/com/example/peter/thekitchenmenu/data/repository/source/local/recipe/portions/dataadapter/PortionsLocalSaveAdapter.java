package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceDomainModel;

import javax.annotation.Nonnull;

public class PortionsLocalSaveAdapter {

    @Nonnull
    private final RecipePortionsLocalDataSource dataSource;
    @Nonnull
    private final PortionsModelToDatabaseEntityConverterParent converter;

    public PortionsLocalSaveAdapter(@Nonnull RecipePortionsLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new PortionsModelToDatabaseEntityConverterParent();
    }

    public void save(RecipePortionsPersistenceDomainModel model) {
        dataSource.save(converter.convertParentDomainModelToEntity(model));
    }
}
