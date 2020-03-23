package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.ui.ObservableAndroidViewModel;

import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureAbstract.UNIT_ONE_WIDTH_INDEX;

public class ProductMeasurementViewModel extends ObservableAndroidViewModel {

    private final MutableLiveData<ProductMeasurementModel> measurementModel = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> measurementModelIsValidEvent = new SingleLiveEvent<>();
    private ProductMeasurementHandler measurementHandler;

    private MeasurementSubtype subtype;
    private UnitOfMeasure unitOfMeasure;
    private int numberOfUnits;
    private int numberOfItems;

    private String totalUnitOne;
    private String totalUnitTwo;
    private String itemUnitOne;
    private String itemUnitTwo;

    public ProductMeasurementViewModel(@Nonnull Application application) {
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
        // Always add a measurement in this order: 1.subType 2.baseUnits 3.numberOfItems
        setSubtype(measurementModel.getMeasurementSubtype());
        setBaseUnits(measurementModel.getBaseUnits());
        setNumberOfItems(measurementModel.getNumberOfItems());
    }

    private void setBaseUnits(double baseUnits) {
        if (isBaseUnitsChanged(baseUnits))
            if (unitOfMeasure.isTotalBaseUnitsSet(baseUnits))
                updateUi(); // with new value
                // if required, add error message here
            else updateUi(); // with old value
    }

    private boolean isBaseUnitsChanged(double baseUnits) {
        return unitOfMeasure.getTotalBaseUnits() != baseUnits;
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
    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    @Bindable
    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void incrementItems() {
        setNumberOfItems((unitOfMeasure.getNumberOfItems() + 1));
    }

    public void decrementItems() {
        setNumberOfItems((unitOfMeasure.getNumberOfItems() - 1));
    }

    public void setNumberOfItems(int numberOfItems) {
        if (isNumberOfItemsChanged(numberOfItems))
            if (unitOfMeasure.isNumberOfItemsSet(numberOfItems))
                updateUi(); // with new value
            // if required, add error message here
            else
                updateUi(); // with old value
    }

    private boolean isNumberOfItemsChanged(int numberOfItems) {
        return unitOfMeasure.getNumberOfItems() != numberOfItems;
    }

    @Bindable
    public String getTotalUnitOne() {
        return totalUnitOne;
    }

    @Bindable
    public String getItemUnitOne() {
        return itemUnitOne;
    }

    @Bindable
    public String getTotalUnitTwo() {
        return totalUnitTwo;
    }

    @Bindable
    public String getItemUnitTwo() {
        return itemUnitTwo;
    }

    void processDecimalUnit(int viewId, double decimalMeasurement) {
        if (isDecimalUnitChanged(viewId, decimalMeasurement))
            processDecimalMeasurements(viewId, decimalMeasurement);
    }

    private boolean isDecimalUnitChanged(int viewId, double newUnit) {
        double oldUnit;

        switch (viewId) {
            case R.id.pack_editable_measurement_one:
                oldUnit = unitOfMeasure.getTotalUnitOne();
                return oldUnit != newUnit;

            case R.id.product_editable_measurement_one:
                oldUnit = unitOfMeasure.getItemUnitOne();
                return oldUnit != newUnit;
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

    void processIntegerUnit(int viewId, int integerMeasurement) {
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

        if (numberOfUnits != unitOfMeasure.getNumberOfUnits())
            numberOfUnits = unitOfMeasure.getNumberOfUnits();

        if (numberOfItems != unitOfMeasure.getNumberOfItems())
            numberOfItems = unitOfMeasure.getNumberOfItems();

        int unitsAfterDecimal = (int) unitOfMeasure.
                getMaxUnitDigitWidths()[UNIT_ONE_WIDTH_INDEX].second;

        if (unitsAfterDecimal > 0) {
            totalUnitOne = String.valueOf(unitOfMeasure.getTotalUnitOne());
            itemUnitOne = String.valueOf(unitOfMeasure.getItemUnitOne());
        } else {
            totalUnitOne = String.valueOf((int) unitOfMeasure.getTotalUnitOne());
            itemUnitOne = String.valueOf((int) unitOfMeasure.getItemUnitOne());
        }
        if (numberOfUnits > 1) {
            totalUnitTwo = String.valueOf(unitOfMeasure.getTotalUnitTwo());
            itemUnitTwo = String.valueOf(unitOfMeasure.getItemUnitTwo());
        }
        notifyChange();
        updateMeasurementModel();
    }

    private void updateMeasurementModel() {
        ProductMeasurementModel model = new ProductMeasurementModel();
        model.setMeasurementSubtype(unitOfMeasure.getMeasurementSubtype());
        model.setNumberOfItems(unitOfMeasure.getNumberOfItems());
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