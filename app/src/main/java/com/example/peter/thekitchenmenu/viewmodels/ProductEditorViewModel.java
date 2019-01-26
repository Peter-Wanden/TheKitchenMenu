package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import static com.example.peter.thekitchenmenu.data.entity.Product.*;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private MutableLiveData<Product> product = new MutableLiveData<>();;
    private boolean descriptionValidated;
    private boolean madeByValidated;

    private final ObservableField<String> description = new ObservableField<>();
    private final ObservableField<String> madeBy = new ObservableField<>();
    private ObservableInt category = new ObservableInt();
    private ObservableInt unitOfMeasure = new ObservableInt();
    private ObservableBoolean multiPack = new ObservableBoolean();
    private ObservableInt numberOfItemsInPack = new ObservableInt();

    public ProductEditorViewModel(@NonNull Application application) {
        super(application);

        Product p = new Product(1,
                "Cornflakes",
                "Kellogs",
                2,
                false,
                4,
                12,
                500,
                5,
                2.99,
                "q344ufhfk7",
                "https://image.url.com",
                123456789,
                123456789,
                "skdjf496dflekgg");
        product.setValue(p);

        description.set(product.getValue().getDescription());
        madeBy.set(product.getValue().getMadeBy());


    }

    public void setProduct(MutableLiveData<Product> product) {
        this.product = product;
    }

    @Bindable
    public ObservableField<String> getDescription() {
        return description;
    }

    @Bindable
    public ObservableField<String> getMadeBy() {
        return madeBy;
    }

    @Bindable
    public ObservableInt getCategory() {
        return category;
    }

    public void setCategory(ObservableInt category) {
        this.category = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public ObservableInt getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(ObservableInt unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        notifyPropertyChanged(BR.unitOfMeasure);
    }

    @Bindable
    public ObservableBoolean getMultiPack() {
        return multiPack;
    }

    public void setMultiPack(ObservableBoolean multiPack) {
        this.multiPack = multiPack;
        notifyPropertyChanged(BR.multiPack);
    }

    @Bindable
    public ObservableInt getNumberOfItemsInPack() {
        Log.d(TAG, "getNumberOfItemsInPack: " + numberOfItemsInPack.get());
        return numberOfItemsInPack;
    }

    public void setNumberOfItemsInPack(ObservableInt numberOfItemsInPack) {
        Log.d(TAG, "setNumberOfItemsInPack: " + numberOfItemsInPack.get());
        this.numberOfItemsInPack = numberOfItemsInPack;
        notifyPropertyChanged(BR.numberOfItemsInPack);
    }

    public MutableLiveData<Product> getProduct() {
        Log.d(TAG, "getProduct: product values are:" + product.getValue().toString());
        return product;
    }

    public void setFieldValidation(Pair<String, Boolean> fieldToValidate) {

        assert fieldToValidate.first != null && fieldToValidate.second != null;

        if (fieldToValidate.first.equals(DESCRIPTION)) {
            this.descriptionValidated = fieldToValidate.second;

        } else if (fieldToValidate.first.equals(MADE_BY)) {
            this.madeByValidated = fieldToValidate.second;
        }
    }

    public void printProduct() {
        Log.d(TAG, "printProduct: " + product.getValue().toString());
    }
}
