package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.databinding.ProductIdentityEditorBinding;
import com.example.peter.thekitchenmenu.viewmodels.ProductIdentityViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProductIdentityEditor extends Fragment {

    private static final String TAG = "ProductIdentityEditor";

    private ProductIdentityEditorBinding identityEditorBinding;
    private ProductIdentityViewModel identityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        identityEditorBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_identity_editor,
                container,
                false);

        View rootView = identityEditorBinding.getRoot();
        identityEditorBinding.setLifecycleOwner(this);

        setViewModel();
        setValidationHandler();
        setBindingInstanceVariables();
        setupSpinners();

        return rootView;
    }

    private void setViewModel() {
        identityViewModel = ViewModelProviders.of(requireActivity()).
                get(ProductIdentityViewModel.class);
    }

    private void setValidationHandler() {
        identityEditorBinding.setTextValidation(identityViewModel.getTextValidationHandler());
    }

    private void setBindingInstanceVariables() {
        identityEditorBinding.setIdentityModel(identityViewModel.getNewIdentityModel());
    }

    private void setupSpinners() {
        setupCategorySpinner();
        setUpShelfLifeSpinner();
    }

    private void setupCategorySpinner() {
        identityEditorBinding.spinnerCategory.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(), R.array.product_category_options, R.layout.list_item_spinner));
    }

    private void setUpShelfLifeSpinner() {
        identityEditorBinding.spinnerShelfLife.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.shelf_life_options,
                R.layout.list_item_spinner));
    }
}
