package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductUserDataEditorBinding;
import com.example.peter.thekitchenmenu.viewmodels.ProductUserDataEditorViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProductUserDataEditor extends Fragment {

    private static final String TAG = "ProductUserDataEditor";

    private ProductUserDataEditorBinding userDataEditorBinding;
    private ProductUserDataEditorViewModel userDataEditorViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        userDataEditorBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_user_data_editor,
                container,
                false);

        View rootView = userDataEditorBinding.getRoot();
        userDataEditorBinding.setLifecycleOwner(this);

        setViewModel();
        setValidationHandler();
        setBindingInstanceVariables();

        return rootView;
    }

    private void setViewModel() {

        userDataEditorViewModel = ViewModelProviders.of(requireActivity()).
                get(ProductUserDataEditorViewModel.class);
    }

    private void setValidationHandler() {

        userDataEditorBinding.setTextValidation(userDataEditorViewModel.getTextValidationHandler());
    }

    private void setBindingInstanceVariables() {

        userDataEditorBinding.setUserDataModel(userDataEditorViewModel.getUserDataModel());
    }}
