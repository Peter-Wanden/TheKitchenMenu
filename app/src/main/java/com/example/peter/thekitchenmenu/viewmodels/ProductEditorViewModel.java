package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureClassSelector;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import static com.example.peter.thekitchenmenu.data.entity.Product.*;
import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private MutableLiveData<Product> product = new MutableLiveData<>();
    private Context applicationContext;

    private final ObservableField<String> description = new ObservableField<>();
    private boolean descriptionValidated;
    private final ObservableField<String> madeBy = new ObservableField<>();
    private boolean madeByValidated;
    private ObservableInt category = new ObservableInt();
    private boolean categoryValidated;
    private ObservableInt shelfLife = new ObservableInt();
    private boolean shelfLifeValidated;
    private ObservableBoolean multiPack = new ObservableBoolean();
    private ObservableInt numberOfItemsInPack = new ObservableInt();
    private boolean numberOfItemsInPackValidated;
    private ObservableInt unitOfMeasure = new ObservableInt();
    private boolean unitOfMeasureValidated;
    private final ObservableInt packSizeInt = new ObservableInt();
    private boolean packSizeValidated;

    public ProductEditorViewModel(@NonNull Application application) {
        super(application);
        this.applicationContext = application;

        Product p = new Product(0,
                "",
                "",
                0,
                false,
                0,
                0,
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
            numberOfItemsInPack.set(product.getValue().getNumberOfItems());
            unitOfMeasure.set(product.getValue().getUnitOfMeasure());
            packSizeInt.set(product.getValue().getPackSize());
        }
    }

    public void setProduct(MutableLiveData<Product> product) {
        this.product = product;
    }

    @Bindable
    public ObservableField<String> getDescription() {
        if (descriptionValidated && product.getValue() != null) {
            product.getValue().setDescription(description.get());
        }
        return description;
    }

    @Bindable
    public ObservableField<String> getMadeBy() {
        if (madeByValidated && product.getValue() != null) {
            product.getValue().setMadeBy(madeBy.get());
        }
        return madeBy;
    }

    @Bindable
    public ObservableInt getCategory() {
        if (category.get() == 0) {
            categoryValidated = false;

        } else {
            categoryValidated = true;

            if (product.getValue() != null) {
                product.getValue().setCategory(category.get());
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
            }
        }
        return shelfLife;
    }

    @Bindable
    public ObservableBoolean getMultiPack() {
        if (product.getValue() != null) {
            product.getValue().setMultiPack(multiPack.get());
        }
        return multiPack;
    }

    @Bindable
    public ObservableInt getNumberOfItemsInPack() {
        if (product.getValue() != null &&
                numberOfItemsInPack.get() >= MULTI_PACK_MINIMUM_NO_OF_ITEMS) {
            product.getValue().setNumberOfItems(numberOfItemsInPack.get());
            numberOfItemsInPackValidated = true;
        }
        return numberOfItemsInPack;
    }

    @Bindable
    public ObservableInt getUnitOfMeasure() {
        if (product.getValue() != null && unitOfMeasure.get() > NOTHING_SELECTED) {
            if (packSizeInt.get() > 0)
            product.getValue().setUnitOfMeasure(unitOfMeasure.get());
            unitOfMeasureValidated = true;
        }
        return unitOfMeasure;
    }

    @Bindable
    public ObservableInt getPackSizeInt() {
        if (product.getValue() != null && unitOfMeasure.get() != NOTHING_SELECTED) {
            UnitOfMeasure unitOfMeasureClass = UnitOfMeasureClassSelector.getUnitOfMeasureClass(
                    applicationContext, unitOfMeasure.get());

            product.getValue().setPackSize(unitOfMeasureClass.convertToBaseUnit(packSizeInt.get()));

            Log.d(TAG, "getPackSizeInt: product.packSizeInt is: " + product.getValue().getPackSize());
        }
        return packSizeInt;
    }

    public void setPackSizeInt(int packSizeInt) {
        if (this.packSizeInt.get() != packSizeInt) {
            this.packSizeInt.set(packSizeInt);
        }
    }

    public MutableLiveData<Product> getProduct() {
        return product;
    }

    public void setFieldValidation(Pair<String, Boolean> fieldToValidate) {

        assert fieldToValidate.first != null && fieldToValidate.second != null;

        if (fieldToValidate.first.equals(DESCRIPTION)) {
            this.descriptionValidated = fieldToValidate.second;

        } else if (fieldToValidate.first.equals(MADE_BY)) {
            this.madeByValidated = fieldToValidate.second;

        } else if (fieldToValidate.first.equals(PACK_SIZE)) {
            this.packSizeValidated = fieldToValidate.second;
        }
    }

    // TODO - For dev only.
    public void printProduct() {
        if (product.getValue() != null)
        Log.d(TAG, "printProduct: " + product.getValue().toString());
    }
}
