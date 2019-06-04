package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.ui.catalog.CatalogProductsViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.editor.ProductEditorViewModel;

/**
 * A creator is used to inject the product ID into the ViewModel
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = "tkm-ViewModelFactory";

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;
    private final Application application;
    private final ProductRepository repository;

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application,
                            DatabaseInjection.provideProductsRepository(
                                    application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(Application application, ProductRepository repository) {
        this.application = application;
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(CatalogProductsViewModel.class)) {
            //noinspection unchecked
            return (T) new CatalogProductsViewModel(application, repository);

        } else if (modelClass.isAssignableFrom(ProductEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new ProductEditorViewModel(application, repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
