package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCasePersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class IdentityModelToDatabaseEntityConverterParent
        implements
        PersistenceModelToDatabaseEntityConverter<RecipeIdentityUseCasePersistenceModel, RecipeIdentityEntity> {

    @Override
    public RecipeIdentityUseCasePersistenceModel convertParentEntityToDomainModel(
            @Nonnull RecipeIdentityEntity entity) {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setTitle(entity.getTitle()).
                setDescription(entity.getDescription()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public RecipeIdentityEntity convertParentDomainModelToEntity(
            @Nonnull RecipeIdentityUseCasePersistenceModel parent) {
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
    public List<RecipeIdentityUseCasePersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeIdentityEntity> entities) {
        List<RecipeIdentityUseCasePersistenceModel> models = new ArrayList<>();
        for (RecipeIdentityEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }
}
