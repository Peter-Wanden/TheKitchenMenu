package com.example.peter.thekitchenmenu.ui;

import androidx.lifecycle.ViewModel;

public class ObservableViewModel extends ViewModel  {

//    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    public ObservableViewModel() {
    }

//    @Override
//    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        callbacks.add(callback);
//    }

//    @Override
//    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        callbacks.remove(callback);
//    }

//    public void notifyChange() {
//        callbacks.notifyCallbacks(this, 0, null);
//    }

    public void notifyPropertyChanged(int fieldId) {
//        callbacks.notifyCallbacks(this, fieldId, null);
    }
}
