package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.model.VmProd;

public class ViewModelProdDetail extends AndroidViewModel {

    MutableLiveData<VmProd> mVmProd;


    public ViewModelProdDetail(@NonNull Application application) {
        super(application);
    }
}
