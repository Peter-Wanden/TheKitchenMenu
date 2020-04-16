package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverterParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class PortionsModelConverterParent
        implements
        DomainModelConverterParent<RecipePortionsPersistenceModel, RecipePortionsEntity> {

    @Override
    public RecipePortionsPersistenceModel convertToModel(
            @Nonnull RecipePortionsEntity e) {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId(e.getDataId()).
                setDomainId(e.getDomainId()).
                setServings(e.getServings()).
                setSittings(e.getSittings()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLastUpdate()).
                build();
    }

    @Override
    public RecipePortionsEntity convertToPrimitive(
            @Nonnull RecipePortionsPersistenceModel m) {
        return new RecipePortionsEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getServings(),
                m.getSittings(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }

    @Override
    public List<RecipePortionsPersistenceModel> convertToModels(
            @Nonnull List<RecipePortionsEntity> es) {
        List<RecipePortionsPersistenceModel> models = new ArrayList<>();
        for (RecipePortionsEntity e : es) {
            models.add(convertToModel(e));
        }
        return models;
    }
}
