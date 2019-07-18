package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.content.Intent;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.google.android.gms.common.util.Strings;

public class FavoriteProductViewerViewModel
        extends AndroidViewModel
        implements DataSource.GetEntityCallback<FavoriteProductEntity> {

    private static final String TAG = "tkm-FavProductViewerVM";

    private FavoriteProductViewerNavigator navigator;

    private FavoriteProductsDataSource favoriteProductEntityDataSource;
    private final ObservableBoolean dataIsLoading = new ObservableBoolean();
    public final ObservableField<FavoriteProductEntity> favoriteProduct = new ObservableField<>();
    public final ObservableBoolean isFavorite = new ObservableBoolean();

    private final SingleLiveEvent<Boolean> hasOptionsMenuEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> hasEditDeleteMenuOptions = new SingleLiveEvent<>();

    private boolean favoriteAddedEdited;
    private String productId;

    public FavoriteProductViewerViewModel(
            Application application,
            FavoriteProductsDataSource favoriteProductEntityDataSource) {

        super(application);
        this.favoriteProductEntityDataSource = favoriteProductEntityDataSource;
    }

    void setNavigator(FavoriteProductViewerNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String productId) {
        if (Strings.emptyToNull(productId) != null) {
            this.productId = productId;
            dataIsLoading.set(true);
            favoriteProductEntityDataSource.getByProductId(productId, this);
            setDisplayAsViewAndEdit();
        }
    }

    @Override
    public void onEntityLoaded(FavoriteProductEntity favoriteProductEntity) {
        dataIsLoading.set(false);
        setFavoriteProduct(favoriteProductEntity);
    }

    private void setFavoriteProduct(FavoriteProductEntity favoriteProductEntity) {
        this.favoriteProduct.set(favoriteProductEntity);
        isFavorite.set(true);
        dataIsLoading.set(false);
    }

    @Override
    public void onDataNotAvailable() {
        favoriteProduct.set(null);
        isFavorite.set(false);
        dataIsLoading.set(false);
    }

    void handleActivityResult(int resultCode, Intent data) {
        if (resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK) {
            favoriteAddedEdited = true;
            start(data.getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID));
        }
    }

    public void addFavoriteProduct() {
        navigator.addFavoriteProduct(productId);
    }

    void editFavoriteProduct() {
        navigator.editFavoriteProduct(productId);
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

    private void setDisplayAsViewAndEdit() {
        if (isFavorite.get()) {
            hasOptionsMenuEvent.setValue(true);
            hasEditDeleteMenuOptions.setValue(true);
        }
        else
            hasOptionsMenuEvent.setValue(false);
    }

    SingleLiveEvent<Boolean> getHasOptionsMenuEvent() {
        return hasOptionsMenuEvent;
    }

    SingleLiveEvent<Boolean> getHasEditDeleteMenuOptions() {
        return hasEditDeleteMenuOptions;
    }
}
