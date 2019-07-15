package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductViewerDetailFragmentBinding;

public class ProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-ProductViewerFrag";

    private static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";

    private ProductViewerDetailFragmentBinding binding;
    private ProductViewerViewModel productViewerViewModel;

    public static ProductViewerFragment newInstance(String productId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        ProductViewerFragment fragment = new ProductViewerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        productViewerViewModel.start(getArguments().getString(ARGUMENT_PRODUCT_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
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
        productViewerViewModel = ProductViewerActivity.obtainProductViewerViewModel(
                requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModelProduct(productViewerViewModel);
    }

    private void setupObservers() {
        productViewerViewModel.getHasOptionsMenuEvent().observe(
                this, ProductViewerFragment.this::setOptionsMenu);
    }

    private void setOptionsMenu(boolean hasOptionsMenu) {
        setHasOptionsMenu(hasOptionsMenu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product_viewer_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_product:
                productViewerViewModel.editProduct();
                return true;
            case R.id.menu_item_delete_product:
                productViewerViewModel.deleteProduct();
                return true;
        }
        return false;
    }
}
