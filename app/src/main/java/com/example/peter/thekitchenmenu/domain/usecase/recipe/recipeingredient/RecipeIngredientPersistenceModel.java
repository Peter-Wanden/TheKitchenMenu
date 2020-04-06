package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.usecase.PersistenceBase;

public class RecipeIngredientPersistenceModel extends PersistenceBase {

    private String recipeDataId;
    private String recipeDomainId;
    private String ingredientDataId;
    private String ingredientDomainId;
    private String productId;
    private MeasurementModel measurementModel;
    private String createdBy;
    private long lastUpdate;

    public String getRecipeDataId() {
        return recipeDataId;
    }

    public String getRecipeDomainId() {
        return recipeDomainId;
    }

    public String getIngredientDataId() {
        return ingredientDataId;
    }

    public String getIngredientDomainId() {
        return ingredientDomainId;
    }

    public String getProductId() {
        return productId;
    }

    public MeasurementModel getMeasurementModel() {
        return measurementModel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
