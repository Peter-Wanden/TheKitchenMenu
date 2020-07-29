package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class IdentityLocalSaveAdapter {
    @Nonnull
    private final RecipeIdentityLocalDataSource dataSource;
    @Nonnull
    private final IdentityModelToDatabaseEntityConverterParent converter;

    public IdentityLocalSaveAdapter(@Nonnull RecipeIdentityLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new IdentityModelToDatabaseEntityConverterParent();
    }

    public void save(RecipeIdentityUseCasePersistenceModel model) {
        dataSource.save(converter.convertParentDomainModelToEntity(model));
    }
}
