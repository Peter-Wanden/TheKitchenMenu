package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient.CREATE_NEW_INGREDIENT;

public final class IngredientPersistenceModel
        extends BasePersistence {

    private String name;
    private String description;
    private double conversionFactor;
    private String createdBy;
    private long createDate;
    private long lastUpdate;

    private IngredientPersistenceModel() {}

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

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPersistenceModel that = (IngredientPersistenceModel) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                Double.compare(that.conversionFactor, conversionFactor) == 0 &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, name, description, conversionFactor, createdBy,
                createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "IngredientPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conversionFactor=" + conversionFactor +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends DomainModelBuilder<Builder, IngredientPersistenceModel> {

        public Builder() {
            domainModel = new IngredientPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = CREATE_NEW_INGREDIENT;;
            domainModel.name = "";
            domainModel.description = "";
            domainModel.conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
            domainModel.createdBy = Constants.getUserId();
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

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

        public Builder setDataId(String dataId) {
            domainModel.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            domainModel.domainId = domainId;
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
