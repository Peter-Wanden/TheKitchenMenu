package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.ui.ObservableAndroidViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableAndroidViewModel {

    private final MutableLiveData<ProductMeasurementModel> measurementModel = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> measurementModelIsValidEvent = new SingleLiveEvent<>();
    private ProductMeasurementHandler measurementHandler;

    private MeasurementSubtype subtype;
    private UnitOfMeasure unitOfMeasure;
    private int numberOfMeasurementUnits;
    private int numberOfProducts;

    private String packMeasurementOne;
    private String productMeasurementOne;
    private String packMeasurementTwo;
    private String productMeasurementTwo;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);
        measurementHandler = new ProductMeasurementHandler(this);
        setSubtype(MeasurementSubtype.METRIC_MASS); // default
    }

    MutableLiveData<ProductMeasurementModel> getMeasurementModel() {
        return measurementModel;
    }

    ProductMeasurementHandler getMeasurementHandler() {
        return measurementHandler;
    }

    void setMeasurementModel(ProductMeasurementModel measurementModel) {
        // Always add a measurement in this order: 1.subType 2.baseUnits 3.numberOfProducts
        setSubtype(measurementModel.getMeasurementSubtype());
        setBaseUnits(measurementModel.getBaseUnits());
        setNumberOfProducts(measurementModel.getNumberOfProducts());
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

    public void incrementNumberOfProducts() {
        setNumberOfProducts((unitOfMeasure.getNumberOfItems() + 1));
    }

    public void decrementNumberOfProducts() {
        setNumberOfProducts((unitOfMeasure.getNumberOfItems() - 1));
    }

    public void setNumberOfProducts(int numberOfProducts) {
        if (newNumberOfProductsReceived(numberOfProducts))
            if (canChangeToNewNumberOfProducts(numberOfProducts))
                updateUi(); // with new value
            // if required, add error message here
            else
                updateUi(); // with old value
    }

    private boolean newNumberOfProductsReceived(int newNumberOfProducts) {
        return unitOfMeasure.getNumberOfItems() != newNumberOfProducts;
    }

    private boolean canChangeToNewNumberOfProducts(int numberOfProducts) {
        return unitOfMeasure.isNumberOfItemsSet(numberOfProducts);
    }

    private void setBaseUnits(double newBaseUnits) {
        if (baseUnitsValueChanged(newBaseUnits))
            if (canChangeToNewNumberOfBaseUnits(newBaseUnits))
                updateUi(); // with new value
                // if required, add error message here
            else updateUi(); // with old value
    }

    private boolean baseUnitsValueChanged(double newBaseUnits) {
        return unitOfMeasure.getTotalBaseUnits() != newBaseUnits;
    }

    private boolean canChangeToNewNumberOfBaseUnits(double newBaseUnits) {
        return unitOfMeasure.isTotalBaseUnitsSet(newBaseUnits);
    }

    @Bindable
    public String getPackMeasurementOne() {
        return packMeasurementOne;
    }

    @Bindable
    public String getProductMeasurementOne() {
        return productMeasurementOne;
    }

    @Bindable
    public String getPackMeasurementTwo() {
        return packMeasurementTwo;
    }

    @Bindable
    public String getProductMeasurementTwo() {
        return productMeasurementTwo;
    }

    void newDecimalMeasurementReceived(int viewId, double decimalMeasurement) {
        if (decimalMeasurementHasChanged(viewId, decimalMeasurement))
            processDecimalMeasurements(viewId, decimalMeasurement);
    }

    private boolean decimalMeasurementHasChanged(int viewId, double newMeasurement) {
        double oldMeasurement;

        switch (viewId) {
            case R.id.pack_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getTotalUnitOne();
                return oldMeasurement != newMeasurement;

            case R.id.product_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getItemUnitOne();
                return oldMeasurement != newMeasurement;
        }
        return false;
    }

    private void processDecimalMeasurements(int viewId, double decimalMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one)
            measurementIsSet = unitOfMeasure.isTotalUnitOneSet(decimalMeasurement);

        if (viewId == R.id.product_editable_measurement_one)
            measurementIsSet = unitOfMeasure.isItemUnitOneSet(decimalMeasurement);

        if (measurementIsSet)
            updateUi(); // with new value
            // add error message if required
        else
            updateUi(); // with old value set to zero
    }

    void newIntegerMeasurementReceived(int viewId, int integerMeasurement) {
        if (integerMeasurementHasChanged(viewId, integerMeasurement))
            processIntegerMeasurements(viewId, integerMeasurement);
    }

    private boolean integerMeasurementHasChanged(int viewId, int newMeasurement) {
        int oldMeasurement;

        switch (viewId) {
            case R.id.pack_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getTotalUnitTwo();
                return newMeasurement != oldMeasurement;

            case R.id.product_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getItemUnitTwo();
                return newMeasurement != oldMeasurement;
        }
        return false;
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_two)
            measurementIsSet = unitOfMeasure.isTotalUnitTwoSet(newMeasurement);

        if (viewId == R.id.product_editable_measurement_two)
            measurementIsSet = unitOfMeasure.isItemUnitTwoSet(newMeasurement);

        if (measurementIsSet)
            updateUi(); // with new value
        // add error message if required
        else
            updateUi(); // with old value set to zero
    }

    private void updateUi() {
        if (subtype != unitOfMeasure.getMeasurementSubtype())
            subtype = unitOfMeasure.getMeasurementSubtype();

        if (numberOfMeasurementUnits != unitOfMeasure.getNumberOfUnits())
            numberOfMeasurementUnits = unitOfMeasure.getNumberOfUnits();

        if (numberOfProducts != unitOfMeasure.getNumberOfItems())
            numberOfProducts = unitOfMeasure.getNumberOfItems();

        int unitsAfterDecimal = (int) unitOfMeasure.getMaxUnitDigitWidths()[0].second;

        if (unitsAfterDecimal > 0) {
            packMeasurementOne = String.valueOf(unitOfMeasure.getTotalUnitOne());
            productMeasurementOne = String.valueOf(unitOfMeasure.getItemUnitOne());
        } else {
            packMeasurementOne = String.valueOf((int) unitOfMeasure.getTotalUnitOne());
            productMeasurementOne = String.valueOf((int) unitOfMeasure.getItemUnitOne());
        }
        if (numberOfMeasurementUnits > 1) {
            packMeasurementTwo = String.valueOf(unitOfMeasure.getTotalUnitTwo());
            productMeasurementTwo = String.valueOf(unitOfMeasure.getItemUnitTwo());
        }
        notifyChange();
        updateMeasurementModel();
    }

    private void updateMeasurementModel() {
        ProductMeasurementModel model = new ProductMeasurementModel();
        model.setMeasurementSubtype(unitOfMeasure.getMeasurementSubtype());
        model.setNumberOfProducts(unitOfMeasure.getNumberOfItems());
        model.setBaseUnits(unitOfMeasure.getTotalBaseUnits());

        if (unitOfMeasure.isValidMeasurement()) {
            this.measurementModel.setValue(model);
            measurementModelIsValidEvent.setValue(true);
        } else
            measurementModelIsValidEvent.setValue(false);
    }

    SingleLiveEvent<Boolean> getMeasurementModelIsValidEvent() {
        return measurementModelIsValidEvent;
    }
}