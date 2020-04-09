package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import javax.annotation.Nonnull;

public class DurationLocalSaveAdapter {
    @Nonnull
    private final RecipeDurationLocalDataSource dataSource;

    public DurationLocalSaveAdapter(
            @Nonnull RecipeDurationLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(RecipeDurationPersistenceModel m) {
        DurationConverter c = new DurationConverter();
        dataSource.save(c.convertToPrimitive(m));
    }
}
