package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.model.BasePersistenceDomainModel;

public class RecipeIngredientPersistenceDomainModel extends BasePersistenceDomainModel {

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

    private RecipeIngredientPersistenceDomainModel() {}

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

    public static class Builder
            extends
            PersistenceModelBuilder<Builder,
                    RecipeIngredientPersistenceDomainModel> {

        public Builder() {
            persistenceModel = new RecipeIngredientPersistenceDomainModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.recipeIngredientId = "";
            persistenceModel.recipeDataId = "";
            persistenceModel.recipeDomainId = "";
            persistenceModel.ingredientDataId = "";
            persistenceModel.ingredientDomainId = "";
            persistenceModel.productDataId = "";
            persistenceModel.measurementModel = null;
            persistenceModel.createdBy = "";
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeIngredientPersistenceDomainModel model) {
            persistenceModel.dataId = model.getDataId();
            persistenceModel.recipeIngredientId = model.getDomainId();
            persistenceModel.recipeDataId = model.getRecipeDataId();
            persistenceModel.recipeDomainId = model.getRecipeDomainId();
            persistenceModel.ingredientDataId = model.getIngredientDataId();
            persistenceModel.ingredientDomainId = model.getRecipeDomainId();
            persistenceModel.productDataId = model.getProductDataId();
            persistenceModel.measurementModel = model.getMeasurementModel();
            persistenceModel.createdBy = model.getCreatedBy();
            persistenceModel.createDate = model.getCreateDate();
            persistenceModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setDataId(String dataId) {
            persistenceModel.dataId = dataId;
            return this;
        }

        public Builder setRecipeIngredientId(String recipeIngredientId) {
            persistenceModel.recipeIngredientId = recipeIngredientId;
            return self();
        }

        public Builder setRecipeDataId(String recipeDataId) {
            persistenceModel.recipeDataId = recipeDataId;
            return self();
        }

        public Builder setRecipeDomainId(String recipeDomainId) {
            persistenceModel.recipeDomainId = recipeDomainId;
            return self();
        }

        public Builder setIngredientDataId(String ingredientDataId) {
            persistenceModel.ingredientDataId = ingredientDataId;
            return self();
        }

        public Builder setIngredientDomainId(String ingredientDomainId) {
            persistenceModel.ingredientDomainId = ingredientDomainId;
            return self();
        }

        public Builder setProductDataId(String productId) {
            persistenceModel.productDataId = productId;
            return self();
        }

        public Builder setMeasurementModel(MeasurementModel measurementModel) {
            persistenceModel.measurementModel = measurementModel;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            persistenceModel.createdBy = createdBy;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            persistenceModel.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            persistenceModel.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
