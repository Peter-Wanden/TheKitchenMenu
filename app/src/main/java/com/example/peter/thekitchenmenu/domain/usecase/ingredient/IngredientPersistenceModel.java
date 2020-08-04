package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainPersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient.NO_ID;

public final class IngredientPersistenceModel
        extends BaseDomainPersistenceModel {

    private String name;
    private String description;
    private double conversionFactor;
    private String createdBy;

    private IngredientPersistenceModel() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientPersistenceModel)) return false;
        if (!super.equals(o)) return false;
        IngredientPersistenceModel that = (IngredientPersistenceModel) o;
        return Double.compare(that.conversionFactor, conversionFactor) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, conversionFactor, createdBy);
    }

    @Nonnull
    @Override
    public String toString() {
        return "IngredientPersistenceModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conversionFactor=" + conversionFactor +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, IngredientPersistenceModel> {

        public Builder() {
            domainModel = new IngredientPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = NO_ID;
            domainModel.name = "";
            domainModel.description = "";
            domainModel.conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
            domainModel.createdBy = Constants.getUserId();
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull IngredientPersistenceModel m) {
            domainModel.dataId = m.getDataId();
            domainModel.domainId = m.getDomainId();
            domainModel.name = m.getName();
            domainModel.description = m.getDescription();
            domainModel.conversionFactor = m.getConversionFactor();
            domainModel.createdBy = m.getCreatedBy();
            domainModel.createDate = m.getCreateDate();
            domainModel.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setName(String name) {
            domainModel.name = name;
            return self();
        }

        public Builder setDescription(String description) {
            domainModel.description = description;
            return self();
        }

        public Builder setConversionFactor(double conversionFactor) {
            domainModel.conversionFactor = conversionFactor;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            domainModel.createdBy = createdBy;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
