package com.example.peter.thekitchenmenu.domain.model;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementType;

public class MeasurementModelBuilder {

    private MeasurementType type;
    private MeasurementSubtype subtype;
    private int numberOfUnits;
    private boolean isConversionFactorEnabled;
    private double conversionFactor;
    private double itemBaseUnits;
    private double totalBaseUnits;
    private int numberOfItems;
    private double totalUnitOne;
    private double itemUnitOne;
    private int totalUnitTwo;
    private int itemUnitTwo;
    private boolean isValidMeasurement;
    private double minUnitOne;
    private double maxUnitOne;
    private int maxUnitTwo;
    private Pair[] maxUnitDigitWidths;

    public static MeasurementModelBuilder basedOn (@NonNull MeasurementModel oldModel) {
        return new MeasurementModelBuilder().
                setType(oldModel.getType()).
                setSubtype(oldModel.getSubtype()).
                setNumberOfUnits(oldModel.getNumberOfUnits()).
                setIsConversionFactorEnabled(oldModel.isConversionFactorEnabled()).
                setConversionFactor(oldModel.getConversionFactor()).
                setItemBaseUnits(oldModel.getItemBaseUnits()).
                setTotalBaseUnits(oldModel.getTotalBaseUnits()).
                setNumberOfItems(oldModel.getNumberOfItems()).
                setTotalUnitOne(oldModel.getTotalUnitOne()).
                setItemUnitOne(oldModel.getItemUnitOne()).
                setTotalUnitTwo(oldModel.getTotalUnitTwo()).
                setItemUnitTwo(oldModel.getItemUnitTwo()).
                setIsValidMeasurement(oldModel.isValidMeasurement()).
                setMinUnitOne(oldModel.getMinUnitOne()).
                setMaxUnitOne(oldModel.getMaxUnitOne()).
                setMaxUnitTwo(oldModel.getMaxUnitTwo()).
                setMaxUnitDigitWidths(oldModel.getMaxUnitDigitWidths());
    }


    public MeasurementModelBuilder setType(MeasurementType type) {
        this.type = type;
        return this;
    }

    public MeasurementModelBuilder setSubtype(MeasurementSubtype subtype) {
        this.subtype = subtype;
        return this;
    }

    public MeasurementModelBuilder setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
        return this;
    }

    public MeasurementModelBuilder setIsConversionFactorEnabled(boolean isConversionFactorEnabled) {
        this.isConversionFactorEnabled = isConversionFactorEnabled;
        return this;
    }

    public MeasurementModelBuilder setConversionFactor(double conversionFactor) {
        this.conversionFactor = conversionFactor;
        return this;
    }

    public MeasurementModelBuilder setItemBaseUnits(double itemBaseUnits) {
        this.itemBaseUnits = itemBaseUnits;
        return this;
    }

    public MeasurementModelBuilder setTotalBaseUnits(double totalBaseUnits) {
        this.totalBaseUnits = totalBaseUnits;
        return this;
    }

    public MeasurementModelBuilder setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        return this;
    }

    public MeasurementModelBuilder setTotalUnitOne(double totalUnitOne) {
        this.totalUnitOne = totalUnitOne;
        return this;
    }

    public MeasurementModelBuilder setItemUnitOne(double itemUnitOne) {
        this.itemUnitOne = itemUnitOne;
        return this;
    }

    public MeasurementModelBuilder setTotalUnitTwo(int totalUnitTwo) {
        this.totalUnitTwo = totalUnitTwo;
        return this;
    }

    public MeasurementModelBuilder setItemUnitTwo(int itemUnitTwo) {
        this.itemUnitTwo = itemUnitTwo;
        return this;
    }

    public MeasurementModelBuilder setIsValidMeasurement(boolean isValidMeasurement) {
        this.isValidMeasurement = isValidMeasurement;
        return this;
    }

    public MeasurementModelBuilder setMinUnitOne(double minUnitOne) {
        this.minUnitOne = minUnitOne;
        return this;
    }

    public MeasurementModelBuilder setMaxUnitOne(double maxUnitOne) {
        this.maxUnitOne = maxUnitOne;
        return this;
    }

    public MeasurementModelBuilder setMaxUnitTwo(int maxUnitTwo) {
        this.maxUnitTwo = maxUnitTwo;
        return this;
    }

    public MeasurementModelBuilder setMaxUnitDigitWidths(Pair[] maxUnitDigitWidths) {
        this.maxUnitDigitWidths = maxUnitDigitWidths;
        return this;
    }

    public MeasurementModel build() {
        return new MeasurementModel(
                type,
                subtype,
                numberOfUnits,
                isConversionFactorEnabled,
                conversionFactor,
                itemBaseUnits,
                totalBaseUnits,
                numberOfItems,
                totalUnitOne,
                itemUnitOne,
                totalUnitTwo,
                itemUnitTwo,
                isValidMeasurement,
                minUnitOne,
                maxUnitOne,
                maxUnitTwo,
                maxUnitDigitWidths
        );
    }
}
