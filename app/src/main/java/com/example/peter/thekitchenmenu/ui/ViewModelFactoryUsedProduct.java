package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.data.repository.UsedProductRepository;
import com.example.peter.thekitchenmenu.ui.catalog.CatalogProductsViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor.UsedProductEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.UsedProductViewerViewModel;

public class ViewModelFactoryUsedProduct extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = "tkm-VM_FactoryUsedProduct";

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryUsedProduct INSTANCE;
    private final Application application;
    private final UsedProductRepository usedProductRepository;
    private final ProductRepository productRepository;

    private ViewModelFactoryUsedProduct(Application application,
                                        UsedProductRepository usedProductRepository,
                                        ProductRepository productRepository) {
        this.application = application;
        this.usedProductRepository = usedProductRepository;
        this.productRepository = productRepository;
    }

    public static ViewModelFactoryUsedProduct getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryUsedProduct.class) {
                if (INSTANCE == null) INSTANCE = new ViewModelFactoryUsedProduct(
                            application,
                            DatabaseInjection.provideUsedProductsRepository(
                                    application.getApplicationContext()),
                            DatabaseInjection.provideProductsRepository(
                                    application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(UsedProductEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new UsedProductEditorViewModel(application, usedProductRepository);

        } else if (modelClass.isAssignableFrom(UsedProductViewerViewModel.class)) {
            //noinspection unchecked
            return(T) new UsedProductViewerViewModel(application, usedProductRepository);

        } else if (modelClass.isAssignableFrom(CatalogProductsViewModel.class)) {
            //noinspection unchecked
            return(T) new CatalogProductsViewModel(application, usedProductRepository,
                    productRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
