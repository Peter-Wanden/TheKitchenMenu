package com.example.peter.thekitchenmenu.viewmodels;



import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.VmProd;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ViewModelProdDetail extends AndroidViewModel {

    MutableLiveData<VmProd> mVmProd;


    public ViewModelProdDetail(@NonNull Application application) {
        super(application);
    }
}
