package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.google.android.gms.common.util.Strings;

public class ProductViewerViewModel
        extends AndroidViewModel
        implements PrimitiveDataSource.GetEntityCallback<ProductEntity>, ProductViewerNavigator{

    private static final String TAG = "tkm-" + ProductViewerViewModel.class.getSimpleName() + ":";

    private Resources resources;
    private ProductViewerNavigator navigator;
    private PrimitiveDataSource<ProductEntity> productEntityDataSource;

    public final ObservableBoolean dataIsLoading = new ObservableBoolean();
    public final ObservableField<ProductEntity> productEntityObservable = new ObservableField<>();
    private boolean dataHasChanged;

    public final ObservableBoolean showPostMessageEvent = new ObservableBoolean();
    public final ObservableField<String> reviewBeforePostMessage = new ObservableField<>();

    // Non-default modes of operation
    private boolean viewOnlyMode;
    private boolean reviewBeforePostMode;

    private final SingleLiveEvent<Boolean> hasOptionsMenuEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> resetOptionsMenu = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> setTitleEvent = new SingleLiveEvent<>();

    public ProductViewerViewModel(Application application,
                                  PrimitiveDataSource<ProductEntity> productEntityDataSource) {
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
                productEntityDataSource.getByDataId(productId, this);
            }
        }
    }

    @Override
    public void onEntityLoaded(ProductEntity entity) {
        dataIsLoading.set(false);
        setProductEntityObservable(entity);
        setupDisplayAsViewer();
    }

    @Override
    public void onDataUnavailable() {
        // This is an error
    }

    private void setProductEntityObservable(ProductEntity productEntityObservable) {
        this.productEntityObservable.set(productEntityObservable);
    }

    void handleActivityResult(int resultCode, Intent data) {
        if (resultCode == ProductEditorActivity.RESULT_ADD_EDIT_PRODUCT_OK) {
            ProductEntity productEntity = data.getParcelableExtra(
                    ProductEditorActivity.EXTRA_PRODUCT_ENTITY);
            start(productEntity);
        }
    }

    private void setupDisplayAsReviewAfterEdit() {
        reviewBeforePostMode = true;
        setTitleEvent.setValue(R.string.activity_title_review_new_product);
        hasOptionsMenuEvent.setValue(true);
        resetOptionsMenu.call();
        showPostMessageEvent.set(true);
        reviewBeforePostMessage.set(resources.getString(R.string.review_before_post_message));
    }

    SingleLiveEvent<Integer> getSetTitleEvent() {
        return setTitleEvent;
    }

    SingleLiveEvent<Void> getResetOptionsMenu() {
        return resetOptionsMenu;
    }

    SingleLiveEvent<Boolean> getHasOptionsMenuEvent() {
        return hasOptionsMenuEvent;
    }

    private void setupDisplayAsViewer() {
        reviewBeforePostMode = false;
        showPostMessageEvent.set(false);

        setTitleEvent.setValue(R.string.activity_title_view_product);
        if (!viewOnlyMode) {
            hasOptionsMenuEvent.setValue(true);
            resetOptionsMenu.call();
        }
    }

    public void setViewOnlyMode(boolean viewOnlyMode) {
        this.viewOnlyMode = viewOnlyMode;
    }

    boolean isReviewBeforePostMode() {
        return reviewBeforePostMode;
    }

    @Override
    public void editProduct(ProductEntity productEntity) {
        navigator.editProduct(productEntity);
    }

    @Override
    public void deleteProduct(String productId) {
        if (showPostMessageEvent.get()) {
            // Product add/edit has not been saved so exit as is.
            productEntityObservable.set(null);
            doneWithProduct(productEntityObservable.get().getDataId());
        }
        else {
            productEntityDataSource.deleteByDataId(productEntityObservable.get().getDataId());
            navigator.deleteProduct(productEntityObservable.get().getDataId());
        }
    }

    @Override
    public void discardProductEdits() {
        // Reloads the product to reset it to its last saved state
        start(productEntityObservable.get().getDataId());
    }

    @Override
    public void doneWithProduct(String productId) {
        navigator.doneWithProduct(productId);
    }

    @Override
    public void postProduct() {
        productEntityDataSource.save(productEntityObservable.get());
        dataHasChanged = true;
        setupDisplayAsViewer();
    }

    void setDataHasChanged(boolean dataHasChanged) {
        this.dataHasChanged = dataHasChanged;
    }

    void upOrBackPressed() {
        if (reviewBeforePostMode) {
            showUnsavedChangesDialog();

        } else if (dataHasChanged) {
            navigator.doneWithProduct(productEntityObservable.get().getDataId());

        } else {
            navigator.discardProductEdits();
        }
    }

    @Override
    public void showUnsavedChangesDialog() {
        navigator.showUnsavedChangesDialog();
    }
}
