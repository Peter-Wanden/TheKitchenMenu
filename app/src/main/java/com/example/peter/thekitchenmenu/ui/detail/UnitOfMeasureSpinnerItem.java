package com.example.peter.thekitchenmenu.ui.detail;

public class UnitOfMeasureSpinnerItem {

    private SpinnerItemType type;
    private String measurementUnit;


    public UnitOfMeasureSpinnerItem(SpinnerItemType type, String measurementUnit) {
        this.type = type;
        this.measurementUnit = measurementUnit;
    }

    public String getMeasurementUnits() {
        return measurementUnit;
    }

    public SpinnerItemType getType() {
        return type;
    }
}
