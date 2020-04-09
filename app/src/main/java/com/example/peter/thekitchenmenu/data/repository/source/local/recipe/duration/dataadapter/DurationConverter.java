package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DurationConverter
        implements
        DomainModelConverter<RecipeDurationPersistenceModel, RecipeDurationEntity> {

    @Override
    public RecipeDurationPersistenceModel convertToModel(
            @Nonnull RecipeDurationEntity e) {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId(e.getId()).
                setDomainId(e.getDomainId()).
                setPrepTime(e.getPrepTime()).
                setCookTime(e.getCookTime()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLastUpdate()).
                build();
    }

    @Override
    public RecipeDurationEntity convertToPrimitive(
            @Nonnull RecipeDurationPersistenceModel m) {
        return new RecipeDurationEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getPrepTime(),
                m.getCookTime(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }

    @Override
    public List<RecipeDurationPersistenceModel> convertToModels(
            @Nonnull List<RecipeDurationEntity> entities) {
        List<RecipeDurationPersistenceModel> models = new ArrayList<>();
        for (RecipeDurationEntity e : entities) {
            models.add(convertToModel(e));
        }
        return models;
    }
}
