package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.ui.catalog.product.ProductCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductIdentityViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.ProductViewerViewModel;
import com.example.peter.thekitchenmenu.ui.utils.TextValidator;

public class ViewModelFactoryProduct extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryProduct INSTANCE;
    private final Application application;
    private final DataSource<ProductEntity> productEntityDataSource;

    private ViewModelFactoryProduct(Application application,
                                    DataSource<ProductEntity> productEntityDataSource) {
        this.application = application;
        this.productEntityDataSource = productEntityDataSource;
    }

    public static ViewModelFactoryProduct getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryProduct.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryProduct(
                        application,
                        DatabaseInjection.provideProductDataSource(
                                application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ProductEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new ProductEditorViewModel(application);
        }
        else if (modelClass.isAssignableFrom(ProductViewerViewModel.class)) {
            //noinspection unchecked
            return (T) new ProductViewerViewModel(application, productEntityDataSource);
        }
        else if (modelClass.isAssignableFrom(ProductCatalogViewModel.class)) {
            //noinspection unchecked
            return (T) new ProductCatalogViewModel(application);
        }
        else if (modelClass.isAssignableFrom(ProductIdentityViewModel.class)) {
            //noinspection unchecked
            return(T) new ProductIdentityViewModel(application,
                    new TextValidator(application.getResources()));
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
