package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.model.BasePersistenceDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient.NO_ID;

public final class IngredientPersistenceDomainModel
        extends BasePersistenceDomainModel {

    private String name;
    private String description;
    private double conversionFactor;
    private String createdBy;

    private IngredientPersistenceDomainModel() {
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
        if (!(o instanceof IngredientPersistenceDomainModel)) return false;
        if (!super.equals(o)) return false;
        IngredientPersistenceDomainModel that = (IngredientPersistenceDomainModel) o;
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
            PersistenceModelBuilder<Builder, IngredientPersistenceDomainModel> {

        public Builder() {
            persistenceModel = new IngredientPersistenceDomainModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = NO_ID;
            persistenceModel.name = "";
            persistenceModel.description = "";
            persistenceModel.conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
            persistenceModel.createdBy = Constants.getUserId();
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull IngredientPersistenceDomainModel m) {
            persistenceModel.dataId = m.getDataId();
            persistenceModel.domainId = m.getDomainId();
            persistenceModel.name = m.getName();
            persistenceModel.description = m.getDescription();
            persistenceModel.conversionFactor = m.getConversionFactor();
            persistenceModel.createdBy = m.getCreatedBy();
            persistenceModel.createDate = m.getCreateDate();
            persistenceModel.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setName(String name) {
            persistenceModel.name = name;
            return self();
        }

        public Builder setDescription(String description) {
            persistenceModel.description = description;
            return self();
        }

        public Builder setConversionFactor(double conversionFactor) {
            persistenceModel.conversionFactor = conversionFactor;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            persistenceModel.createdBy = createdBy;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
