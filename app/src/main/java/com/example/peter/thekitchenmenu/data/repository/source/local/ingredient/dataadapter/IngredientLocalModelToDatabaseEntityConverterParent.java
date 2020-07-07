package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class IngredientLocalModelToDatabaseEntityConverterParent
        implements PersistenceModelToDatabaseEntityConverter<IngredientPersistenceDomainModel, IngredientEntity> {

    @Override
    public IngredientPersistenceDomainModel convertParentEntityToDomainModel(
            @Nonnull IngredientEntity entity) {
        return new IngredientPersistenceDomainModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setName(entity.getName()).
                setDescription(entity.getDescription()).
                setConversionFactor(entity.getConversionFactor()).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public IngredientEntity convertParentDomainModelToEntity(
            @Nonnull IngredientPersistenceDomainModel parent) {
        return new IngredientEntity(
                parent.getDataId(),
                parent.getDomainId(),
                parent.getName(),
                parent.getDescription(),
                parent.getConversionFactor(),
                parent.getCreatedBy(),
                parent.getCreateDate(),
                parent.getLastUpdate()
        );
    }

    @Override
    public List<IngredientPersistenceDomainModel> convertParentEntitiesToDomainModels(
            @Nonnull List<IngredientEntity> entities) {
        List<IngredientPersistenceDomainModel> models = new ArrayList<>();
        for (IngredientEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }
}
