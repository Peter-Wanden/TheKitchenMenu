package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient.CREATE_NEW_INGREDIENT;

public final class IngredientPersistenceModel extends BasePersistence {

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

    public static class Builder extends DomainModelBuilder<Builder, IngredientPersistenceModel> {

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.domainId = CREATE_NEW_INGREDIENT;;
            model.name = "";
            model.description = "";
            model.conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
            model.createdBy = Constants.getUserId();
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return self();
        }

        public Builder basedOnPersistenceModel(@Nonnull IngredientPersistenceModel m) {
            return new Builder().
                    setDataId(m.getDataId()).
                    setDomainId(m.getDomainId()).
                    setName(m.getName()).
                    setDescription(m.getDescription()).
                    setConversionFactor(m.getConversionFactor()).
                    setCreatedBy(m.getCreatedBy()).
                    setCreateDate(m.getCreateDate()).
                    setLastUpdate(m.getLastUpdate());
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            model.domainId = domainId;
            return self();
        }

        public Builder setName(String name) {
            model.name = name;
            return self();
        }

        public Builder setDescription(String description) {
            model.description = description;
            return self();
        }

        public Builder setConversionFactor(double conversionFactor) {
            model.conversionFactor = conversionFactor;
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
