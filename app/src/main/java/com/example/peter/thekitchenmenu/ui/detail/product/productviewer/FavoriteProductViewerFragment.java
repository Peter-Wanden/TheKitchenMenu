package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;

import javax.annotation.Nonnull;

public class FavoriteProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-" + FavoriteProductViewerFragment.class.getSimpleName() +
            ":";

//    private FavoriteProductViewerFragmentBinding binding;
    private FavoriteProductViewerViewModel viewModel;

    static FavoriteProductViewerFragment newInstance(String productId) {

        Bundle arguments = new Bundle();
        arguments.putString(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        FavoriteProductViewerFragment fragment = new FavoriteProductViewerFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments().containsKey(ProductEditorActivity.EXTRA_PRODUCT_ID))
            viewModel.start(getArguments().getString(
                    ProductEditorActivity.EXTRA_PRODUCT_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        binding = DataBindingUtil.inflate(
//                inflater,
//                R.layout.favorite_product_viewer_fragment,
//                container,
//                false);

        setViewModel();
        setBindingInstanceVariables();
        setupObservers();

//        return binding.getRoot();
        return null;
    }

    private void setViewModel() {
        viewModel = ProductViewerActivity.obtainFavoriteProductViewerViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
//        binding.setViewModel(viewModel);
    }

    private void setupObservers() {
        viewModel.getHasOptionsMenuEvent().observe(
                getViewLifecycleOwner(),
                aVoid -> hasMenuOptions());
    }

    private void hasMenuOptions() {
//        setHasOptionsMenu(viewModel.isFavorite.get());
    }

    @Override
    public void onCreateOptionsMenu(@Nonnull Menu menu, @Nonnull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite_viewer_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_favorite:
                viewModel.editFavoriteProduct();
                return true;
            case R.id.menu_item_delete_favorite:
                viewModel.deleteFavoriteProduct();
                return true;
        }
        return false;
    }
}