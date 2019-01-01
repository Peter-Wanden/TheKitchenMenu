package com.example.peter.thekitchenmenu.data.repository;

class ModelStatus {

    private String modelName;
    private boolean dataSetReturned;

    ModelStatus(String modelName, boolean dataSetReturned) {
        this.modelName = modelName;
        this.dataSetReturned = dataSetReturned;
    }

    @Override
    public String toString() {
        return "ModelStatus{" +
                "modelName='" + modelName + '\'' +
                ", dataSetReturned=" + dataSetReturned +
                '}';
    }

    String getModelName() {
        return modelName;
    }

    boolean dataSetReturned() {
        return dataSetReturned;
    }
}
