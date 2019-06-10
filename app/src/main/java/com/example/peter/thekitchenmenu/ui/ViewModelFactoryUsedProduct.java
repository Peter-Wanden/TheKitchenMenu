package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.UsedProductRepository;
import com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor.UsedProductEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.UsedProductViewerViewModel;

public class ViewModelFactoryUsedProduct extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = "tkm-VM_FactoryUsedProduct";

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryUsedProduct INSTANCE;
    private final Application application;
    private final UsedProductRepository repository;

    private ViewModelFactoryUsedProduct(Application application, UsedProductRepository repository) {
        this.application = application;
        this.repository = repository;
    }

    public static ViewModelFactoryUsedProduct getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryUsedProduct.class) {
                if (INSTANCE == null) INSTANCE = new ViewModelFactoryUsedProduct(
                            application,
                            DatabaseInjection.provideUsedProductsRepository(
                                    application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(UsedProductEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new UsedProductEditorViewModel(application, repository);

        } else if (modelClass.isAssignableFrom(UsedProductViewerViewModel.class)) {
            //noinspection unchecked
            return(T) new UsedProductViewerViewModel(application, repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
