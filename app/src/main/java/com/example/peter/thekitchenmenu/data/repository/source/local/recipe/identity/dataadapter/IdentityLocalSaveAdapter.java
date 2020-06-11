package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import javax.annotation.Nonnull;

public class IdentityLocalSaveAdapter {
    @Nonnull
    private final RecipeIdentityLocalDataSource dataSource;
    @Nonnull
    private final IdentityModelConverterParent converter;

    public IdentityLocalSaveAdapter(@Nonnull RecipeIdentityLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new IdentityModelConverterParent();
    }

    public void save(RecipeIdentityPersistenceModel model) {
        dataSource.save(converter.convertParentDomainModelToEntity(model));
    }
}
