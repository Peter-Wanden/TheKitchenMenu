package com.example.peter.thekitchenmenu.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProductEditor extends Fragment {

    private static final String TAG = "ProductEditor";

    private ProductEditorBinding productEditor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        productEditor = DataBindingUtil.inflate(
                inflater,
                R.layout.product_editor,
                container,
                false);

        View rootView = productEditor.getRoot();
        productEditor.setLifecycleOwner(this);

        setViewModel();
        setupSpinners();
        setFieldValidationHandler();

        return rootView;
    }

    private void setViewModel() {
        ProductEditorViewModel productEditorViewModel = ViewModelProviders.of(
                getActivity()).
                get(ProductEditorViewModel.class);

        productEditor.setProductEditorViewModel(productEditorViewModel);
    }

    private void setupSpinners() {
        productEditor.spinnerCategory.setAdapter(ArrayAdapter.createFromResource(
                getActivity(),
                R.array.product_category_options,
                R.layout.list_item_spinner));
        setSpinnerListeners(productEditor.spinnerCategory);

        productEditor.spinnerShelfLife.setAdapter(ArrayAdapter.createFromResource(
                getActivity(),
                R.array.shelf_life_options,
                R.layout.list_item_spinner));
        setSpinnerListeners(productEditor.spinnerShelfLife);

        productEditor.spinnerUnitOfMeasure.setAdapter(ArrayAdapter.createFromResource(
                getActivity(),
                R.array.unit_of_measure_options,
                R.layout.list_item_spinner));
        setSpinnerListeners(productEditor.spinnerUnitOfMeasure);
    }

    private void setSpinnerListeners(Spinner spinner) {
        spinner.setOnFocusChangeListener((view, b) -> {
            if(view.hasFocus()) {
                ShowHideSoftInput.showKeyboard(view, false);
                view.performClick();
            }
        });
        spinner.setOnTouchListener((view, motionEvent) -> {
            ShowHideSoftInput.showKeyboard(view, false);
            view.performClick();
            return true;
        });
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

    private void setFieldValidationHandler() {
        ProductValidationHandler validationHandler = new ProductValidationHandler();
        validationHandler.setBinding(
                getActivity(),
                productEditor);
        productEditor.setValidation(validationHandler);
    }
}
