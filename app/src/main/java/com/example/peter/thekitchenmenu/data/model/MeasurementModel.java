package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementType;

import java.util.Arrays;
import java.util.Objects;

public final class MeasurementModel {

    @NonNull
    private final MeasurementType type;
    @NonNull
    private final MeasurementSubtype subtype;

    private final int numberOfMeasurementUnits;

    private final boolean isConversionFactorEnabled;
    private final double conversionFactor;

    private final double itemBaseUnits;
    private final double totalBaseUnits;

    private final int numberOfItems;

    private final double totalMeasurementOne;
    private final double itemMeasurementOne;

    private final int totalMeasurementTwo;
    private final int itemMeasurementTwo;

    private final boolean isValidMeasurement;
    // Min measurement is displayed as a single unit
    private final double minimumMeasurement;
    // Max measurement is displayed as two units
    private final double maxMeasurementOne;
    private final int maxMeasurementTwo;

    private final Pair[] measurementUnitDigitWidths;

    public MeasurementModel(@NonNull MeasurementType type,
                            @NonNull MeasurementSubtype subtype,
                            int numberOfMeasurementUnits,
                            boolean isConversionFactorEnabled,
                            double conversionFactor,
                            double itemBaseUnits,
                            double totalBaseUnits,
                            int numberOfItems,
                            double totalMeasurementOne,
                            double itemMeasurementOne,
                            int totalMeasurementTwo,
                            int itemMeasurementTwo,
                            boolean isValidMeasurement,
                            double minimumMeasurement,
                            double maxMeasurementOne,
                            int maxMeasurementTwo,
                            Pair[] measurementUnitDigitWidths) {
        this.type = type;
        this.subtype = subtype;
        this.numberOfMeasurementUnits = numberOfMeasurementUnits;
        this.isConversionFactorEnabled = isConversionFactorEnabled;
        this.conversionFactor = conversionFactor;
        this.itemBaseUnits = itemBaseUnits;
        this.totalBaseUnits = totalBaseUnits;
        this.numberOfItems = numberOfItems;
        this.totalMeasurementOne = totalMeasurementOne;
        this.itemMeasurementOne = itemMeasurementOne;
        this.totalMeasurementTwo = totalMeasurementTwo;
        this.itemMeasurementTwo = itemMeasurementTwo;
        this.isValidMeasurement = isValidMeasurement;
        this.minimumMeasurement = minimumMeasurement;
        this.maxMeasurementOne = maxMeasurementOne;
        this.maxMeasurementTwo = maxMeasurementTwo;
        this.measurementUnitDigitWidths = measurementUnitDigitWidths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementModel model = (MeasurementModel) o;
        return numberOfMeasurementUnits == model.numberOfMeasurementUnits &&
                isConversionFactorEnabled == model.isConversionFactorEnabled &&
                Double.compare(model.conversionFactor, conversionFactor) == 0 &&
                Double.compare(model.itemBaseUnits, itemBaseUnits) == 0 &&
                Double.compare(model.totalBaseUnits, totalBaseUnits) == 0 &&
                numberOfItems == model.numberOfItems &&
                Double.compare(model.totalMeasurementOne, totalMeasurementOne) == 0 &&
                Double.compare(model.itemMeasurementOne, itemMeasurementOne) == 0 &&
                totalMeasurementTwo == model.totalMeasurementTwo &&
                itemMeasurementTwo == model.itemMeasurementTwo &&
                isValidMeasurement == model.isValidMeasurement &&
                Double.compare(model.minimumMeasurement, minimumMeasurement) == 0 &&
                Double.compare(model.maxMeasurementOne, maxMeasurementOne) == 0 &&
                maxMeasurementTwo == model.maxMeasurementTwo &&
                type == model.type &&
                subtype == model.subtype &&
                Arrays.equals(measurementUnitDigitWidths, model.measurementUnitDigitWidths);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, subtype, numberOfMeasurementUnits,
                isConversionFactorEnabled, conversionFactor, itemBaseUnits, totalBaseUnits,
                numberOfItems, totalMeasurementOne, itemMeasurementOne, totalMeasurementTwo,
                itemMeasurementTwo, isValidMeasurement, minimumMeasurement, maxMeasurementOne,
                maxMeasurementTwo);
        result = 31 * result + Arrays.hashCode(measurementUnitDigitWidths);
        return result;
    }

    @Override
    public String toString() {
        return "MeasurementModel{" +
                "type=" + type +
                ", subtype=" + subtype +
                ", numberOfMeasurementUnits=" + numberOfMeasurementUnits +
                ", isConversionFactorEnabled=" + isConversionFactorEnabled +
                ", conversionFactor=" + conversionFactor +
                ", itemBaseUnits=" + itemBaseUnits +
                ", totalBaseUnits=" + totalBaseUnits +
                ", numberOfItems=" + numberOfItems +
                ", totalMeasurementOne=" + totalMeasurementOne +
                ", itemMeasurementOne=" + itemMeasurementOne +
                ", totalMeasurementTwo=" + totalMeasurementTwo +
                ", itemMeasurementTwo=" + itemMeasurementTwo +
                ", isValidMeasurement=" + isValidMeasurement +
                ", minimumMeasurement=" + minimumMeasurement +
                ", maxMeasurementOne=" + maxMeasurementOne +
                ", maxMeasurementTwo=" + maxMeasurementTwo +
                ", measurementUnitDigitWidths=" + Arrays.toString(measurementUnitDigitWidths) +
                '}';
    }

    @NonNull
    public MeasurementType getType() {
        return type;
    }

    @NonNull
    public MeasurementSubtype getSubtype() {
        return subtype;
    }

    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    public boolean isConversionFactorEnabled() {
        return isConversionFactorEnabled;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double getItemBaseUnits() {
        return itemBaseUnits;
    }

    public double getTotalBaseUnits() {
        return totalBaseUnits;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public double getTotalMeasurementOne() {
        return totalMeasurementOne;
    }

    public double getItemMeasurementOne() {
        return itemMeasurementOne;
    }

    public int getTotalMeasurementTwo() {
        return totalMeasurementTwo;
    }

    public int getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    public boolean isValidMeasurement() {
        return isValidMeasurement;
    }

    public double getMinimumMeasurement() {
        return minimumMeasurement;
    }

    public double getMaxMeasurementOne() {
        return maxMeasurementOne;
    }

    public int getMaxMeasurementTwo() {
        return maxMeasurementTwo;
    }

    public Pair[] getMeasurementUnitDigitWidths() {
        return measurementUnitDigitWidths;
    }
}
