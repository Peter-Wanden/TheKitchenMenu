package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.google.android.gms.common.util.Strings;

public class FavoriteProductViewerViewModel
        extends AndroidViewModel
        implements DataSource.GetEntityCallback<FavoriteProductEntity> {

    private static final String TAG = "tkm-FavProductViewerVM";

    private Resources resources;
    private FavoriteProductViewerNavigator navigator;

    private DataSource<FavoriteProductEntity> favoriteProductEntityDataSource;
    public final ObservableField<FavoriteProductEntity> favoriteProduct = new ObservableField<>();
    private final MutableLiveData<String> productId = new MutableLiveData<>();

    private boolean dataIsLoading;
    private boolean favoriteAddedEdited;
    public final ObservableBoolean isFavorite = new ObservableBoolean();

    public FavoriteProductViewerViewModel(
            Application application,
            DataSource<FavoriteProductEntity> favoriteProductEntityDataSource) {

        super(application);
        resources = application.getResources();
        this.favoriteProductEntityDataSource = favoriteProductEntityDataSource;
    }

    void setNavigator(FavoriteProductViewerNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(ProductEntity productEntity) {

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

    private void setFavoriteProduct(FavoriteProductEntity favoriteProductEntity) {
        this.favoriteProduct.set(favoriteProductEntity);
        isFavorite.set(true);
        dataIsLoading = false;
        productId.setValue(favoriteProductEntity.getProductId());
    }

    public MutableLiveData<String> getProductId() {
        return productId;
    }

    @Override
    public void onDataNotAvailable() {
        favoriteProduct.set(null);
        isFavorite.set(false);
        dataIsLoading = false;
    }

    void handleActivityResult(int resultCode, Intent data) {
        if (resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK) {
            favoriteAddedEdited = true;
            start(data.getStringExtra(FavoriteProductEditorActivity.EXTRA_FAVORITE_PRODUCT_ID));
        }
    }

    public void addFavoriteProduct() {
        navigator.addFavoriteProduct(productId.getValue());
    }

    void editFavoriteProduct() {
        navigator.editFavoriteProduct();
    }

    void deleteFavoriteProduct() {
        if (favoriteProduct.get() != null) {
            favoriteProductEntityDataSource.deleteById(favoriteProduct.get().getId());
            isFavorite.set(false);
            favoriteProduct.set(null);
        }
    }

    boolean isFavoriteAddedEdited() {
        return favoriteAddedEdited;
    }
}
