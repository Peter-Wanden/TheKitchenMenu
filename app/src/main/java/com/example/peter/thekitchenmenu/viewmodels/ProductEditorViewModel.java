package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.Measurement;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureClassSelector;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import static com.example.peter.thekitchenmenu.data.entity.Product.BASE_SI_UNITS;
import static com.example.peter.thekitchenmenu.data.entity.Product.DESCRIPTION;
import static com.example.peter.thekitchenmenu.data.entity.Product.MADE_BY;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NOTHING_SELECTED;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private MutableLiveData<Product> product = new MutableLiveData<>();
    private Context applicationContext;

    private final ObservableField<String> description = new ObservableField<>();
    private boolean descriptionValidated;
    private final ObservableField<String> madeBy = new ObservableField<>();
    private boolean madeByValidated;
    // TODO - Change category and shelf life to an enum
    private ObservableInt category = new ObservableInt(0);
    private boolean categoryValidated;
    private ObservableInt shelfLife = new ObservableInt(0);
    private boolean shelfLifeValidated;
    private ObservableBoolean multiPack = new ObservableBoolean(false);

    private boolean numberOfItemsInPackValidated;
    private boolean packSizeValidated;

    // Measurements
    private UnitOfMeasure unitOfMeasure;
    private Measurement measurement;

    public ProductEditorViewModel(@NonNull Application application) {

        super(application);
        applicationContext = application;

        unitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                applicationContext, MeasurementSubType.NOTHING_SELECTED);

        measurement = new Measurement();


        Product p = new Product(0,
                "Mars Bar",
                "Mars",
                1,
                false,
                0,
                5,
                0,
                0,
                0,
                "",
                "",
                0,
                0,
                "");
        product.setValue(p);

        if (product.getValue() != null) {

            description.set(product.getValue().getDescription());
            madeBy.set(product.getValue().getMadeBy());
            category.set(product.getValue().getCategory());
            shelfLife.set(product.getValue().getShelfLife());
            multiPack.set(product.getValue().getMultiPack());

            if (product.getValue().getUnitOfMeasureSubType() >
                    MeasurementSubType.NOTHING_SELECTED.ordinal()) {

                unitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                        applicationContext,
                        MeasurementSubType.values()[product.getValue().getUnitOfMeasureSubType()]);

                Log.d(TAG, "ProductEditorViewModel: unit of measure is: " +
                        unitOfMeasure.getMeasurementSubType());

                boolean productValuesAreSet = unitOfMeasure.setValuesFromProduct(product.getValue());

                if (productValuesAreSet) unitOfMeasure.setNewMeasurementValuesTo(measurement);
                else Log.d(TAG, "ProductEditorViewModel: Product values contain errors.");

                Log.d(TAG, "ProductEditorViewModel: Unit of measure set to: " +
                        unitOfMeasure.toString());
                Log.d(TAG, "ProductEditorViewModel: Measurement set to: " + measurement);
            }
        }
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        Log.d(TAG, "setMeasurement: " + measurement.toString());
        this.measurement = measurement;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        Log.d(TAG, "getUnitOfMeasure: " + unitOfMeasure.toString());
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public MutableLiveData<Product> getProduct() {
        return product;
    }

    public void setPackSizeValidated(boolean packSizeValidated) {
        this.packSizeValidated = packSizeValidated;
    }

    @Bindable
    public ObservableField<String> getDescription() {

        if (descriptionValidated && product.getValue() != null) {

            product.getValue().setDescription(description.get());
            printProduct();
        }
        return description;
    }

    @Bindable
    public ObservableField<String> getMadeBy() {

        if (madeByValidated && product.getValue() != null) {

            product.getValue().setMadeBy(madeBy.get());
            printProduct();
        }
        return madeBy;
    }

    @Bindable
    public ObservableInt getCategory() {

        if (category.get() == 0) {

            categoryValidated = false;
            printProduct();

        } else {

            categoryValidated = true;

            if (product.getValue() != null) {

                product.getValue().setCategory(category.get());
                printProduct();
            }
        }
        return category;
    }

    @Bindable
    public ObservableInt getShelfLife() {

        if (shelfLife.get() == NOTHING_SELECTED) {

            shelfLifeValidated = false;

        } else {

            shelfLifeValidated = true;

            if (product.getValue() != null) {

                product.getValue().setShelfLife(shelfLife.get());
                printProduct();
            }
        }
        return shelfLife;
    }

    @Bindable
    public ObservableBoolean getMultiPack() {

        if (product.getValue() != null) {

            product.getValue().setMultiPack(multiPack.get());
            printProduct();
        }
        return multiPack;
    }

    public void setFieldValidation(Pair<String, Boolean> fieldToValidate) {

        assert fieldToValidate.first != null && fieldToValidate.second != null;

        if (fieldToValidate.first.equals(DESCRIPTION)) {
            this.descriptionValidated = fieldToValidate.second;

        } else if (fieldToValidate.first.equals(MADE_BY)) {
            this.madeByValidated = fieldToValidate.second;

        } else if (fieldToValidate.first.equals(BASE_SI_UNITS)) {
            this.packSizeValidated = fieldToValidate.second;
        }
    }

    public void setNumberOfItemsInPackValidated(boolean numberOfItemsInPackValidated) {
        this.numberOfItemsInPackValidated = numberOfItemsInPackValidated;
    }

    // TODO - For dev only.
    private void printProduct() {
        if (product.getValue() != null)
            Log.d(TAG, "printProduct: " + product.getValue().toString());
    }

    private void printUnitOfMeasure() {
        Log.d(TAG, "printUnitOfMeasure: " + unitOfMeasure.toString());
    }
}
