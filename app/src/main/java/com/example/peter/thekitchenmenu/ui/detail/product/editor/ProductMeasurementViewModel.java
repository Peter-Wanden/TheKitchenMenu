package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-MeasurementVM";

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
        setSubtype(MeasurementSubtype.TYPE_METRIC_MASS); // default
    }

    MutableLiveData<ProductMeasurementModel> getMeasurementModel() {
        return measurementModel;
    }

    ProductMeasurementHandler getMeasurementHandler() {
        return measurementHandler;
    }

    void setMeasurementModel(ProductMeasurementModel measurementModel) {
        setSubtype(measurementModel.getMeasurementSubtype());
        setNumberOfProducts(measurementModel.getNumberOfProducts());
        setBaseUnits(measurementModel.getBaseUnits());
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
        setNumberOfProducts((unitOfMeasure.getNumberOfProducts() + 1));
    }

    public void decrementNumberOfProducts() {
        setNumberOfProducts((unitOfMeasure.getNumberOfProducts() - 1));
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
        return unitOfMeasure.getNumberOfProducts() != newNumberOfProducts;
    }

    private boolean canChangeToNewNumberOfProducts(int numberOfProducts) {
        return unitOfMeasure.numberOfProductsIsSet(numberOfProducts);
    }

    private void setBaseUnits(double newBaseUnits) {
        if (newBaseUnitsReceived(newBaseUnits))
            if (canChangeToNewNumberOfBaseUnits(newBaseUnits))
                updateUi(); // with new value
                // if required, add error message here
            else updateUi(); // with old value
    }

    private boolean newBaseUnitsReceived(double newBaseUnits) {
        return unitOfMeasure.getBaseUnits() != newBaseUnits;
    }

    private boolean canChangeToNewNumberOfBaseUnits(double newBaseUnits) {
        return unitOfMeasure.baseUnitsAreSet(newBaseUnits);
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
                oldMeasurement = unitOfMeasure.getPackMeasurementOne();
                return oldMeasurement != newMeasurement;

            case R.id.product_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getProductMeasurementOne();
                return oldMeasurement != newMeasurement;
        }
        return false;
    }

    private void processDecimalMeasurements(int viewId, double decimalMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one)
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(decimalMeasurement);

        if (viewId == R.id.product_editable_measurement_one)
            measurementIsSet = unitOfMeasure.productMeasurementOneIsSet(decimalMeasurement);

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
                oldMeasurement = unitOfMeasure.getPackMeasurementTwo();
                return newMeasurement != oldMeasurement;

            case R.id.product_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getProductMeasurementTwo();
                return newMeasurement != oldMeasurement;
        }
        return false;
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_two)
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        if (viewId == R.id.product_editable_measurement_two)
            measurementIsSet = unitOfMeasure.productMeasurementTwoIsSet(newMeasurement);

        if (measurementIsSet)
            updateUi(); // with new value
        // add error message if required
        else
            updateUi(); // with old value set to zero
    }

    private void updateUi() {
        if (subtype != unitOfMeasure.getMeasurementSubtype()) {
            subtype = unitOfMeasure.getMeasurementSubtype();
        }
        if (numberOfMeasurementUnits != unitOfMeasure.getNumberOfMeasurementUnits()) {
            numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();
        }
        if (numberOfProducts != unitOfMeasure.getNumberOfProducts()) {
            numberOfProducts = unitOfMeasure.getNumberOfProducts();
        }
        int unitsAfterDecimal = (int) unitOfMeasure.getMeasurementUnitsDigitWidths()[0].second;
        if (unitsAfterDecimal > 0) {
            packMeasurementOne = String.valueOf(unitOfMeasure.getPackMeasurementOne());
            productMeasurementOne = String.valueOf(unitOfMeasure.getProductMeasurementOne());
        } else {
            packMeasurementOne = String.valueOf((int) unitOfMeasure.getPackMeasurementOne());
            productMeasurementOne = String.valueOf((int) unitOfMeasure.getProductMeasurementOne());
        }
        if (numberOfMeasurementUnits > 1) {
            packMeasurementTwo = String.valueOf(unitOfMeasure.getPackMeasurementTwo());
            productMeasurementTwo = String.valueOf(unitOfMeasure.getProductMeasurementTwo());
        }
        notifyChange();
        updateMeasurementModel();
    }

    private void updateMeasurementModel() {
        ProductMeasurementModel measurementModel = new ProductMeasurementModel();
        measurementModel.setMeasurementSubtype(unitOfMeasure.getMeasurementSubtype());
        measurementModel.setNumberOfProducts(unitOfMeasure.getNumberOfProducts());
        measurementModel.setBaseUnits(unitOfMeasure.getBaseUnits());

        if (unitOfMeasure.isValidMeasurement()) {
            getMeasurementModel().setValue(measurementModel);
            measurementModelIsValidEvent.setValue(true);
        } else
            measurementModelIsValidEvent.setValue(false);
    }

    SingleLiveEvent<Boolean> getMeasurementModelIsValidEvent() {
        return measurementModelIsValidEvent;
    }
}