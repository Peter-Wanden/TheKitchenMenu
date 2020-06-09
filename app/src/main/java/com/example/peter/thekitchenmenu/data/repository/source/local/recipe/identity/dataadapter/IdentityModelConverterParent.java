package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverterParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class IdentityModelConverterParent
        implements
        DomainModelConverterParent<RecipeIdentityPersistenceModel, RecipeIdentityEntity> {

    @Override
    public RecipeIdentityPersistenceModel convertToModelItem(
            @Nonnull RecipeIdentityEntity entity) {
        return new RecipeIdentityPersistenceModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setTitle(entity.getTitle()).
                setDescription(entity.getDescription()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public RecipeIdentityEntity convertToPrimitive(
            @Nonnull RecipeIdentityPersistenceModel parent) {
        return new RecipeIdentityEntity(
                parent.getDataId(),
                parent.getDomainId(),
                parent.getTitle(),
                parent.getDescription(),
                parent.getCreateDate(),
                parent.getLastUpdate()
        );
    }

    @Override
    public List<RecipeIdentityPersistenceModel> convertToModels(
            @Nonnull List<RecipeIdentityEntity> entities) {
        List<RecipeIdentityPersistenceModel> models = new ArrayList<>();
        for (RecipeIdentityEntity e : entities) {
            models.add(convertToModelItem(e));
        }
        return models;
    }
}
