package com.example.peter.thekitchenmenu.domain.usecase.product.component.measurement;

import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.model.BasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.product.component.measurement.ProductMeasurement.ShelfLife;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.*;

public class ProductMeasurementPersistenceModel
        extends BasePersistenceModel {

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
            extends
            PersistenceModelBuilder<Builder, ProductMeasurementPersistenceModel> {

        public Builder() {
            persistenceModel = new ProductMeasurementPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.productId = "";
            persistenceModel.shelfLife = ShelfLife.OPTION_1;
            persistenceModel.measurementSubtype = DEFAULT_UNIT_OF_MEASURE.getMeasurementSubtype();
            persistenceModel.baseUnits = 0.;
            persistenceModel.numberOfItems = MIN_NUMBER_OF_ITEMS;
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(ProductMeasurementPersistenceModel model) {
            persistenceModel.dataId = model.getDataId();
            persistenceModel.domainId = model.getDomainId();
            persistenceModel.productId = model.getProductId();
            persistenceModel.shelfLife = model.getShelfLife();
            persistenceModel.measurementSubtype = model.getMeasurementSubtype();
            persistenceModel.baseUnits = model.getBaseUnits();
            persistenceModel.numberOfItems = model.getNumberOfItems();
            persistenceModel.createDate = model.getCreateDate();
            persistenceModel.lastUpdate = model.getLastUpdate();

            return self();
        }

        public Builder setDataId(String dataId) {
            persistenceModel.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            persistenceModel.domainId = domainId;
            return self();
        }

        public Builder setProductId(String productId) {
            persistenceModel.productId = productId;
            return self();
        }

        public Builder setShelfLife(ShelfLife shelfLife) {
            persistenceModel.shelfLife = shelfLife;
            return self();
        }

        public Builder setMeasurementSubType(MeasurementSubtype measurementSubType) {
            persistenceModel.measurementSubtype = measurementSubType;
            return self();
        }

        public Builder setBaseUnits(double baseUnits) {
            persistenceModel.baseUnits = baseUnits;
            return self();
        }

        public Builder setNumberOfItems(int numberOfItems) {
            persistenceModel.numberOfItems = numberOfItems;
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
