package com.example.peter.thekitchenmenu.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.ProductNumericValidationHandler;
import com.example.peter.thekitchenmenu.utils.ProductTextValidationHandler;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        setAlphaValidationHandler();
        setNumericValidationHandler();

        return rootView;
    }

    private void setViewModel() {

        ProductEditorViewModel productEditorViewModel = ViewModelProviders.of(
                getActivity()).
                get(ProductEditorViewModel.class);

        productEditor.setProductEditorViewModel(productEditorViewModel);
    }

    private void setupSpinners() {
        setupCategorySpinner();
        setupShelfLifeSpinner();
        setupUnitOfMeasureSpinner();
    }

    private void setupCategorySpinner() {
        productEditor.spinnerCategory.setAdapter(ArrayAdapter.createFromResource(
                getActivity(), R.array.product_category_options,
                R.layout.list_item_spinner));
        setSpinnerListeners(productEditor.spinnerCategory);
    }

    private void setupShelfLifeSpinner() {
        productEditor.spinnerShelfLife.setAdapter(ArrayAdapter.createFromResource(
                getActivity(),
                R.array.shelf_life_options,
                R.layout.list_item_spinner));
        setSpinnerListeners(productEditor.spinnerShelfLife);
    }

    private void setupUnitOfMeasureSpinner() {
        productEditor.spinnerUnitOfMeasure.setAdapter(getUnitOfMeasureSpinnerAdapter());
        productEditor.spinnerUnitOfMeasure.setPrompt(getResources().getString(R.string.please_select));
        setSpinnerListeners(productEditor.spinnerUnitOfMeasure);
    }

    private SpinnerAdapter getUnitOfMeasureSpinnerAdapter() {
        return new UnitOfMeasureSpinnerAdapter(getActivity(),unitOfMeasureArrayList());
    }

    private void setSpinnerListeners(Spinner spinner) {

        spinner.setOnFocusChangeListener((view, b) -> {

            if (view.hasFocus()) {
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

    private ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureArrayList() {

        List<String> unitOfMeasureHeaders = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_headers
        ));
        List<String> unitOfMeasureMassMetric = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_options_mass_metric
        ));
        List<String> unitOfMeasureMassImperial = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_options_mass_imperial
        ));
        List<String> unitOfMeasureVolumeMetric = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_options_volume_metric
        ));
        List<String> unitOfMeasureVolumeImperial = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_options_volume_imperial
        ));
        List<String> unitOfMeasureCount = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_options_count
        ));

        ArrayList<UnitOfMeasureSpinnerItem> categoryList = new ArrayList<>();

        for (int header = 0; header < unitOfMeasureHeaders.size(); header ++) {

            UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                    SpinnerItemType.SECTION_HEADER,
                    unitOfMeasureHeaders.get(header)
            );
            categoryList.add(headerItem);

            if (header == 0) {

                for (int massMetric = 0; massMetric < unitOfMeasureMassMetric.size(); massMetric++) {

                    UnitOfMeasureSpinnerItem massItem = new UnitOfMeasureSpinnerItem(
                            SpinnerItemType.LIST_ITEM,
                            unitOfMeasureMass.get(massMetric)
                    );
                    categoryList.add(massItem);
                }

            } else if (header == 1) {

                for (int volume = 0; volume < unitOfMeasureVolume.size(); volume ++) {
                    UnitOfMeasureSpinnerItem volumeItem = new UnitOfMeasureSpinnerItem(
                            SpinnerItemType.LIST_ITEM,
                            unitOfMeasureVolume.get(volume)
                    );
                    categoryList.add(volumeItem);
                }

            } else if (header == 2) {

                for (int count = 0; count < unitOfMeasureCount.size(); count ++) {

                    UnitOfMeasureSpinnerItem countItem = new UnitOfMeasureSpinnerItem(
                            SpinnerItemType.LIST_ITEM,
                            unitOfMeasureCount.get(count)
                    );
                    categoryList.add(countItem);
                }
            }
        }
        return categoryList;
    }

    private void setAlphaValidationHandler() {

        ProductTextValidationHandler textValidationHandler =
                new ProductTextValidationHandler();

        textValidationHandler.
                setBinding(getActivity(),
                productEditor);

        productEditor.setTextValidation(textValidationHandler);
    }

    private void setNumericValidationHandler() {

        ProductNumericValidationHandler numericValidationHandler =
                new ProductNumericValidationHandler();

        numericValidationHandler.setBinding(
                getActivity(),
                productEditor);

        productEditor.setNumericValidation(numericValidationHandler);
    }
}
