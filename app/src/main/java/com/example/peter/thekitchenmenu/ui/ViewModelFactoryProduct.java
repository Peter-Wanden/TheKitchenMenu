package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.ui.catalog.product.ProductCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductIdentityViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.ProductViewerViewModel;
import com.example.peter.thekitchenmenu.ui.utils.TextValidator;

import javax.annotation.Nonnull;

public class ViewModelFactoryProduct extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryProduct INSTANCE;
    private final Application application;
    private final PrimitiveDataSource<ProductEntity> productEntityDataSource;

    private ViewModelFactoryProduct(Application application,
                                    PrimitiveDataSource<ProductEntity> productEntityDataSource) {
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

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ProductEditorViewModel.class)) {
            return (T) new ProductEditorViewModel(application);
        }
        else if (modelClass.isAssignableFrom(ProductViewerViewModel.class)) {
            return (T) new ProductViewerViewModel(application, productEntityDataSource);
        }
        else if (modelClass.isAssignableFrom(ProductCatalogViewModel.class)) {
            return (T) new ProductCatalogViewModel(application);
        }
        else if (modelClass.isAssignableFrom(ProductIdentityViewModel.class)) {
            return(T) new ProductIdentityViewModel(application,
                    new TextValidator(application.getResources()));
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
