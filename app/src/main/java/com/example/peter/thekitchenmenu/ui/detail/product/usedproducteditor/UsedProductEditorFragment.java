package com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.UsedProductEditorBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class UsedProductEditorFragment extends Fragment {

    private static final String TAG = "UsedProductEditorFragment";

    public static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";

    private UsedProductEditorBinding binding;
    private UsedProductEditorViewModel viewModel;

    public static UsedProductEditorFragment newInstance(String productId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        UsedProductEditorFragment fragment = new UsedProductEditorFragment();
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
                R.layout.used_product_editor,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();
        setValidationHandler();

        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    private void setViewModel() {
    }

    private void setValidationHandler() {
        binding.setTextValidation(viewModel.getTextValidationHandler());
    }

    private void setBindingInstanceVariables() {
        binding.setUserDataModel(viewModel.getUserDataModel());
    }
}
