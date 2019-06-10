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
import com.example.peter.thekitchenmenu.databinding.UsedProductViewerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UsedProductViewerFragment extends Fragment {

    public static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";

    private UsedProductViewerBinding binding;
    private UsedProductViewerViewModel viewModel;

    public static UsedProductViewerFragment newInstance(String productId) {

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        UsedProductViewerFragment fragment = new UsedProductViewerFragment();
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
                R.layout.used_product_viewer,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();
        setupFab();

        return binding.getRoot();
    }

    private void setViewModel() {
        viewModel = ProductViewerActivity.obtainUsedProductViewerViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.product_viewer_activity_fab);
        fab.setOnClickListener(v -> viewModel.addUsedProduct());
    }
}
