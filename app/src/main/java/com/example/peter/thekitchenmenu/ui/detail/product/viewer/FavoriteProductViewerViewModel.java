package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsRepository;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

public class FavoriteProductViewerViewModel
        extends AndroidViewModel
        implements FavoriteProductsDataSource.GetFavoriteProductCallback {

    private static final String TAG = "tkm-FavProductViewerVM";

    private FavoriteProductsRepository repository;
    private FavoriteProductViewerNavigator navigator;

    private boolean dataIsLoading;
    public final ObservableBoolean isFavorite = new ObservableBoolean();
    public final ObservableField<FavoriteProductEntity> favoriteProduct = new ObservableField<>();
    private boolean favoriteAddedEdited;
    private final SingleLiveEvent<Boolean> setFabIcon = new SingleLiveEvent<>();

    public FavoriteProductViewerViewModel(Application application,
                                          FavoriteProductsRepository repository) {
        super(application);
        this.repository = repository;
    }

    void setNavigator(FavoriteProductViewerNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String productId) {
        if (productId != null) {
            dataIsLoading = true;
            repository.getFavoriteProductByProductId(productId, this);
        }
    }

    @Override
    public void onFavoriteProductLoaded(FavoriteProductEntity favoriteProduct) {
        Log.d(TAG, "onFavoriteProductLoaded: ");
        setFavoriteProduct(favoriteProduct);
    }

    private void setFavoriteProduct(FavoriteProductEntity favoriteProduct) {
        this.favoriteProduct.set(favoriteProduct);
        isFavorite.set(true);
        setFabIcon.setValue(true);
        dataIsLoading = false;
    }

    @Override
    public void onDataNotAvailable() {
        Log.d(TAG, "onDataNotAvailable: ");
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
            repository.deleteFavoriteProduct(favoriteProduct.get().getId());
            favoriteProduct.set(null);
            isFavorite.set(false);
        }
    }

    boolean isFavoriteAddedEdited() {
        return favoriteAddedEdited;
    }
}
