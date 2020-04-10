package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIngredientModelConverter
implements DomainModelConverter<RecipeIngredientPersistenceModel, RecipeIngredientEntity> {
    @Override
    public RecipeIngredientPersistenceModel convertToModel(
            @Nonnull RecipeIngredientEntity e) {
        return new RecipeIngredientPersistenceModel.Builder().
                setDataId(e.getDataId()).
                setRecipeIngredientId(e.getRecipeIngredientId()).
                setRecipeDataId(e.getRecipeDataId()).
                setRecipeDomainId(e.getRecipeDomainId()).
                setIngredientDataId(e.getIngredientDataId()).
                setIngredientDomainId(e.getIngredientDomainId()).
                setProductDataId(e.getProductDataId()).
                setMeasurementModel(getMeasurementModel(e)).
                setCreatedBy(e.getCreatedBy()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLastUpdate()).
                build();
    }

    @Override
    public RecipeIngredientEntity convertToPrimitive(
            @Nonnull RecipeIngredientPersistenceModel m) {
        return new RecipeIngredientEntity(
                m.getDataId(),
                m.getRecipeIngredientId(),
                m.getRecipeDataId(),
                m.getRecipeDomainId(),
                m.getIngredientDataId(),
                m.getIngredientDomainId(),
                m.getProductDataId(),
                m.getMeasurementModel().getItemBaseUnits(),
                m.getMeasurementModel().getSubtype().asInt(),
                m.getCreatedBy(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }

    @Override
    public List<RecipeIngredientPersistenceModel> convertToModels(
            @Nonnull List<RecipeIngredientEntity> entities) {
        List<RecipeIngredientPersistenceModel> models = new ArrayList<>();
        for (RecipeIngredientEntity e : entities) {
            models.add(convertToModel(e));
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
