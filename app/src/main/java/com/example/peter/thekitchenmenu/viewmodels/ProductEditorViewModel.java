package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureClassSelector;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import static com.example.peter.thekitchenmenu.data.entity.Product.BASE_SI_UNITS;
import static com.example.peter.thekitchenmenu.data.entity.Product.DESCRIPTION;
import static com.example.peter.thekitchenmenu.data.entity.Product.MADE_BY;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NOTHING_SELECTED;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private MutableLiveData<Product> product = new MutableLiveData<>();
    private Context applicationContext;

    private final ObservableField<String> description = new ObservableField<>();
    private boolean descriptionValidated;
    private final ObservableField<String> madeBy = new ObservableField<>();
    private boolean madeByValidated;
    // TODO - Change category to an enum
    private ObservableInt category = new ObservableInt(0);
    private boolean categoryValidated;
    private ObservableInt shelfLife = new ObservableInt(0);
    private boolean shelfLifeValidated;
    private ObservableField<MeasurementSubType> unitOfMeasureSubtype =
            new ObservableField<>(MeasurementSubType.NOTHING_SELECTED);
    private boolean unitOfMeasureValidated;
    private boolean numberOfItemsInPackValidated;
    private boolean packSizeValidated;

    // Measurements
    private ObservableBoolean multiPack = new ObservableBoolean(false);
    private ObservableInt numberOfItemsInPack = new ObservableInt(0);
    private UnitOfMeasure unitOfMeasure;

    private ObservableInt packMeasurementOne = new ObservableInt(0);
    private ObservableInt packMeasurementTwo = new ObservableInt(0);
    private ObservableInt itemMeasurementOne = new ObservableInt(0);
    private ObservableInt itemMeasurementTwo = new ObservableInt(0);

    public ProductEditorViewModel(@NonNull Application application) {
        super(application);
        applicationContext = application;

        Product p = new Product(0,
                "Cake!",
                "Cake factory",
                1,
                true,
                4,
                5,
                1200,
                1,
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

            if (product.getValue().getUnitOfMeasureSubType() > 0) {

                unitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                        applicationContext,
                        MeasurementSubType.values()[product.getValue().getUnitOfMeasureSubType()]);
                unitOfMeasureSubtype.set(unitOfMeasure.getSubType());

                if (multiPack.get()) {

                    unitOfMeasure.setNumberOfItems(product.getValue().getNumberOfItems());
                    numberOfItemsInPack.set(unitOfMeasure.getNumberOfItems());
                }

                boolean baseSiIsSet = unitOfMeasure.setBaseSiUnits(product.getValue().getBaseSiUnits());

                if (baseSiIsSet) {

                    packMeasurementOne.set(unitOfMeasure.getPackMeasurementOne());
                    packMeasurementTwo.set(unitOfMeasure.getPackMeasurementTwo());

                    if (multiPack.get()) {

                        itemMeasurementOne.set(unitOfMeasure.getItemMeasurementOne());
                        itemMeasurementTwo.set(unitOfMeasure.getItemMeasurementTwo());
                    }
                }
            }
        }
    }

    public MutableLiveData<Product> getProduct() {
        return product;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
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

    @Bindable
    public ObservableInt getNumberOfItemsInPack() {

        if (product.getValue() != null &&
                numberOfItemsInPack.get() >= MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

            product.getValue().setNumberOfItems(numberOfItemsInPack.get());
            numberOfItemsInPackValidated = true;
            printProduct();
        }
        return numberOfItemsInPack;
    }

    @Bindable
    public ObservableField<MeasurementSubType> getUnitOfMeasureSubtype() {
        Log.d(TAG, "getUnitOfMeasureSubtype: called");

        if (product.getValue() != null) {

            UnitOfMeasure newUnitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                    applicationContext, unitOfMeasureSubtype.get());

            if (unitOfMeasure.getSubType() == newUnitOfMeasure.getSubType()) {

                // Actions for a new unit of measure to replace nothing
                if (unitOfMeasure.getSubType() == MeasurementSubType.NOTHING_SELECTED) {

                    unitOfMeasure = newUnitOfMeasure;

                    if (multiPack.get() &&
                            numberOfItemsInPack.get() >= MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

                        unitOfMeasure.setNumberOfItems(numberOfItemsInPack.get());

                    }

                    // Actions for converting an old to a new unit of measure
                } else if (unitOfMeasure.getType() == newUnitOfMeasure.getType()) {

                    newUnitOfMeasure.setBaseSiUnits(unitOfMeasure.getBaseSiUnits());
                    unitOfMeasure = newUnitOfMeasure;

                    if (numberOfItemsInPack.get() >= MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

                        unitOfMeasure.setNumberOfItems(numberOfItemsInPack.get());

                    }

                    packMeasurementOne.set(unitOfMeasure.getPackMeasurementOne());
                    packMeasurementTwo.set(unitOfMeasure.getPackMeasurementTwo());
                    itemMeasurementOne.set(unitOfMeasure.getItemMeasurementOne());
                    itemMeasurementTwo.set(unitOfMeasure.getItemMeasurementTwo());

                    // Actions for replacing inconvertible unit of measure types
                } else {

                    unitOfMeasure = newUnitOfMeasure;

                    if (numberOfItemsInPack.get() >= MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

                        unitOfMeasure.setNumberOfItems(numberOfItemsInPack.get());
                    }

                    packMeasurementOne.set(0);
                    packMeasurementTwo.set(0);
                    itemMeasurementOne.set(0);
                    itemMeasurementTwo.set(0);
                }

                product.getValue().setUnitOfMeasureSubType(unitOfMeasureSubtype.get().ordinal());
            }
        }
        printProduct();
        return unitOfMeasureSubtype;
    }

    // TODO - Make unit of measure observable OR a LiveData that updates automatically
    @Bindable
    public ObservableInt getPackMeasurementOne() {
        return packMeasurementOne;
    }

    @Bindable
    public ObservableInt getPackMeasurementTwo() {

        unitOfMeasure.setPackMeasurementTwo(packMeasurementTwo.get());
        packMeasurementTwo.set(unitOfMeasure.getPackMeasurementTwo());
        product.getValue().setBaseSiUnits(unitOfMeasure.getBaseSiUnits());
        Log.d(TAG, "getPackMeasurementTwo: Pack two is: " + packMeasurementTwo.get());
        printProduct();
        return packMeasurementTwo;
    }

    @Bindable
    public ObservableInt getItemMeasurementOne() {

        unitOfMeasure.setItemMeasurementOne(itemMeasurementOne.get());
        itemMeasurementOne.set(unitOfMeasure.getItemMeasurementOne());
        product.getValue().setBaseSiUnits(unitOfMeasure.getBaseSiUnits());
        Log.d(TAG, "getItemMeasurementOne: Item one is: " + itemMeasurementOne.get());
        printProduct();
        return itemMeasurementOne;
    }

    @Bindable
    public ObservableInt getItemMeasurementTwo() {

        unitOfMeasure.setItemMeasurementTwo(itemMeasurementTwo.get());
        itemMeasurementTwo.set(unitOfMeasure.getItemMeasurementTwo());
        product.getValue().setBaseSiUnits(unitOfMeasure.getBaseSiUnits());
        Log.d(TAG, "getItemMeasurementTwo: Item two is: " + itemMeasurementTwo.get());
        printProduct();
        return itemMeasurementTwo;
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

    // TODO - For dev only.
    private void printProduct() {
        if (product.getValue() != null)
            Log.d(TAG, "printProduct: " + product.getValue().toString());
    }
}
