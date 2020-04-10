package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class IngredientLocalModelConverter
        implements DomainModelConverter<IngredientPersistenceModel, IngredientEntity> {

    @Override
    public IngredientPersistenceModel convertToModel(
            @Nonnull IngredientEntity e) {
        return new IngredientPersistenceModel.Builder().
                setDataId(e.getDataId()).
                setDomainId(e.getDomainId()).
                setName(e.getName()).
                setDescription(e.getDescription()).
                setConversionFactor(e.getConversionFactor()).
                setCreatedBy(e.getCreatedBy()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLastUpdate()).
                build();
    }

    @Override
    public IngredientEntity convertToPrimitive(
            @Nonnull IngredientPersistenceModel m) {
        return new IngredientEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getName(),
                m.getDescription(),
                m.getConversionFactor(),
                m.getCreatedBy(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }

    @Override
    public List<IngredientPersistenceModel> convertToModels(
            @Nonnull List<IngredientEntity> entities) {
        List<IngredientPersistenceModel> models = new ArrayList<>();
        for (IngredientEntity e : entities) {
            models.add(convertToModel(e));
        }
        return models;
    }
}
