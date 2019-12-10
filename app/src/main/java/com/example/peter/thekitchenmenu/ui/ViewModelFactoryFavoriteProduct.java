package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.DataSourceFavoriteProducts;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.FavoriteProductViewerViewModel;
import com.example.peter.thekitchenmenu.ui.utils.TextValidator;

public class ViewModelFactoryFavoriteProduct extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryFavoriteProduct INSTANCE;
    private final Application application;
    private final DataSourceFavoriteProducts favoriteProductEntityDataSource;

    private ViewModelFactoryFavoriteProduct(
            Application application,
            DataSourceFavoriteProducts favoriteProductEntityDataSource) {

        this.application = application;
        this.favoriteProductEntityDataSource = favoriteProductEntityDataSource;
    }

    public static ViewModelFactoryFavoriteProduct getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryFavoriteProduct.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryFavoriteProduct(
                            application,
                            DatabaseInjection.provideFavoritesProductsDataSource(
                                    application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(FavoriteProductEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new FavoriteProductEditorViewModel(
                    application, favoriteProductEntityDataSource,
                    new TextValidator(application.getResources()));
        }
        else if (modelClass.isAssignableFrom(FavoriteProductViewerViewModel.class)) {
            //noinspection unchecked
            return (T) new FavoriteProductViewerViewModel(
                    application, favoriteProductEntityDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
