package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.Product;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProductMainViewModel extends AndroidViewModel {

    private static final String TAG = "ProductMainViewModel";

    private MutableLiveData<String> title;

    public ProductMainViewModel(@NonNull Application application) {
        super(application);

        title = new MutableLiveData<>();
        title.setValue(application.getString(R.string.activity_title_edit_product));
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public void onFabClick() {
    }
}
