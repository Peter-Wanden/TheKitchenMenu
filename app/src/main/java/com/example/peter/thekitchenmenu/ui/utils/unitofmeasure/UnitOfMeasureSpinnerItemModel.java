package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.ui.utils.SpinnerItemType;

public class UnitOfMeasureSpinnerItemModel {

    private SpinnerItemType type;
    private String measurementUnit;

    public UnitOfMeasureSpinnerItemModel(SpinnerItemType type, String measurementUnit) {
        this.type = type;
        this.measurementUnit = measurementUnit;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public SpinnerItemType getType() {
        return type;
    }
}
