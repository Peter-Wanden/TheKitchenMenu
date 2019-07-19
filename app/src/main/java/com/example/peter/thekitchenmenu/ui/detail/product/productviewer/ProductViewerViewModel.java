package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.google.android.gms.common.util.Strings;

public class ProductViewerViewModel
        extends AndroidViewModel
        implements DataSource.GetEntityCallback<ProductEntity>, ProductViewerNavigator{

    private static final String TAG = "tkm-ProductViewerVM";

    private Resources resources;
    private ProductViewerNavigator navigator;
    private DataSource<ProductEntity> productEntityDataSource;

    private boolean dataIsLoading;
    public final ObservableField<ProductEntity> productEntityObservable = new ObservableField<>();
    public final ObservableBoolean showPostMessageEvent = new ObservableBoolean();
    public final ObservableField<String> reviewBeforePostMessage = new ObservableField<>();
    private boolean viewOnly;

    private final SingleLiveEvent<Boolean> hasOptionsMenuEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> setMenuOptionsToPostEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> setTitleEvent = new SingleLiveEvent<>();

    public ProductViewerViewModel(Application application,
                                  DataSource<ProductEntity> productEntityDataSource) {
        super(application);
        this.productEntityDataSource = productEntityDataSource;
        resources = application.getResources();
    }

    void setNavigator(ProductViewerNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(ProductEntity productEntity) {
        productEntityObservable.set(productEntity);
        setupDisplayAsReviewAfterEdit();
    }

    public void start(String productId) {
        if (!showPostMessageEvent.get()) {
            if (!Strings.isEmptyOrWhitespace(productId)) {
                dataIsLoading = true;
                productEntityDataSource.getById(productId, this);
            }
        }
    }

    SingleLiveEvent<String> getSetTitleEvent() {
        return setTitleEvent;
    }

    SingleLiveEvent<Boolean> getSetMenuOptionsToPostEvent() {
        return setMenuOptionsToPostEvent;
    }

    @Override
    public void onEntityLoaded(ProductEntity product) {
        Log.d(TAG, "onEntityLoaded: ");
        setProductEntityObservable(product);
        dataIsLoading = false;
        setupDisplayAsViewer();
    }

    private void setProductEntityObservable(ProductEntity productEntityObservable) {
        this.productEntityObservable.set(productEntityObservable);
    }

    @Override
    public void onDataNotAvailable() {
        productEntityObservable.set(null);
        dataIsLoading = false;
    }

    void postProduct() {
        Log.d(TAG, "postProduct: ");
        productEntityDataSource.save(productEntityObservable.get());
        setupDisplayAsViewer();
    }

    SingleLiveEvent<Boolean> getHasOptionsMenuEvent() {
        return hasOptionsMenuEvent;
    }

    void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ProductEditorActivity.RESULT_ADD_EDIT_PRODUCT_OK) {

            ProductEntity productEntity = data.getParcelableExtra(
                    ProductEditorActivity.EXTRA_PRODUCT_ENTITY);
            start(productEntity);
        }

        else if (requestCode == FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT) {
            Log.d(TAG, "handleActivityResult: ");
            setupDisplayAsViewer();
        }
    }

    private void setupDisplayAsReviewAfterEdit() {
        Log.d(TAG, "setupDisplayAsReviewAfterEdit: ");
        setTitleEvent.setValue(resources.getString(R.string.activity_title_review_new_product));
        hasOptionsMenuEvent.setValue(true);
        setMenuOptionsToPostEvent.setValue(true);
        showPostMessageEvent.set(true);
        reviewBeforePostMessage.set(resources.getString(R.string.review_before_post_message));
    }

    private void setupDisplayAsViewer() {
        Log.d(TAG, "setupDisplayAsViewer: ");
        setTitleEvent.setValue(resources.getString(R.string.activity_title_view_product));
        showPostMessageEvent.set(false);

        if (!viewOnly) {
            hasOptionsMenuEvent.setValue(true);
            setMenuOptionsToPostEvent.setValue(false);
        }
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
    }

    void editProduct() {
        editProduct(productEntityObservable.get());
    }

    @Override
    public void editProduct(ProductEntity productEntity) {
        navigator.editProduct(productEntity);
    }

    void deleteProduct() {
        if (showPostMessageEvent.get()) {
            // Product add/edit has not been saved so exit as is.
            productEntityObservable.set(null);
            doneWithProduct();
        }
        else {
            productEntityDataSource.deleteById(productEntityObservable.get().getId());
            navigator.deleteProduct(productEntityObservable.get().getId());
        }
    }

    @Override
    public void deleteProduct(String productId) {

    }

    void doneWithProduct() {
        if (productEntityObservable.get() != null) {
            doneWithProduct(productEntityObservable.get().getId());
        }
        else
            doneWithProduct(null);
    }

    @Override
    public void doneWithProduct(String productId) {
        navigator.doneWithProduct(productId);
    }
}
