package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;

import java.util.Objects;

public class MeasurementModel {

    @NonNull
    private final MeasurementSubtype subtype;
    private final int numberOfItems;
    private final double conversionFactor;
    private final double totalMeasurementOne;
    private final int totalMeasurementTwo;
    private final double itemMeasurementOne;
    private final int itemMeasurementTwo;
    private final double itemBaseUnits;

    public MeasurementModel(@NonNull MeasurementSubtype subtype,
                            int numberOfItems,
                            double conversionFactor,
                            double totalMeasurementOne,
                            int totalMeasurementTwo,
                            double itemMeasurementOne,
                            int itemMeasurementTwo,
                            double itemBaseUnits) {
        this.subtype = subtype;
        this.numberOfItems = numberOfItems;
        this.conversionFactor = conversionFactor;
        this.totalMeasurementOne = totalMeasurementOne;
        this.totalMeasurementTwo = totalMeasurementTwo;
        this.itemMeasurementOne = itemMeasurementOne;
        this.itemMeasurementTwo = itemMeasurementTwo;
        this.itemBaseUnits = itemBaseUnits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementModel that = (MeasurementModel) o;
        return numberOfItems == that.numberOfItems &&
                Double.compare(that.conversionFactor, conversionFactor) == 0 &&
                Double.compare(that.totalMeasurementOne, totalMeasurementOne) == 0 &&
                totalMeasurementTwo == that.totalMeasurementTwo &&
                Double.compare(that.itemMeasurementOne, itemMeasurementOne) == 0 &&
                itemMeasurementTwo == that.itemMeasurementTwo &&
                Double.compare(that.itemBaseUnits, itemBaseUnits) == 0 &&
                subtype == that.subtype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtype,
                numberOfItems,
                conversionFactor,
                totalMeasurementOne,
                totalMeasurementTwo,
                itemMeasurementOne,
                itemMeasurementTwo,
                itemBaseUnits);
    }

    @NonNull
    @Override
    public String toString() {
        return "MeasurementModel{" +
                "subtype=" + subtype +
                ", numberOfItems=" + numberOfItems +
                ", conversionFactor=" + conversionFactor +
                ", totalMeasurementOne=" + totalMeasurementOne +
                ", totalMeasurementTwo=" + totalMeasurementTwo +
                ", itemMeasurementOne=" + itemMeasurementOne +
                ", itemMeasurementTwo=" + itemMeasurementTwo +
                ", itemBaseUnits=" + itemBaseUnits +
                '}';
    }

    @NonNull
    public MeasurementSubtype getSubtype() {
        return subtype;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double getTotalMeasurementOne() {
        return totalMeasurementOne;
    }

    public int getTotalMeasurementTwo() {
        return totalMeasurementTwo;
    }

    public double getItemMeasurementOne() {
        return itemMeasurementOne;
    }

    public int getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    public double getItemBaseUnits() {
        return itemBaseUnits;
    }
}
