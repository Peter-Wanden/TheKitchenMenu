package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
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

    public final ObservableBoolean dataIsLoading = new ObservableBoolean();
    public final ObservableField<ProductEntity> productEntityObservable = new ObservableField<>();
    private boolean dataHasChanged;
    private ProductEntity productEntityPreEditing;

    public final ObservableBoolean showPostMessageEvent = new ObservableBoolean();
    public final ObservableField<String> reviewBeforePostMessage = new ObservableField<>();

    // Non-default modes of operation
    private boolean viewOnlyMode;
    private boolean postMode;

    private final SingleLiveEvent<Boolean> hasOptionsMenuEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> resetOptionsMenu = new SingleLiveEvent<>();
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
                dataIsLoading.set(true);
                productEntityDataSource.getById(productId, this);
            }
        }
    }

    @Override
    public void onEntityLoaded(ProductEntity product) {
        dataIsLoading.set(false);
        setProductEntityObservable(product);
        setupDisplayAsViewer();
    }

    @Override
    public void onDataNotAvailable() {
        // This is an error
    }

    private void setProductEntityObservable(ProductEntity productEntityObservable) {
        this.productEntityObservable.set(productEntityObservable);
    }

    void handleActivityResult(int resultCode, Intent data) {
        if (resultCode == ProductEditorActivity.RESULT_ADD_EDIT_PRODUCT_OK) {
            dataHasChanged = true;
            ProductEntity productEntity = data.getParcelableExtra(
                    ProductEditorActivity.EXTRA_PRODUCT_ENTITY);
            start(productEntity);
        }
    }

    private void setupDisplayAsReviewAfterEdit() {
        postMode = true;
        dataHasChanged = true;
        setTitleEvent.setValue(resources.getString(R.string.activity_title_review_new_product));
        hasOptionsMenuEvent.setValue(true);
        resetOptionsMenu.call();
        showPostMessageEvent.set(true);
        reviewBeforePostMessage.set(resources.getString(R.string.review_before_post_message));
    }

    SingleLiveEvent<String> getSetTitleEvent() {
        return setTitleEvent;
    }

    SingleLiveEvent<Void> getResetOptionsMenu() {
        return resetOptionsMenu;
    }

    SingleLiveEvent<Boolean> getHasOptionsMenuEvent() {
        return hasOptionsMenuEvent;
    }

    private void setupDisplayAsViewer() {
        postMode = false;
        showPostMessageEvent.set(false);

        setTitleEvent.setValue(resources.getString(R.string.activity_title_view_product));
        if (!viewOnlyMode) {
            hasOptionsMenuEvent.setValue(true);
            resetOptionsMenu.call();
        }
    }

    public void setViewOnlyMode(boolean viewOnlyMode) {
        this.viewOnlyMode = viewOnlyMode;
    }

    boolean isPostMode() {
        return postMode;
    }

    @Override
    public void editProduct(ProductEntity productEntity) {
        productEntityPreEditing = productEntity;
        navigator.editProduct(productEntity);
    }

    @Override
    public void deleteProduct(String productId) {
        if (showPostMessageEvent.get()) {
            // Product add/edit has not been saved so exit as is.
            productEntityObservable.set(null);
            doneWithProduct(productEntityObservable.get().getId());
        }
        else {
            productEntityDataSource.deleteById(productEntityObservable.get().getId());
            navigator.deleteProduct(productEntityObservable.get().getId());
        }
    }

    @Override
    public void discardChanges() {

    }

    @Override
    public void doneWithProduct(String productId) {
        navigator.doneWithProduct(productId);
    }

    @Override
    public void postProduct() {
        productEntityDataSource.save(productEntityObservable.get());
        setupDisplayAsViewer();
    }

    boolean isDataHasChanged() {
        return dataHasChanged;
    }
}
