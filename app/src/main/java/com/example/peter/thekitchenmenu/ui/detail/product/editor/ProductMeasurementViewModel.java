package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-MeasurementVM";

    private MutableLiveData<ProductMeasurementModel> modelOut;
    private ProductMeasurementHandler measurementHandler;

    private MeasurementSubType subType;
    private UnitOfMeasure unitOfMeasure;
    private int numberOfMeasurementUnits;
    private int numberOfItems;

    // TODO - these need to be integers and doubles!
    private String packMeasurementOne;
    private String itemMeasurementOne;
    private String packMeasurementTwo;
    private String itemMeasurementTwo;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        modelOut = new MutableLiveData<>();
        measurementHandler = new ProductMeasurementHandler(this);
        setSubType(MeasurementSubType.TYPE_METRIC_MASS); // default
    }

    MutableLiveData<ProductMeasurementModel> getModelOut() {
        if (modelOut == null) modelOut = new MutableLiveData<>();
        return modelOut;
    }

    ProductMeasurementHandler getMeasurementHandler() {
        return measurementHandler;
    }

    // TODO - Bind number of measurement units to the display and remove it from Measurement
    //  model. Then re-jig the references to it in xml
    //
    void setMeasurementModelIn(ProductMeasurementModel measurementModelIn) {
        setSubType(measurementModelIn.getMeasurementSubType());
        setNumberOfItems(measurementModelIn.getNumberOfItems());
        setBaseSiUnits(measurementModelIn.getBaseSiUnits());
    }

    @Bindable
    public MeasurementSubType getSubType() {
        return subType;
    }

    public void setSubType(MeasurementSubType subType) {
        if (this.subType != subType) {
            newUnitOfMeasureSelected(subType);
        }
    }

    UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    private void newUnitOfMeasureSelected(MeasurementSubType subType) {
        unitOfMeasure = subType.getMeasurementClass();
        updateUi();
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    @Bindable
    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void addOneToNumberOfItems() {
        setNumberOfItems((unitOfMeasure.getNumberOfItems() + 1));
    }

    public void minusOneFromNumberOfItems() {
        setNumberOfItems((unitOfMeasure.getNumberOfItems() - 1));
    }

    public void setNumberOfItems(int numberOfItems) {
        if (newNumberOfItemsReceived(numberOfItems))
            if (canChangeToNewNumberOfItems(numberOfItems)) updateUi(); // with new value
                // Room to add error message if required
            else updateUi(); // with old value
    }

    private boolean newNumberOfItemsReceived(int newNumberOfItems) {
        return unitOfMeasure.getNumberOfItems() != newNumberOfItems;
    }

    private boolean canChangeToNewNumberOfItems(int numberOfItems) {
        return unitOfMeasure.numberOfItemsAreSet(numberOfItems);
    }

    private void setBaseSiUnits(double newBaseSiUnits) {
        if (newBaseUnitsReceived(newBaseSiUnits)) {
            if (canChangeToNewNumberOfBaseSiUnits(newBaseSiUnits)) updateUi(); // with new value
                // Room to add error message if required
            else updateUi(); // with old value
        }
    }

    private boolean newBaseUnitsReceived(double newBaseSiUnits) {
        return unitOfMeasure.getBaseSiUnits() != newBaseSiUnits;
    }

    private boolean canChangeToNewNumberOfBaseSiUnits(double newBaseSiUnits) {
        return unitOfMeasure.baseSiUnitsAreSet(newBaseSiUnits);
    }

    @Bindable
    public String getPackMeasurementOne() {
        return packMeasurementOne;
    }

    @Bindable
    public String getItemMeasurementOne() {
        return itemMeasurementOne;
    }

    @Bindable
    public String getPackMeasurementTwo() {
        return packMeasurementTwo;
    }

    @Bindable
    public String getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    void newDecimalMeasurementReceived(int viewId, double decimalMeasurement) {
        if (decimalMeasurementHasChanged(viewId, decimalMeasurement))
            processDecimalMeasurements(viewId, decimalMeasurement);
    }

    private boolean decimalMeasurementHasChanged(int viewId, double newMeasurement) {
        double oldMeasurement;

        switch (viewId) {
            case R.id.pack_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getPackMeasurementOne();
                return oldMeasurement != newMeasurement;

            case R.id.item_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getItemMeasurementOne();
                return oldMeasurement != newMeasurement;
        }
        return false;
    }

    private void processDecimalMeasurements(int viewId, double decimalMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one)
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(decimalMeasurement);

        if (viewId == R.id.item_editable_measurement_one)
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(decimalMeasurement);

        if (measurementIsSet) updateUi(); // with new value
            // add error message if required
        else updateUi(); // with old value set to zero
    }

    void newIntegerMeasurementReceived(int viewId, int integerMeasurement) {
        if (integerMeasurementHasChanged(viewId, integerMeasurement))
            processIntegerMeasurements(viewId, integerMeasurement);
    }

    private boolean integerMeasurementHasChanged(int viewId, int newMeasurement) {
        int oldMeasurement;

        switch (viewId) {
            case R.id.pack_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getPackMeasurementTwo();
                return newMeasurement != oldMeasurement;

            case R.id.item_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getItemMeasurementTwo();
                return newMeasurement != oldMeasurement;
        }
        return false;
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_two)
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        if (viewId == R.id.item_editable_measurement_two)
            measurementIsSet = unitOfMeasure.itemMeasurementTwoIsSet(newMeasurement);

        if (measurementIsSet) updateUi(); // with new value
        // add error message if required
        else updateUi(); // with old value set to zero
    }

    private void updateUi() {
        if (subType != unitOfMeasure.getMeasurementSubType()) {
            subType = unitOfMeasure.getMeasurementSubType();
            notifyPropertyChanged(BR.subType);
        }
        if (numberOfMeasurementUnits != unitOfMeasure.getNumberOfMeasurementUnits()) {
            numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();
            notifyPropertyChanged(BR.numberOfMeasurementUnits);
        }
        if (numberOfItems != unitOfMeasure.getNumberOfItems()) {
            numberOfItems = unitOfMeasure.getNumberOfItems();
            notifyPropertyChanged(BR.numberOfItems);
        }
        int numberOfUnitsAfterDecimal = (int) unitOfMeasure.getMeasurementUnitNumberTypeArray()[0].second;
        if (numberOfUnitsAfterDecimal > 0) {
            packMeasurementOne = String.valueOf(unitOfMeasure.getPackMeasurementOne());
            notifyPropertyChanged(BR.packMeasurementOne);

            itemMeasurementOne = String.valueOf(unitOfMeasure.getItemMeasurementOne());
            notifyPropertyChanged(BR.itemMeasurementOne);
        } else {
            packMeasurementOne = String.valueOf((int) unitOfMeasure.getPackMeasurementOne());
            notifyPropertyChanged(BR.packMeasurementOne);

            itemMeasurementOne = String.valueOf((int) unitOfMeasure.getItemMeasurementOne());
            notifyPropertyChanged(BR.itemMeasurementOne);
        }
        if (numberOfMeasurementUnits > 1) {
            packMeasurementTwo = String.valueOf(unitOfMeasure.getPackMeasurementTwo());
            notifyPropertyChanged(BR.packMeasurementTwo);

            itemMeasurementTwo = String.valueOf(unitOfMeasure.getItemMeasurementTwo());
            notifyPropertyChanged(BR.itemMeasurementTwo);
        }
        updateMeasurementModel();
    }

    private void updateMeasurementModel() {
        ProductMeasurementModel modelOut = new ProductMeasurementModel();
        modelOut.setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
        modelOut.setNumberOfItems(unitOfMeasure.getNumberOfItems());
        modelOut.setBaseSiUnits(unitOfMeasure.getBaseSiUnits());

        if (unitOfMeasure.isValidMeasurement()) {
            getModelOut().setValue(modelOut);
            Log.d(TAG, "updateMeasurementModel: model isValid:" + modelOut.toString());
        } else Log.d(TAG, "updateMeasurementModel: model isNotValid");
    }
}