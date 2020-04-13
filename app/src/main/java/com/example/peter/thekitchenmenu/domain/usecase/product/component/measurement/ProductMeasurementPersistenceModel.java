package com.example.peter.thekitchenmenu.domain.usecase.product.component.measurement;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;
import com.example.peter.thekitchenmenu.domain.usecase.product.component.measurement.ProductMeasurement.ShelfLife;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;

public class ProductMeasurementPersistenceModel
        extends BasePersistence {

    private String productId;
    private int numberOfItems;
    private double baseUnits;
    private MeasurementSubtype measurementSubtype;
    private ShelfLife shelfLife;
    private long createDate;
    private long lastUpdate;

    private ProductMeasurementPersistenceModel() {
    }

    public String getProductId() {
        return productId;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public double getBaseUnits() {
        return baseUnits;
    }

    public MeasurementSubtype getMeasurementSubtype() {
        return measurementSubtype;
    }

    public ShelfLife getShelfLife() {
        return shelfLife;
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
        ProductMeasurementPersistenceModel that = (ProductMeasurementPersistenceModel) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                productId.equals(that.productId) &&
                shelfLife == that.shelfLife &&
                numberOfItems == that.numberOfItems &&
                measurementSubtype == that.measurementSubtype &&
                Double.compare(that.baseUnits, baseUnits) == 0 &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, numberOfItems, baseUnits, measurementSubtype, shelfLife,
                createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "ProductMeasurementPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", productId='" + productId + '\'' +
                ", shelfLife=" + shelfLife +
                ", measurementSubtype=" + measurementSubtype +
                ", baseUnits=" + baseUnits +
                ", numberOfItems=" + numberOfItems +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends DomainModelBuilder<Builder, ProductMeasurementPersistenceModel> {

        public Builder() {
            model = new ProductMeasurementPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.productId = "";
            model.shelfLife = ShelfLife.OPTION_1;
            model.measurementSubtype = DEFAULT_UNIT_OF_MEASURE.getMeasurementSubtype();
            model.baseUnits = 0.;
            model.numberOfItems = MIN_NUMBER_OF_ITEMS;
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return self();
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            model.domainId = domainId;
            return self();
        }

        public Builder setProductId(String productId) {
            model.productId = productId;
            return self();
        }

        public Builder setShelfLife(ShelfLife shelfLife) {
            model.shelfLife = shelfLife;
            return self();
        }

        public Builder setMeasurementSubType(MeasurementSubtype measurementSubType) {
            model.measurementSubtype = measurementSubType;
            return self();
        }

        public Builder setBaseUnits(double baseUnits) {
            model.baseUnits = baseUnits;
            return self();
        }

        public Builder setNumberOfItems(int numberOfItems) {
            model.numberOfItems = numberOfItems;
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
