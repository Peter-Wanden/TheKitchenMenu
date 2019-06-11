package com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.UsedProductEditorFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class UsedProductEditorFragment extends Fragment {

    private static final String TAG = "tkm-UsedProductEditFrag";

    public static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";
    public static final String ARGUMENT_USED_PRODUCT_ID = "USED_PRODUCT_ID";

    private UsedProductEditorFragmentBinding binding;
    private UsedProductEditorViewModel viewModel;

    public UsedProductEditorFragment() {
    }

    public static UsedProductEditorFragment newInstance(String productId, String usedProductId) {

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        arguments.putString(ARGUMENT_USED_PRODUCT_ID, usedProductId);
        UsedProductEditorFragment fragment = new UsedProductEditorFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = obtainViewModel();
        loadData();
        subscribeToEvents();
    }

    private UsedProductEditorViewModel obtainViewModel() {
        return UsedProductEditorActivity.obtainUsedProductEditorViewModel(requireActivity());
    }

    private void loadData() {
        if (getArguments() != null)

            if (getArguments().getString(ARGUMENT_USED_PRODUCT_ID) != null) {
                viewModel.start(
                        getArguments().getString(ARGUMENT_PRODUCT_ID),
                        getArguments().getString(ARGUMENT_USED_PRODUCT_ID));
            } else {
                viewModel.start(
                        getArguments().getString(ARGUMENT_PRODUCT_ID),
                        null);
            }
    }

    private void subscribeToEvents() {
        viewModel.getRetailerErrorEvent().observe(this, this::retailerError);
    }

    private void retailerError(String retailerError) {
        binding.editableRetailer.setError(retailerError);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.used_product_editor_fragment,
                container,
                false);

        setBindingInstanceVariables();

        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }
}
