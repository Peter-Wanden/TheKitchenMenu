package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-MeasurementVM";

    private MutableLiveData<ProductMeasurementModel> modelOut;
    private ProductMeasurementHandler measurementHandler;

    private MeasurementSubtype subtype;
    private UnitOfMeasure unitOfMeasure;
    private int numberOfMeasurementUnits;
    private int numberOfProducts;

    // TODO - these need to be integers and doubles!
    private String packMeasurementOne;
    private String itemMeasurementOne;
    private String packMeasurementTwo;
    private String itemMeasurementTwo;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        modelOut = new MutableLiveData<>();
        measurementHandler = new ProductMeasurementHandler(this);
        setSubtype(MeasurementSubtype.TYPE_METRIC_MASS); // default
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
//        setSubtype(measurementModelIn.getMeasurementSubtype());
//        setNumberOfProducts(measurementModelIn.getNumberOfProducts());
//        setBaseUnits(measurementModelIn.getBaseUnits());
        setSubtype(MeasurementSubtype.TYPE_IMPERIAL_VOLUME);
        setNumberOfProducts(20);
        setBaseSiUnits(1500);
    }

    @Bindable
    public MeasurementSubtype getSubtype() {
        return subtype;
    }

    public void setSubtype(MeasurementSubtype subtype) {
        if (this.subtype != subtype) {
            newUnitOfMeasureSelected(subtype);
        }
    }

    private void newUnitOfMeasureSelected(MeasurementSubtype subtype) {
        unitOfMeasure = subtype.getMeasurementClass();
        updateUi();
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    @Bindable
    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void addOneToNumberOfItems() {
        setNumberOfProducts((unitOfMeasure.getNumberOfProducts() + 1));
    }

    public void minusOneFromNumberOfItems() {
        setNumberOfProducts((unitOfMeasure.getNumberOfProducts() - 1));
    }

    public void setNumberOfProducts(int numberOfProducts) {
        if (newNumberOfItemsReceived(numberOfProducts))
            if (canChangeToNewNumberOfItems(numberOfProducts)) updateUi(); // with new value
                // Room to add error message if required
            else updateUi(); // with old value
    }

    private boolean newNumberOfItemsReceived(int newNumberOfItems) {
        return unitOfMeasure.getNumberOfProducts() != newNumberOfItems;
    }

    private boolean canChangeToNewNumberOfItems(int numberOfItems) {
        return unitOfMeasure.numberOfProductsIsSet(numberOfItems);
    }

    private void setBaseSiUnits(double newBaseSiUnits) {
        if (newBaseUnitsReceived(newBaseSiUnits)) {
            if (canChangeToNewNumberOfBaseSiUnits(newBaseSiUnits)) updateUi(); // with new value
                // Room to add error message if required
            else updateUi(); // with old value
        }
    }

    private boolean newBaseUnitsReceived(double newBaseSiUnits) {
        return unitOfMeasure.getBaseUnits() != newBaseSiUnits;
    }

    private boolean canChangeToNewNumberOfBaseSiUnits(double newBaseSiUnits) {
        return unitOfMeasure.baseUnitsAreSet(newBaseSiUnits);
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
                oldMeasurement = unitOfMeasure.getProductMeasurementOne();
                return oldMeasurement != newMeasurement;
        }
        return false;
    }

    private void processDecimalMeasurements(int viewId, double decimalMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one)
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(decimalMeasurement);

        if (viewId == R.id.item_editable_measurement_one)
            measurementIsSet = unitOfMeasure.productMeasurementOneIsSet(decimalMeasurement);

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
                oldMeasurement = unitOfMeasure.getProductMeasurementTwo();
                return newMeasurement != oldMeasurement;
        }
        return false;
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_two)
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        if (viewId == R.id.item_editable_measurement_two)
            measurementIsSet = unitOfMeasure.productMeasurementTwoIsSet(newMeasurement);

        if (measurementIsSet) updateUi(); // with new value
        // add error message if required
        else updateUi(); // with old value set to zero
    }

    private void updateUi() {
        if (subtype != unitOfMeasure.getMeasurementSubtype()) {
            subtype = unitOfMeasure.getMeasurementSubtype();
            notifyPropertyChanged(BR.subtype);
        }
        if (numberOfMeasurementUnits != unitOfMeasure.getNumberOfMeasurementUnits()) {
            numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();
            notifyPropertyChanged(BR.numberOfMeasurementUnits);
        }
        if (numberOfProducts != unitOfMeasure.getNumberOfProducts()) {
            numberOfProducts = unitOfMeasure.getNumberOfProducts();
            notifyPropertyChanged(BR.numberOfProducts);
        }
        int numberOfUnitsAfterDecimal = (int) unitOfMeasure.getMeasurementUnitDigitLengthArray()[0].second;
        if (numberOfUnitsAfterDecimal > 0) {
            packMeasurementOne = String.valueOf(unitOfMeasure.getPackMeasurementOne());
            notifyPropertyChanged(BR.packMeasurementOne);

            itemMeasurementOne = String.valueOf(unitOfMeasure.getProductMeasurementOne());
            notifyPropertyChanged(BR.itemMeasurementOne);
        } else {
            packMeasurementOne = String.valueOf((int) unitOfMeasure.getPackMeasurementOne());
            notifyPropertyChanged(BR.packMeasurementOne);

            itemMeasurementOne = String.valueOf((int) unitOfMeasure.getProductMeasurementOne());
            notifyPropertyChanged(BR.itemMeasurementOne);
        }
        if (numberOfMeasurementUnits > 1) {
            packMeasurementTwo = String.valueOf(unitOfMeasure.getPackMeasurementTwo());
            notifyPropertyChanged(BR.packMeasurementTwo);

            itemMeasurementTwo = String.valueOf(unitOfMeasure.getProductMeasurementTwo());
            notifyPropertyChanged(BR.itemMeasurementTwo);
        }
        updateMeasurementModel();
    }

    private void updateMeasurementModel() {
        ProductMeasurementModel modelOut = new ProductMeasurementModel();
        modelOut.setMeasurementSubtype(unitOfMeasure.getMeasurementSubtype());
        modelOut.setNumberOfProducts(unitOfMeasure.getNumberOfProducts());
        modelOut.setBaseUnits(unitOfMeasure.getBaseUnits());

        if (unitOfMeasure.isValidMeasurement()) {
            getModelOut().setValue(modelOut);
            Log.d(TAG, "updateMeasurementModel: model isValid:" + modelOut.toString());
        } else Log.d(TAG, "updateMeasurementModel: model isNotValid");
    }
}