package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

public class RecipeIngredientPersistenceModel extends BasePersistence {

    private String dataId;
    private String recipeIngredientId;
    private String recipeDataId;
    private String recipeDomainId;
    private String ingredientDataId;
    private String ingredientDomainId;
    private String productDataId;
    private MeasurementModel measurementModel;
    private String createdBy;
    private long createDate;
    private long lastUpdate;

    private RecipeIngredientPersistenceModel() {}

    @Override
    public String getDataId() {
        return dataId;
    }

    public String getRecipeIngredientId() {
        return recipeIngredientId;
    }

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

    public String getProductDataId() {
        return productDataId;
    }

    public MeasurementModel getMeasurementModel() {
        return measurementModel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public static class Builder extends DomainModelBuilder<
            Builder,
            RecipeIngredientPersistenceModel> {

        public Builder() {
            model = new RecipeIngredientPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.recipeIngredientId = "";
            model.recipeDataId = "";
            model.recipeDomainId = "";
            model.ingredientDataId = "";
            model.ingredientDomainId = "";
            model.productDataId = "";
            model.measurementModel = null;
            model.createdBy = "";
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return self();
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return this;
        }

        public Builder setRecipeIngredientId(String recipeIngredientId) {
            model.recipeIngredientId = recipeIngredientId;
            return self();
        }

        public Builder setRecipeDataId(String recipeDataId) {
            model.recipeDataId = recipeDataId;
            return self();
        }

        public Builder setRecipeDomainId(String recipeDomainId) {
            model.recipeDomainId = recipeDomainId;
            return self();
        }

        public Builder setIngredientDataId(String ingredientDataId) {
            model.ingredientDataId = ingredientDataId;
            return self();
        }

        public Builder setIngredientDomainId(String ingredientDomainId) {
            model.ingredientDomainId = ingredientDomainId;
            return self();
        }

        public Builder setProductDataId(String productId) {
            model.productDataId = productId;
            return self();
        }

        public Builder setMeasurementModel(MeasurementModel measurementModel) {
            model.measurementModel = measurementModel;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            model.createdBy = createdBy;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            model.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            model.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
