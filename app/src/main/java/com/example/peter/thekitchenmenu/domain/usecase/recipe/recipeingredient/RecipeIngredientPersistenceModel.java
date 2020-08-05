package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient;

import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainPersistenceModel;

public class RecipeIngredientPersistenceModel extends BaseDomainPersistenceModel {

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

    public static class Builder
            extends
            PersistenceModelBuilder<Builder,
                    RecipeIngredientPersistenceModel> {

        public Builder() {
            super(new RecipeIngredientPersistenceModel());
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.recipeIngredientId = "";
            domainModel.recipeDataId = "";
            domainModel.recipeDomainId = "";
            domainModel.ingredientDataId = "";
            domainModel.ingredientDomainId = "";
            domainModel.productDataId = "";
            domainModel.measurementModel = null;
            domainModel.createdBy = "";
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeIngredientPersistenceModel model) {
            domainModel.dataId = model.getDataId();
            domainModel.recipeIngredientId = model.getDomainId();
            domainModel.recipeDataId = model.getRecipeDataId();
            domainModel.recipeDomainId = model.getRecipeDomainId();
            domainModel.ingredientDataId = model.getIngredientDataId();
            domainModel.ingredientDomainId = model.getRecipeDomainId();
            domainModel.productDataId = model.getProductDataId();
            domainModel.measurementModel = model.getMeasurementModel();
            domainModel.createdBy = model.getCreatedBy();
            domainModel.createDate = model.getCreateDate();
            domainModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setDataId(String dataId) {
            domainModel.dataId = dataId;
            return this;
        }

        public Builder setRecipeIngredientId(String recipeIngredientId) {
            domainModel.recipeIngredientId = recipeIngredientId;
            return self();
        }

        public Builder setRecipeDataId(String recipeDataId) {
            domainModel.recipeDataId = recipeDataId;
            return self();
        }

        public Builder setRecipeDomainId(String recipeDomainId) {
            domainModel.recipeDomainId = recipeDomainId;
            return self();
        }

        public Builder setIngredientDataId(String ingredientDataId) {
            domainModel.ingredientDataId = ingredientDataId;
            return self();
        }

        public Builder setIngredientDomainId(String ingredientDomainId) {
            domainModel.ingredientDomainId = ingredientDomainId;
            return self();
        }

        public Builder setProductDataId(String productId) {
            domainModel.productDataId = productId;
            return self();
        }

        public Builder setMeasurementModel(MeasurementModel measurementModel) {
            domainModel.measurementModel = measurementModel;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            domainModel.createdBy = createdBy;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            domainModel.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            domainModel.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
