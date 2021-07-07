package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class DurationLocalSaveAdapter {

    @Nonnull
    private final RecipeDurationLocalDataSource dataSource;
    @Nonnull
    private final DurationModelToDatabaseEntityConverterParent converter;

    public DurationLocalSaveAdapter(@Nonnull RecipeDurationLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new DurationModelToDatabaseEntityConverterParent();
    }

    public void save(RecipeDurationUseCasePersistenceModel model) {
        dataSource.save(converter.convertParentDomainModelToEntity(model));
    }
}
