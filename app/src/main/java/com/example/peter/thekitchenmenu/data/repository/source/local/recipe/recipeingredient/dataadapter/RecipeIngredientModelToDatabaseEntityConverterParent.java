package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIngredientModelToDatabaseEntityConverterParent
implements PersistenceModelToDatabaseEntityConverter<RecipeIngredientPersistenceModel, RecipeIngredientEntity> {
    @Override
    public RecipeIngredientPersistenceModel convertParentEntityToDomainModel(
            @Nonnull RecipeIngredientEntity entity) {
        return new RecipeIngredientPersistenceModel.Builder().
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
            @Nonnull RecipeIngredientPersistenceModel parent) {
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
    public List<RecipeIngredientPersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeIngredientEntity> entities) {
        List<RecipeIngredientPersistenceModel> models = new ArrayList<>();
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
