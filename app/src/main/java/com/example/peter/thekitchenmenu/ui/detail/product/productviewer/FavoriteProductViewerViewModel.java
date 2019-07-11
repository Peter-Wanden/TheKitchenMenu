package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
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
    private final SingleLiveEvent<Void> deleteFavoriteEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> editFavoriteEvent = new SingleLiveEvent<>();

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

    void favoritePopUpMenuItemSelected(int menuItemId) {
        if (menuItemId == R.id.menu_item_edit_favorite) {
            Log.d(TAG, "onMenuItemClick: edit favorite selected");
            // TODO - delete from the database and update the display
        }
        if(menuItemId == R.id.menu_item_delete_favorite) {
            Log.d(TAG, "onMenuItemClick: delete favorite selected");
            // TODO - Launch the favorite editor activity for result to favorite viewer
        }
    }

    void handleActivityResult(int requestCode, int resultCode, String productId) {
        Log.d(TAG, "handleActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);
        if (FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT == requestCode) {
            if (resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK) {
                favoriteAddedEdited = true;
                start(productId);
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
            isFavorite.set(false);
            favoriteProduct.set(null);
        }
    }

    boolean isFavoriteAddedEdited() {
        return favoriteAddedEdited;
    }
}
