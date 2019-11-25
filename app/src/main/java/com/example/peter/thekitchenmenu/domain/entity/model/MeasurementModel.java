package com.example.peter.thekitchenmenu.domain.entity.model;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementType;

import java.util.Arrays;
import java.util.Objects;

public final class MeasurementModel {

    @NonNull
    private final MeasurementType type;
    @NonNull
    private final MeasurementSubtype subtype;

    private final int numberOfUnits;

    private final boolean isConversionFactorEnabled;
    private final double conversionFactor;

    private final double itemBaseUnits;
    private final double totalBaseUnits;

    private final int numberOfItems;

    private final double totalUnitOne;
    private final double itemUnitOne;

    private final int totalUnitTwo;
    private final int itemUnitTwo;

    private final boolean isValidMeasurement;
    // Min measurement is displayed as a single unit
    private final double minUnitOne;
    // Max measurement is displayed as two units
    private final double maxUnitOne;
    private final int maxUnitTwo;

    private final Pair[] maxUnitDigitWidths;

    public MeasurementModel(@NonNull MeasurementType type,
                            @NonNull MeasurementSubtype subtype,
                            int numberOfUnits,
                            boolean isConversionFactorEnabled,
                            double conversionFactor,
                            double itemBaseUnits,
                            double totalBaseUnits,
                            int numberOfItems,
                            double totalUnitOne,
                            double itemUnitOne,
                            int totalUnitTwo,
                            int itemUnitTwo,
                            boolean isValidMeasurement,
                            double minUnitOne,
                            double maxUnitOne,
                            int maxUnitTwo,
                            Pair[] maxUnitDigitWidths) {
        this.type = type;
        this.subtype = subtype;
        this.numberOfUnits = numberOfUnits;
        this.isConversionFactorEnabled = isConversionFactorEnabled;
        this.conversionFactor = conversionFactor;
        this.itemBaseUnits = itemBaseUnits;
        this.totalBaseUnits = totalBaseUnits;
        this.numberOfItems = numberOfItems;
        this.totalUnitOne = totalUnitOne;
        this.itemUnitOne = itemUnitOne;
        this.totalUnitTwo = totalUnitTwo;
        this.itemUnitTwo = itemUnitTwo;
        this.isValidMeasurement = isValidMeasurement;
        this.minUnitOne = minUnitOne;
        this.maxUnitOne = maxUnitOne;
        this.maxUnitTwo = maxUnitTwo;
        this.maxUnitDigitWidths = maxUnitDigitWidths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementModel model = (MeasurementModel) o;
        return numberOfUnits == model.numberOfUnits &&
                isConversionFactorEnabled == model.isConversionFactorEnabled &&
                Double.compare(model.conversionFactor, conversionFactor) == 0 &&
                Double.compare(model.itemBaseUnits, itemBaseUnits) == 0 &&
                Double.compare(model.totalBaseUnits, totalBaseUnits) == 0 &&
                numberOfItems == model.numberOfItems &&
                Double.compare(model.totalUnitOne, totalUnitOne) == 0 &&
                Double.compare(model.itemUnitOne, itemUnitOne) == 0 &&
                totalUnitTwo == model.totalUnitTwo &&
                itemUnitTwo == model.itemUnitTwo &&
                isValidMeasurement == model.isValidMeasurement &&
                Double.compare(model.minUnitOne, minUnitOne) == 0 &&
                Double.compare(model.maxUnitOne, maxUnitOne) == 0 &&
                maxUnitTwo == model.maxUnitTwo &&
                type == model.type &&
                subtype == model.subtype &&
                Arrays.equals(maxUnitDigitWidths, model.maxUnitDigitWidths);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, subtype, numberOfUnits,
                isConversionFactorEnabled, conversionFactor, itemBaseUnits, totalBaseUnits,
                numberOfItems, totalUnitOne, itemUnitOne, totalUnitTwo,
                itemUnitTwo, isValidMeasurement, minUnitOne, maxUnitOne,
                maxUnitTwo);
        result = 31 * result + Arrays.hashCode(maxUnitDigitWidths);
        return result;
    }

    @Override
    public String toString() {
        return "MeasurementModel{" +
                "type=" + type +
                ", subtype=" + subtype +
                ", numberOfUnits=" + numberOfUnits +
                ", isConversionFactorEnabled=" + isConversionFactorEnabled +
                ", conversionFactor=" + conversionFactor +
                ", itemBaseUnits=" + itemBaseUnits +
                ", totalBaseUnits=" + totalBaseUnits +
                ", numberOfItems=" + numberOfItems +
                ", totalUnitOne=" + totalUnitOne +
                ", itemUnitOne=" + itemUnitOne +
                ", totalUnitTwo=" + totalUnitTwo +
                ", itemUnitTwo=" + itemUnitTwo +
                ", isValidMeasurement=" + isValidMeasurement +
                ", minUnitOne=" + minUnitOne +
                ", maxUnitOne=" + maxUnitOne +
                ", maxUnitTwo=" + maxUnitTwo +
                ", maxUnitDigitWidths=" + Arrays.toString(maxUnitDigitWidths) +
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

    public int getNumberOfUnits() {
        return numberOfUnits;
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

    public double getTotalUnitOne() {
        return totalUnitOne;
    }

    public double getItemUnitOne() {
        return itemUnitOne;
    }

    public int getTotalUnitTwo() {
        return totalUnitTwo;
    }

    public int getItemUnitTwo() {
        return itemUnitTwo;
    }

    public boolean isValidMeasurement() {
        return isValidMeasurement;
    }

    public double getMinUnitOne() {
        return minUnitOne;
    }

    public double getMaxUnitOne() {
        return maxUnitOne;
    }

    public int getMaxUnitTwo() {
        return maxUnitTwo;
    }

    public Pair[] getMaxUnitDigitWidths() {
        return maxUnitDigitWidths;
    }
}
