package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.os.Bundle;
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

    private static final String TAG = "tkm-FavProductViewerFrag";

    public static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";

    private FavoriteProductViewerBinding binding;
    private FavoriteProductViewerViewModel viewModel;
    private FloatingActionButton fab;

    public static FavoriteProductViewerFragment newInstance(String productId) {

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        FavoriteProductViewerFragment fragment = new FavoriteProductViewerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start(getArguments().getString(ARGUMENT_PRODUCT_ID));
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

    private void setFabIcon(boolean setEditIconIfTrue) {
        if (setEditIconIfTrue) fab.setImageResource(R.drawable.ic_edit_white);
        else fab.setImageResource(R.drawable.ic_format_list_bulleted_white);
    }
}