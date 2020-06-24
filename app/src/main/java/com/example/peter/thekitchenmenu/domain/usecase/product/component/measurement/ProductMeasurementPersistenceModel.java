package com.example.peter.thekitchenmenu.domain.usecase.product.component.measurement;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.product.component.measurement.ProductMeasurement.ShelfLife;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;

public class ProductMeasurementPersistenceModel
        extends BaseDomainPersistenceModel {

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
            domainModel = new ProductMeasurementPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.productId = "";
            domainModel.shelfLife = ShelfLife.OPTION_1;
            domainModel.measurementSubtype = DEFAULT_UNIT_OF_MEASURE.getMeasurementSubtype();
            domainModel.baseUnits = 0.;
            domainModel.numberOfItems = MIN_NUMBER_OF_ITEMS;
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(ProductMeasurementPersistenceModel model) {
            domainModel.dataId = model.getDataId();
            domainModel.domainId = model.getDomainId();
            domainModel.productId = model.getProductId();
            domainModel.shelfLife = model.getShelfLife();
            domainModel.measurementSubtype = model.getMeasurementSubtype();
            domainModel.baseUnits = model.getBaseUnits();
            domainModel.numberOfItems = model.getNumberOfItems();
            domainModel.createDate = model.getCreateDate();
            domainModel.lastUpdate = model.getLastUpdate();

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

        public Builder setProductId(String productId) {
            domainModel.productId = productId;
            return self();
        }

        public Builder setShelfLife(ShelfLife shelfLife) {
            domainModel.shelfLife = shelfLife;
            return self();
        }

        public Builder setMeasurementSubType(MeasurementSubtype measurementSubType) {
            domainModel.measurementSubtype = measurementSubType;
            return self();
        }

        public Builder setBaseUnits(double baseUnits) {
            domainModel.baseUnits = baseUnits;
            return self();
        }

        public Builder setNumberOfItems(int numberOfItems) {
            domainModel.numberOfItems = numberOfItems;
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
