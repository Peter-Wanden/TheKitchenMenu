package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.databinding.ProductIdentityEditorBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProductIdentityEditorFragment extends Fragment {

    private static final String TAG = "ProductIdentityEditorFragment";

    private ProductIdentityEditorBinding identityBinding;
    private ProductIdentityViewModel identityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        identityBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_identity_editor,
                container,
                false);

        View rootView = identityBinding.getRoot();
        identityBinding.setLifecycleOwner(this);

        setViewModel();
        setObservers();
        setValidationHandler();
        setBindingInstanceVariables();
        setupSpinners();

        return rootView;
    }

    private void setViewModel() {
        identityViewModel = ViewModelProviders.of(requireActivity()).
                get(ProductIdentityViewModel.class);
    }

    private void setObservers() {

        final Observer<ProductIdentityModel> identityModelObserver = identityModel ->
                identityBinding.setIdentityModel(identityModel);

        identityViewModel.getExistingIdentityModel().observe(this, identityModelObserver);
    }

    private void setValidationHandler() {
        identityBinding.setTextValidation(identityViewModel.getTextValidationHandler());
    }

    private void setBindingInstanceVariables() {
        identityBinding.setIdentityModel(identityViewModel.getUpdatedIdentityModel());
    }

    private void setupSpinners() {
        setupCategorySpinner();
        setUpShelfLifeSpinner();
    }

    private void setupCategorySpinner() {
        identityBinding.spinnerCategory.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(), R.array.product_category_options, R.layout.list_item_spinner));
    }

    private void setUpShelfLifeSpinner() {
        identityBinding.spinnerShelfLife.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.shelf_life_options,
                R.layout.list_item_spinner));


    }
}
