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

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductViewerFragmentBinding;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;

public class FavoriteProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-FavProdViewerFrag";

    private FavoriteProductViewerFragmentBinding binding;
    private FavoriteProductViewerViewModel viewModel;
    private boolean hasEditDeleteMenuItems;

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

        if (getArguments() != null)
            viewModel.start(getArguments().getString(ProductEditorActivity.EXTRA_PRODUCT_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.favorite_product_viewer_fragment,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();
        setupObservers();

        return binding.getRoot();
    }

    private void setViewModel() {
        viewModel = ProductViewerActivity.obtainFavoriteProductViewerViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupObservers(){
        viewModel.getHasOptionsMenuEvent().observe(this, this::hasMenuOptions);
        viewModel.getHasEditDeleteMenuOptions().observe(this, this::hasEditDeleteMenuItems);
    }

    private void hasMenuOptions(boolean hasMenuOptions) {
        setHasOptionsMenu(hasMenuOptions);
    }

    private void hasEditDeleteMenuItems(boolean hasEditDeleteMenuItems) {
        this.hasEditDeleteMenuItems = hasEditDeleteMenuItems;
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite_viewer_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if (hasEditDeleteMenuItems) {
            menu.findItem(R.id.menu_item_edit_favorite).setVisible(true);
            menu.findItem(R.id.menu_item_delete_favorite).setVisible(true);
        }
        else {
            menu.findItem(R.id.menu_item_edit_favorite).setVisible(false);
            menu.findItem(R.id.menu_item_delete_favorite).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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