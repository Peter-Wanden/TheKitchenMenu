package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsRepository;
import com.example.peter.thekitchenmenu.ui.catalog.product.ProductCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteeditor.FavoriteProductEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.FavoriteProductViewerViewModel;

public class ViewModelFactoryFavoriteProduct extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = "tkm-VM_FactoryFavProduct";

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryFavoriteProduct INSTANCE;
    private final Application application;
    private final FavoriteProductsRepository favoriteProductsRepository;
    private final ProductRepository productRepository;

    private ViewModelFactoryFavoriteProduct(Application application,
                                            FavoriteProductsRepository favoriteProductsRepository,
                                            ProductRepository productRepository) {
        this.application = application;
        this.favoriteProductsRepository = favoriteProductsRepository;
        this.productRepository = productRepository;
    }

    public static ViewModelFactoryFavoriteProduct getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryFavoriteProduct.class) {
                if (INSTANCE == null) INSTANCE = new ViewModelFactoryFavoriteProduct(
                            application,
                            DatabaseInjection.provideFavoritesProductsRepository(
                                    application.getApplicationContext()),
                            DatabaseInjection.provideProductsRepository(
                                    application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(FavoriteProductEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new FavoriteProductEditorViewModel(application, favoriteProductsRepository);

        } else if (modelClass.isAssignableFrom(FavoriteProductViewerViewModel.class)) {
            //noinspection unchecked
            return(T) new FavoriteProductViewerViewModel(application, favoriteProductsRepository);

        } else if (modelClass.isAssignableFrom(ProductCatalogViewModel.class)) {
            //noinspection unchecked
            return(T) new ProductCatalogViewModel(application, favoriteProductsRepository,
                    productRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
