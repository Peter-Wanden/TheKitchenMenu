package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.databinding.ProductViewerDetailFragmentBinding;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;

import javax.annotation.Nonnull;

public class ProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-" + ProductViewerFragment.class.getSimpleName() + ":";

    private static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";
    static final String ARGUMENT_PRODUCT_ENTITY = "PRODUCT_ENTITY";

    private ProductViewerDetailFragmentBinding binding;
    private ProductViewerViewModel productViewerViewModel;

    public static ProductViewerFragment newInstance(String productId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        ProductViewerFragment fragment = new ProductViewerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static ProductViewerFragment newInstance(ProductEntity productEntity) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_PRODUCT_ENTITY, productEntity);
        ProductViewerFragment fragment = new ProductViewerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments().containsKey(ARGUMENT_PRODUCT_ID)) {
            productViewerViewModel.start(getArguments().getString(ARGUMENT_PRODUCT_ID));
        }

        else if (getArguments().containsKey(ARGUMENT_PRODUCT_ENTITY)) {
            productViewerViewModel.start(
                    (ProductEntity) getArguments().getParcelable(ARGUMENT_PRODUCT_ENTITY));
            getArguments().remove(ARGUMENT_PRODUCT_ENTITY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_viewer_detail_fragment,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();
        setupObservers();

        return binding.getRoot();
    }

    private void setViewModel() {
        if (requireActivity() instanceof ProductViewerActivity)
            productViewerViewModel = ProductViewerActivity.obtainProductViewerViewModel(
                    requireActivity());

        if (requireActivity() instanceof FavoriteProductEditorActivity)
            productViewerViewModel = FavoriteProductEditorActivity.obtainProductViewerViewModel(
                    requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(productViewerViewModel);
    }

    private void setupObservers() {
        productViewerViewModel.getHasOptionsMenuEvent().observe(
                getViewLifecycleOwner(), ProductViewerFragment.this::setOptionsMenu);

        productViewerViewModel.getResetOptionsMenu().observe(
                getViewLifecycleOwner(), aVoid -> requireActivity().invalidateOptionsMenu());
    }

    private void setOptionsMenu(boolean hasOptionsMenu) {
        setHasOptionsMenu(hasOptionsMenu);
    }

    @Override
    public void onCreateOptionsMenu(@Nonnull Menu menu, @Nonnull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product_viewer_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@Nonnull Menu menu) {
        if (productViewerViewModel.isReviewBeforePostMode()) {
            menu.findItem(R.id.menu_item_post_product).setVisible(true);
            menu.findItem(R.id.menu_item_discard_changes).setVisible(true);

            menu.findItem(R.id.menu_item_done).setVisible(false);
            menu.findItem(R.id.menu_item_delete_product).setVisible(false);
        } else {
            menu.findItem(R.id.menu_item_done).setVisible(true);
            menu.findItem(R.id.menu_item_delete_product).setVisible(true);

            menu.findItem(R.id.menu_item_post_product).setVisible(false);
            menu.findItem(R.id.menu_item_discard_changes).setVisible(false);
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_product:
                productViewerViewModel.editProduct(
                        productViewerViewModel.productEntityObservable.get());
                return true;

            case R.id.menu_item_delete_product:
                productViewerViewModel.deleteProduct(
                        productViewerViewModel.productEntityObservable.get().getDataId());
                return true;

            case R.id.menu_item_post_product:
                productViewerViewModel.postProduct();
                return true;

            case R.id.menu_item_discard_changes:
                productViewerViewModel.discardProductEdits();
                return true;

            case R.id.menu_item_done:
                productViewerViewModel.doneWithProduct(
                        productViewerViewModel.productEntityObservable.get().getDataId());
                return true;
        }
        return false;
    }
}
