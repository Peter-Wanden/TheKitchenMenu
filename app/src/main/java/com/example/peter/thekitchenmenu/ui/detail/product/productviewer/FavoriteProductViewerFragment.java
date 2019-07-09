package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductViewerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FavoriteProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-FavProdViewerFrag";

    private static final String ARGUMENT_FAVORITE_PRODUCT_ID = "PRODUCT_ID";

    private FavoriteProductViewerBinding binding;
    private FavoriteProductViewerViewModel viewModel;
    private FloatingActionButton fab;

    static FavoriteProductViewerFragment newInstance(String favoriteProductId) {

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_FAVORITE_PRODUCT_ID, favoriteProductId);
        FavoriteProductViewerFragment fragment = new FavoriteProductViewerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null)
            viewModel.start(getArguments().getString(ARGUMENT_FAVORITE_PRODUCT_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.favorite_product_viewer,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();
        setupFab();
        setHasOptionsMenu(true);
        subscribeToNavigationChanges();

        return binding.getRoot();
    }

    private void setViewModel() {
        viewModel = ProductViewerActivity.obtainFavoriteProductViewerViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupFab() {
        fab = getActivity().findViewById(R.id.product_viewer_activity_fab);
        fab.setOnClickListener(v -> viewModel.onFabClicked());
    }

    private void subscribeToNavigationChanges() {
        viewModel.getSetFabIcon().observe(this, this::setFabIcon);
    }

    // todo - move to viewModel, see TODO
    private void setFabIcon(boolean setEditIcon) {
        if (setEditIcon)
            fab.setImageResource(R.drawable.ic_edit_white);
        else
            fab.setImageResource(R.drawable.ic_list_add);
    }
}