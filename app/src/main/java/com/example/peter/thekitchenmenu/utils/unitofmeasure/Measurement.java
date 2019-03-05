package com.example.peter.thekitchenmenu.utils.unitofmeasure;

public class Measurement {

    private String type;
    private String measurementUnitOne;
    private String measurementUnitTwo;
    private double minimumMeasurement;
    private int measurementOne;
    private int measurementTwo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeasurementUnitOne() {
        return measurementUnitOne;
    }

    public void setMeasurementUnitOne(String measurementUnitOne) {
        this.measurementUnitOne = measurementUnitOne;
    }

    public String getMeasurementUnitTwo() {
        return measurementUnitTwo;
    }

    public void setMeasurementUnitTwo(String measurementUnitTwo) {
        this.measurementUnitTwo = measurementUnitTwo;
    }

    public double getMinimumMeasurement() {
        return minimumMeasurement;
    }

    public void setMinimumMeasurement(double minimumMeasurement) {
        this.minimumMeasurement = minimumMeasurement;
    }

    public int getMeasurementOne() {
        return measurementOne;
    }

    public void setMeasurementOne(int measurementOne) {
        this.measurementOne = measurementOne;
    }

    public int getMeasurementTwo() {
        return measurementTwo;
    }

    public void setMeasurementTwo(int measurementTwo) {
        this.measurementTwo = measurementTwo;
    }
}
