package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverterParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class IngredientLocalModelConverterParent
        implements DomainModelConverterParent<IngredientPersistenceModel, IngredientEntity> {

    @Override
    public IngredientPersistenceModel convertToModelItem(
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
    public IngredientEntity convertToPrimitive(
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
    public List<IngredientPersistenceModel> convertToModels(
            @Nonnull List<IngredientEntity> entities) {
        List<IngredientPersistenceModel> models = new ArrayList<>();
        for (IngredientEntity e : entities) {
            models.add(convertToModelItem(e));
        }
        return models;
    }
}
