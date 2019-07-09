package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.google.android.gms.common.util.Strings;

public class FavoriteProductViewerViewModel
        extends AndroidViewModel
        implements DataSource.GetEntityCallback<FavoriteProductEntity> {

    private static final String TAG = "tkm-FavProductViewerVM";

    private DataSource<FavoriteProductEntity> favoriteProductEntityDataSource;
    private FavoriteProductViewerNavigator navigator;

    private boolean dataIsLoading;
    public final ObservableBoolean isFavorite = new ObservableBoolean();
    public final ObservableField<FavoriteProductEntity> favoriteProduct = new ObservableField<>();
    private boolean favoriteAddedEdited;
    private final SingleLiveEvent<Boolean> setFabIcon = new SingleLiveEvent<>();

    public FavoriteProductViewerViewModel(
            Application application,
            DataSource<FavoriteProductEntity> favoriteProductEntityDataSource) {

        super(application);
        this.favoriteProductEntityDataSource = favoriteProductEntityDataSource;
    }

    void setNavigator(FavoriteProductViewerNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String favoriteProductId) {
        if (Strings.emptyToNull(favoriteProductId) != null) {
            dataIsLoading = true;
            favoriteProductEntityDataSource.getById(favoriteProductId, this);
        }
    }

    @Override
    public void onEntityLoaded(FavoriteProductEntity favoriteProductEntity) {
        setFavoriteProduct(favoriteProductEntity);
    }

    private void setFavoriteProduct(FavoriteProductEntity favoriteProduct) {
        this.favoriteProduct.set(favoriteProduct);
        isFavorite.set(true);
        setFabIcon.setValue(true);
        dataIsLoading = false;
    }

    @Override
    public void onDataNotAvailable() {
        favoriteProduct.set(null);
        isFavorite.set(false);
        setFabIcon.setValue(false);
        dataIsLoading = false;
    }

    SingleLiveEvent<Boolean> getSetFabIcon() {
        return setFabIcon;
    }

    void onFabClicked() {
        if (isFavorite.get())
            navigator.editFavoriteProduct();
        else
            navigator.addFavoriteProduct();
    }

    void handleActivityResult(int requestCode, int resultCode) {
        Log.d(TAG, "handleActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);
        if (FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT == requestCode) {
            if (resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK) {
                favoriteAddedEdited = true;
            }
        }
    }

    public void addFavoriteProduct() {
        navigator.addFavoriteProduct();
    }

    void deleteFavoriteProduct() {
        Log.d(TAG, "deleteFavoriteProduct=" + favoriteProduct.get());
        if (favoriteProduct.get() != null) {
            favoriteProductEntityDataSource.deleteById(favoriteProduct.get().getId());
            favoriteProduct.set(null);
            isFavorite.set(false);
        }
    }

    boolean isFavoriteAddedEdited() {
        return favoriteAddedEdited;
    }
}
