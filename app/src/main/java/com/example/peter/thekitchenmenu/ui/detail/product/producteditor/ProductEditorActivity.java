package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.ui.UnsavedChangesDialogFragment;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.ProductViewerActivity;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.ui.imageeditor.ImageEditorViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

public class ProductEditorActivity extends AppCompatActivity implements AddEditProductNavigator {

    private static final String TAG = "tkm-EditorActivity";

    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";
    public static final String EXTRA_PRODUCT_ENTITY = "EXTRA_PRODUCT_ENTITY";
    public static final int REQUEST_ADD_EDIT_PRODUCT = 5;
    public static final int RESULT_ADD_EDIT_PRODUCT_OK = RESULT_FIRST_USER + 1;
    public static final int RESULT_ADD_EDIT_PRODUCT_CANCELLED = RESULT_FIRST_USER + 2;

    private ProductEditorBinding productEditorBinding;

    private ProductEditorViewModel productEditorViewModel;
    private ImageEditorViewModel imageEditorViewModel;
    private ProductIdentityViewModel identityEditorViewModel;
    private ProductMeasurementViewModel measurementEditorViewModel;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Log.d(TAG, "onSupportNavigateUp: clicked");
        return true;
    }

    @Override
    public void onBackPressed() {
        productEditorViewModel.backPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        setViewModels();
        setEntityObserver();
        setModelObservers();
        setModelValidationObservers();
        setActivityObservers();
        subscribeToNavigationChanges();
        start();
    }

    private void initialiseBindings() {
        productEditorBinding = DataBindingUtil.setContentView(this, R.layout.product_editor);
        productEditorBinding.setLifecycleOwner(this);
    }

    private void setViewModels() {
        productEditorViewModel = obtainViewModel(this);
        productEditorViewModel.setNavigator(this);

        imageEditorViewModel = ViewModelProviders.of(
                this).get(ImageEditorViewModel.class);

        identityEditorViewModel = ViewModelProviders.of(
                this).get(ProductIdentityViewModel.class);

        measurementEditorViewModel = ViewModelProviders.of(
                this).get(ProductMeasurementViewModel.class);
    }

    private static ProductEditorViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactoryProduct factory = ViewModelFactoryProduct.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProductEditorViewModel.class);
    }

    private void setEntityObserver() {
        // Calve up the incoming entity into models and set them to their respective view models
        final Observer<ProductEntity> productObserver = productEntity -> {

            if (productEntity != null) {

                ImageModel imageModel = new ImageModel(
                        productEntity.getRemoteLargeImageUri(),
                        null,
                        productEntity.getRemoteMediumImageUri(),
                        null,
                        productEntity.getRemoteSmallImageUri(),
                        null,
                        productEntity.getWebImageUrl()
                );
                imageEditorViewModel.getExistingImageModel().setValue(imageModel);

                ProductIdentityModel identityModel = new ProductIdentityModel(
                        productEntity.getDescription(),
                        productEntity.getShoppingListItemName(),
                        productEntity.getCategory(),
                        productEntity.getShelfLife()
                );
                identityEditorViewModel.setIdentityModel(identityModel);

                ProductMeasurementModel measurementModel = new ProductMeasurementModel(
                        MeasurementSubtype.values()[productEntity.getUnitOfMeasureSubtype()],
                        productEntity.getNumberOfProducts(),
                        productEntity.getBaseUnits()
                );
                measurementEditorViewModel.setMeasurementModel(measurementModel);
            }
        };

        productEditorViewModel.getUneditedProductEntity().observe(this, productObserver);
    }

    private void setModelObservers() {
        // Observe the models within their respective view models, set changes to the edited entity
        final Observer<ImageModel> imageModelObserver = imageModel ->
                productEditorViewModel.setUpdatedImageModel(imageModel);
        imageEditorViewModel.getExistingImageModel().observe(
                this, imageModelObserver);

        final Observer<ProductIdentityModel> identityModelObserver = IdentityModel ->
                productEditorViewModel.setUpdatedIdentityModel(IdentityModel);
        identityEditorViewModel.getExistingIdentityModel().observe(
                this, identityModelObserver);

        final Observer<ProductMeasurementModel> measurementModelObserver = measurementModel ->
                productEditorViewModel.setUpdatedMeasurementModel(measurementModel);
        measurementEditorViewModel.getMeasurementModel().observe(
                this, measurementModelObserver);

    }

    private void setModelValidationObservers() {
        // Once a model is validated by its view model, let the main view model know
        identityEditorViewModel.getIdentityModelValidEvent().observe(
                this, identityModelIsValid ->
                        productEditorViewModel.setIdentityModelIsValid(identityModelIsValid));

        measurementEditorViewModel.getMeasurementModelIsValidEvent().observe(
                this, measurementModelIsValid ->
                        productEditorViewModel.setMeasurementModelIsValid(measurementModelIsValid));
    }

    private void setActivityObservers() {
        productEditorViewModel.getSetActivityTitleEvent().observe(
                this, this::setTitle);
    }

    private void subscribeToNavigationChanges() {
        productEditorViewModel.getUpdateOptionsMenuEvent().observe(
                this, aVoid -> invalidateOptionsMenu());

        productEditorViewModel.getShowUnsavedChangesDialogEvent().observe(
                this, aVoid -> showUnsavedChangesDialog());
    }

    private void start() {
        if (getIntent().hasExtra(EXTRA_PRODUCT_ENTITY)) {
            Log.d(TAG, "start: entity=" + getIntent().getParcelableExtra(EXTRA_PRODUCT_ENTITY).toString());
            productEditorViewModel.start(getIntent().getParcelableExtra(EXTRA_PRODUCT_ENTITY));
        }
        else
            productEditorViewModel.start();
    }

    private void setupToolbar() {
        setSupportActionBar(productEditorBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_product_editor_action_review).setVisible(
                productEditorViewModel.isShowReviewButton());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_product_editor_action_review) {
            productEditorViewModel.createOrUpdateProduct();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void reviewEditedProduct(ProductEntity productEntity) {
        Log.d(TAG, "reviewEditedProduct: entity=" + productEntity.toString());
        setResult(RESULT_ADD_EDIT_PRODUCT_OK,
                new Intent().putExtra(EXTRA_PRODUCT_ENTITY, productEntity));
        finish();
    }

    @Override
    public void reviewNewProduct(ProductEntity productEntity) {
        Log.d(TAG, "reviewNewProduct: entity=" + productEntity.toString());
        Intent intent = new Intent(this, ProductViewerActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ENTITY, productEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    @Override
    public void cancelEditing() {
        setResult(RESULT_ADD_EDIT_PRODUCT_CANCELLED);
        finish();
    }

    @Override
    protected void onDestroy() {
        productEditorViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    private void showUnsavedChangesDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment previousDialog = getSupportFragmentManager().findFragmentByTag(
                UnsavedChangesDialogFragment.TAG);

        if (previousDialog != null)
            ft.remove(previousDialog);
        ft.addToBackStack(null);

        UnsavedChangesDialogFragment dialogFragment = UnsavedChangesDialogFragment.newInstance(
                ProductEditorActivity.this.getTitle().toString());
        dialogFragment.show(ft, UnsavedChangesDialogFragment.TAG);
    }
}
