package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.ui.catalog.CatalogProductsViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.ProductViewerViewModel;

/**
 * A creator is used to inject the product ID into the ViewModel
 */
public class ViewModelFactoryProduct extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = "tkm-VM_FactoryProduct";

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryProduct INSTANCE;
    private final Application application;
    private final ProductRepository repository;

    private ViewModelFactoryProduct(Application application, ProductRepository repository) {
        this.application = application;
        this.repository = repository;
    }

    public static ViewModelFactoryProduct getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryProduct.class) {
                if (INSTANCE == null) INSTANCE = new ViewModelFactoryProduct(
                        application,
                        DatabaseInjection.provideProductsRepository(
                                application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(CatalogProductsViewModel.class)) {
            //noinspection unchecked
            return (T) new CatalogProductsViewModel(application, repository);

        } else if (modelClass.isAssignableFrom(ProductEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new ProductEditorViewModel(application, repository);

        } else if (modelClass.isAssignableFrom(ProductViewerViewModel.class)) {
            //noinspection unchecked
            return (T) new ProductViewerViewModel(application, repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
