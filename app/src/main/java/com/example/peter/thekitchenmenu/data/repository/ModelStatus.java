package com.example.peter.thekitchenmenu.data.repository;

// Used by the sync classes to monitor a data models synchronisation status.
class ModelStatus {

    private String modelName;
    private boolean dataSetReturned;
    private int noOfItemsReturned;

    ModelStatus(String modelName) {
        this.modelName = modelName;
        this.dataSetReturned = false;
        this.noOfItemsReturned = 0;
    }

    ModelStatus(String modelName, boolean dataSetReturned, int noOfItemsReturned) {
        this.modelName = modelName;
        this.dataSetReturned = dataSetReturned;
        this.noOfItemsReturned = noOfItemsReturned;
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

    int getNoOfItemsReturned() {
        return noOfItemsReturned;
    }
}
