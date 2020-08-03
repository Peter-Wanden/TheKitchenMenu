package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCasePersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DurationModelToDatabaseEntityConverterParent
        implements
        PersistenceModelToDatabaseEntityConverter<RecipeDurationUseCasePersistenceModel, RecipeDurationEntity> {

    @Override
    public RecipeDurationUseCasePersistenceModel convertParentEntityToDomainModel(
            @Nonnull RecipeDurationEntity entity) {
        return new RecipeDurationUseCasePersistenceModel.Builder().
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
            @Nonnull RecipeDurationUseCasePersistenceModel parent) {
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
    public List<RecipeDurationUseCasePersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeDurationEntity> entities) {

        List<RecipeDurationUseCasePersistenceModel> models = new ArrayList<>();
        for (RecipeDurationEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }
}
