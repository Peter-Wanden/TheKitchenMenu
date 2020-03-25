package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.content.Intent;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.product.DataSourceFavoriteProducts;
import com.example.peter.thekitchenmenu.ui.UnsavedChangesDialogFragment;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.google.android.gms.common.util.Strings;

public class FavoriteProductViewerViewModel
        extends AndroidViewModel
        implements PrimitiveDataSource.GetEntityCallback<FavoriteProductEntity> {

    private static final String TAG = "tkm-" + FavoriteProductViewerViewModel.class.getSimpleName()
            + ":";

    private FavoriteProductViewerNavigator navigator;

    private DataSourceFavoriteProducts favoriteProductEntityDataSource;
    public final ObservableBoolean dataIsLoading = new ObservableBoolean();
    public final ObservableField<FavoriteProductEntity> favoriteProduct = new ObservableField<>();
    public final ObservableBoolean isFavorite = new ObservableBoolean();

    private final SingleLiveEvent<Void> hasOptionsMenuEvent = new SingleLiveEvent<>();

    // Tells any listeners if this view model has added or edited any data
    private final SingleLiveEvent<Boolean> favoriteAddedEdited = new SingleLiveEvent<>();
    private String productId;

    public FavoriteProductViewerViewModel(
            Application application,
            DataSourceFavoriteProducts favoriteProductEntityDataSource) {

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
        }
    }

    @Override
    public void onEntityLoaded(FavoriteProductEntity entity) {
        dataIsLoading.set(false);
        setFavoriteProduct(entity);
    }

    private void setFavoriteProduct(FavoriteProductEntity favoriteProductEntity) {
        this.favoriteProduct.set(favoriteProductEntity);
        isFavorite.set(true);
        hasOptionsMenuEvent.call();
    }

    @Override
    public void onDataUnavailable() {
        dataIsLoading.set(false);
        setAddFavoriteMode();
    }

    private void setAddFavoriteMode() {
        favoriteProduct.set(null);
        isFavorite.set(false);
        hasOptionsMenuEvent.call();
    }

    void handleActivityResult(int resultCode, Intent data) {
        if (resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK) {
            favoriteAddedEdited.setValue(true);
            start(data.getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID));

        } else if (resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_CANCELED ||
                resultCode == UnsavedChangesDialogFragment.RESULT_CANCELED_AFTER_EDIT) {
            favoriteAddedEdited.setValue(false);
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
            hasOptionsMenuEvent.call();
        }
    }

    SingleLiveEvent<Void> getHasOptionsMenuEvent() {
        return hasOptionsMenuEvent;
    }

    SingleLiveEvent<Boolean> isFavoriteAddedEdited() {
        return favoriteAddedEdited;
    }
}