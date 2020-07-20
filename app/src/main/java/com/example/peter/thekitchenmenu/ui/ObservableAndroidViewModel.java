package com.example.peter.thekitchenmenu.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import javax.annotation.Nonnull;

public class ObservableAndroidViewModel extends AndroidViewModel  {

//    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    public ObservableAndroidViewModel(@Nonnull Application application) {
        super(application);
    }

//    @Override
//    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        callbacks.add(callback);
//    }

//    @Override
//    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        callbacks.remove(callback);
//    }
//
//    public void notifyChange() {
//        callbacks.notifyCallbacks(this, 0, null);
//    }

//    public void notifyPropertyChanged(int fieldId) {
//        callbacks.notifyCallbacks(this, fieldId, null);
//    }
}
