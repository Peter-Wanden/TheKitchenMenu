package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private static final String TAG = "ProductMeasurementViewM";

    private MutableLiveData<ProductMeasurementModel> existingMeasurementModel =
            new MutableLiveData<>();

    private Resources resources;

    // Whenever a editedMeasurementModel is valid, set it to measurement model
    private ProductMeasurementModel editedMeasurementModel;
    private ProductMeasurementHandler measurementHandler;

    private static final int MEASUREMENT_ERROR = -1;
    private UnitOfMeasure unitOfMeasure;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        editedMeasurementModel = new ProductMeasurementModel();

        measurementHandler = new ProductMeasurementHandler(
                application,
                this);
        resources = application.getResources();

        unitOfMeasure = MeasurementSubType.TYPE_METRIC_MASS.getMeasurementClass();
        updateMeasurementModel();
    }

    MutableLiveData<ProductMeasurementModel> getExistingMeasurementModel() {
        return existingMeasurementModel;
    }

    public ProductMeasurementHandler getMeasurementHandler() {
        return measurementHandler;
    }

    ProductMeasurementModel getEditedMeasurementModel() {
        return editedMeasurementModel;
    }

    public void setEditedMeasurementModel(ProductMeasurementModel editedMeasurementModel) {
        this.editedMeasurementModel = editedMeasurementModel;
    }

    public void changeUnitOfMeasure(int subTypeAsInt) {
        if (unitOfMeasure.getMeasurementSubType().ordinal() != subTypeAsInt) {
            unitOfMeasure = MeasurementSubType.values()[subTypeAsInt].getMeasurementClass();
            updateMeasurementModel();
        }
    }

    // Synchronises the measurement model with the unit of measure
    private void updateMeasurementModel() {

        Log.d(TAG, "tkm - updateMeasurementModel: Updating measurement model");
        Log.d(TAG, "tkm - updateMeasurementModel: Base units are: " + unitOfMeasure.getBaseSiUnits());

        if (editedMeasurementModel.getMeasurementSubType() != unitOfMeasure.getMeasurementSubType()) {
            Log.d(TAG, "tkm - updateMeasurementModel: Updating measurement Subtype to: " +
                    unitOfMeasure.getMeasurementSubType());

            editedMeasurementModel.setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
        }

        if (editedMeasurementModel.getNumberOfMeasurementUnits() !=
                unitOfMeasure.getNumberOfMeasurementUnits()) {
            Log.d(TAG, "tkm - updateMeasurementModel: Updating number of measurement units to: " +
                    unitOfMeasure.getNumberOfMeasurementUnits());

            editedMeasurementModel.setNumberOfMeasurementUnits(
                    unitOfMeasure.getNumberOfMeasurementUnits());
        }

        if (editedMeasurementModel.getNumberOfItems() != unitOfMeasure.getNumberOfItems()) {
            Log.d(TAG, "tkm - updateMeasurementModel: Updating number of Items to: " +
                    unitOfMeasure.getNumberOfItems());

            editedMeasurementModel.setNumberOfItems(unitOfMeasure.getNumberOfItems());
        }

        if (unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_MASS ||
                unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_VOLUME) {

            if (editedMeasurementModel.getPackMeasurementOneAsDecimal() !=
                    unitOfMeasure.getPackMeasurementOne()) {
                Log.d(TAG, "tkm - updateMeasurementModel: Updating pack One DECIMAL to: " +
                        unitOfMeasure.getPackMeasurementOne());

                editedMeasurementModel.setPackMeasurementOneAsDecimal(
                        unitOfMeasure.getPackMeasurementOne());
            }

            if (editedMeasurementModel.getItemMeasurementOneAsDecimal() !=
                    unitOfMeasure.getItemMeasurementOne()) {
                editedMeasurementModel.setItemMeasurementOneAsDecimal(
                        unitOfMeasure.getItemMeasurementOne());

                Log.d(TAG, "tkm - updateMeasurementModel: Updating Item One DECIMAL to: " +
                        unitOfMeasure.getItemMeasurementOne());
            }

        } else {

            if (editedMeasurementModel.getPackMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getPackMeasurementOne()) {
                Log.d(TAG, "tkm - updateMeasurementModel: Updating Pack One as INTEGER to: " +
                        (int) unitOfMeasure.getPackMeasurementOne());

                editedMeasurementModel.setPackMeasurementOneAsInt(
                        (int) unitOfMeasure.getPackMeasurementOne());
            }

            if (editedMeasurementModel.getItemMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getItemMeasurementOne()) {
                Log.d(TAG, "tkm - updateMeasurementModel: Updating Item One as INTEGER to: " +
                        (int) unitOfMeasure.getItemMeasurementOne());

                editedMeasurementModel.setItemMeasurementOneAsInt(
                        (int) unitOfMeasure.getItemMeasurementOne());
            }
        }

        if (editedMeasurementModel.getPackMeasurementTwo() !=
                unitOfMeasure.getPackMeasurementTwo()) {
            editedMeasurementModel.setPackMeasurementTwo(unitOfMeasure.getPackMeasurementTwo());

            Log.d(TAG, "tkm - updateMeasurementModel: Updating pack Two to: " +
                    unitOfMeasure.getPackMeasurementTwo());
        }


        Log.d(TAG, "tkm - updateMeasurementModel: Measurement - Item two is: " +
                editedMeasurementModel.getItemMeasurementTwo() +
                " Unit of measure Item Two is: " + unitOfMeasure.getItemMeasurementTwo());

        if (editedMeasurementModel.getItemMeasurementTwo() !=
                unitOfMeasure.getItemMeasurementTwo()) {

            editedMeasurementModel.setItemMeasurementTwo(
                    unitOfMeasure.getItemMeasurementTwo());

            Log.d(TAG, "tkm - updateMeasurementModel: Updating Item Two to: " +
                    unitOfMeasure.getItemMeasurementTwo());
        }

        existingMeasurementModel.setValue(editedMeasurementModel);
        Log.d(TAG, "tkm - updateMeasurementModel: Measurement model updating complete");
    }
}
