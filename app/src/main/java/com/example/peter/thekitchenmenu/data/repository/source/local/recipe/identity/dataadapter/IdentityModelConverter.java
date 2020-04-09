package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class IdentityModelConverter
        implements
        DomainModelConverter<RecipeIdentityPersistenceModel, RecipeIdentityEntity> {

    @Override
    public RecipeIdentityPersistenceModel convertToModel(
            @Nonnull RecipeIdentityEntity e) {
        return new RecipeIdentityPersistenceModel.Builder().
                setDataId(e.getDataId()).
                setDomainId(e.getDomainId()).
                setTitle(e.getTitle()).
                setDescription(e.getDescription()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLastUpdate()).
                build();
    }

    @Override
    public RecipeIdentityEntity convertToPrimitive(
            @Nonnull RecipeIdentityPersistenceModel m) {
        return new RecipeIdentityEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getTitle(),
                m.getDescription(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }

    @Override
    public List<RecipeIdentityPersistenceModel> convertToModels(
            @Nonnull List<RecipeIdentityEntity> es) {
        List<RecipeIdentityPersistenceModel> models = new ArrayList<>();
        for (RecipeIdentityEntity e : es) {
            models.add(convertToModel(e));
        }
        return models;
    }
}
