package com.example.peter.thekitchenmenu.ui.utils;

public class UnitOfMeasureSpinnerItem {

    private SpinnerItemType type;
    private String measurementUnit;


    public UnitOfMeasureSpinnerItem(SpinnerItemType type, String measurementUnit) {
        this.type = type;
        this.measurementUnit = measurementUnit;
    }

    String getMeasurementUnit() {
        return measurementUnit;
    }

    public SpinnerItemType getType() {
        return type;
    }
}
