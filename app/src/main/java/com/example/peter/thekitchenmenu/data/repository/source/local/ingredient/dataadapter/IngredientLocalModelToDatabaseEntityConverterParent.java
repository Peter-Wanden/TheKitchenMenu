package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class IngredientLocalModelToDatabaseEntityConverterParent
        implements PersistenceModelToDatabaseEntityConverter<IngredientPersistenceModel, IngredientEntity> {

    @Override
    public IngredientPersistenceModel convertParentEntityToDomainModel(
            @Nonnull IngredientEntity entity) {
        return new IngredientPersistenceModel.Builder().
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
            @Nonnull IngredientPersistenceModel parent) {
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
    public List<IngredientPersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<IngredientEntity> entities) {
        List<IngredientPersistenceModel> models = new ArrayList<>();
        for (IngredientEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }
}
