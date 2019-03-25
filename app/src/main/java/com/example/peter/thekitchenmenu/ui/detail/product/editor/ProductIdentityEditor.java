package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductIdentityEditorBinding;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;
import com.example.peter.thekitchenmenu.viewmodels.ProductIdentityViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
                R.layout.product_identity_editor, container,
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

        identityEditorBinding.setIdentityModel(identityViewModel.getIdentityModel());
    }

    private void setupSpinners() {

        setupCategorySpinner();
        setUpShelfLifeSpinner();
    }

    private void setupCategorySpinner() {

        identityEditorBinding.spinnerCategory.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(), R.array.product_category_options, R.layout.list_item_spinner));

        setSpinnerListeners(identityEditorBinding.spinnerCategory);
    }

    private void setUpShelfLifeSpinner() {

        identityEditorBinding.spinnerShelfLife.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.shelf_life_options,
                R.layout.list_item_spinner));

        setSpinnerListeners(identityEditorBinding.spinnerShelfLife);
    }

    private void setSpinnerListeners(Spinner spinner) {

        spinner.setOnFocusChangeListener((view, b) -> {

            if (view.hasFocus()) {

                ShowHideSoftInput.showKeyboard(view, false);
                // Avoids WindowManager$BadTokenException by waiting for the screen to redraw.
                new Handler().postDelayed(view::performClick, 100);
            }
        });

        spinner.setOnTouchListener((view, motionEvent) -> {

            if (view.getVisibility() == View.VISIBLE) {

                ShowHideSoftInput.showKeyboard(view, false);
                view.performClick();
            }
            return true;
        });

        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }
}
