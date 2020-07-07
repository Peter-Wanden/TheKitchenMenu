package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIngredientModelToDatabaseEntityConverterParent
implements PersistenceModelToDatabaseEntityConverter<RecipeIngredientPersistenceDomainModel, RecipeIngredientEntity> {
    @Override
    public RecipeIngredientPersistenceDomainModel convertParentEntityToDomainModel(
            @Nonnull RecipeIngredientEntity entity) {
        return new RecipeIngredientPersistenceDomainModel.Builder().
                setDataId(entity.getDataId()).
                setRecipeIngredientId(entity.getRecipeIngredientId()).
                setRecipeDataId(entity.getRecipeDataId()).
                setRecipeDomainId(entity.getRecipeDomainId()).
                setIngredientDataId(entity.getIngredientDataId()).
                setIngredientDomainId(entity.getIngredientDomainId()).
                setProductDataId(entity.getProductDataId()).
                setMeasurementModel(getMeasurementModel(entity)).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public RecipeIngredientEntity convertParentDomainModelToEntity(
            @Nonnull RecipeIngredientPersistenceDomainModel parent) {
        return new RecipeIngredientEntity(
                parent.getDataId(),
                parent.getRecipeIngredientId(),
                parent.getRecipeDataId(),
                parent.getRecipeDomainId(),
                parent.getIngredientDataId(),
                parent.getIngredientDomainId(),
                parent.getProductDataId(),
                parent.getMeasurementModel().getItemBaseUnits(),
                parent.getMeasurementModel().getSubtype().asInt(),
                parent.getCreatedBy(),
                parent.getCreateDate(),
                parent.getLastUpdate()
        );
    }

    @Override
    public List<RecipeIngredientPersistenceDomainModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeIngredientEntity> entities) {
        List<RecipeIngredientPersistenceDomainModel> models = new ArrayList<>();
        for (RecipeIngredientEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }

    private MeasurementModel getMeasurementModel(RecipeIngredientEntity e) {

        // TODO - Redo when rewrite UnitOfMeasure
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.fromInt(e.
                getMeasurementSubtype()).
                getMeasurementClass();
        unitOfMeasure.isItemBaseUnitsSet(e.getItemBaseUnits());

        return MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).build();
    }
}
