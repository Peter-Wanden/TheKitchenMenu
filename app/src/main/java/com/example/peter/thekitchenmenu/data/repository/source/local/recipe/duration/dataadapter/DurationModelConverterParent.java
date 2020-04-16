package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverterParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DurationModelConverterParent
        implements
        DomainModelConverterParent<RecipeDurationPersistenceModel, RecipeDurationEntity> {

    @Override
    public RecipeDurationPersistenceModel convertToModel(
            @Nonnull RecipeDurationEntity e) {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId(e.getDataId()).
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
            @Nonnull List<RecipeDurationEntity> es) {
        List<RecipeDurationPersistenceModel> models = new ArrayList<>();
        for (RecipeDurationEntity e : es) {
            models.add(convertToModel(e));
        }
        return models;
    }
}
