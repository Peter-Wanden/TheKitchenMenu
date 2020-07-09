package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DurationModelToDatabaseEntityConverterParent
        implements
        PersistenceModelToDatabaseEntityConverter<RecipeDurationPersistenceModel, RecipeDurationEntity> {

    @Override
    public RecipeDurationPersistenceModel convertParentEntityToDomainModel(
            @Nonnull RecipeDurationEntity entity) {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setPrepTime(entity.getPrepTime()).
                setCookTime(entity.getCookTime()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public RecipeDurationEntity convertParentDomainModelToEntity(
            @Nonnull RecipeDurationPersistenceModel parent) {
        return new RecipeDurationEntity(
                parent.getDataId(),
                parent.getDomainId(),
                parent.getPrepTime(),
                parent.getCookTime(),
                parent.getCreateDate(),
                parent.getLastUpdate()
        );
    }

    @Override
    public List<RecipeDurationPersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeDurationEntity> entities) {

        List<RecipeDurationPersistenceModel> models = new ArrayList<>();
        for (RecipeDurationEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }
}
