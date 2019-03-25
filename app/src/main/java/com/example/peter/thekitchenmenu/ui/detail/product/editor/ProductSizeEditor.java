package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductSizeEditorBinding;
import com.example.peter.thekitchenmenu.ui.detail.SpinnerItemType;
import com.example.peter.thekitchenmenu.ui.detail.UnitOfMeasureSpinnerAdapter;
import com.example.peter.thekitchenmenu.ui.detail.UnitOfMeasureSpinnerItem;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementType;
import com.example.peter.thekitchenmenu.viewmodels.ProductSizeViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProductSizeEditor extends Fragment {

    private static final String TAG = "ProductSizeEditor";

    private ProductSizeEditorBinding sizeEditor;
    private ProductSizeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        sizeEditor = DataBindingUtil.inflate(
                inflater,
                R.layout.product_size_editor,
                container,
                false);

        View rootView = sizeEditor.getRoot();
        sizeEditor.setLifecycleOwner(this);

        setViewModelToBinding();
        setValidationHandlersToBinding();
        setBindingInstanceVariables();
        setupUnitOfMeasureSpinner();

        return rootView;
    }

    private void setViewModelToBinding() {

        viewModel = ViewModelProviders.of(requireActivity()).get(ProductSizeViewModel.class);
        sizeEditor.setSizeEditorViewModel(viewModel);
    }

    private void setValidationHandlersToBinding() {

        sizeEditor.setSizeValidation(viewModel.getSizeValidationHandler());
    }

    private void setBindingInstanceVariables() {

        sizeEditor.setMeasurement(viewModel.getMeasurement());
    }

    private void setupUnitOfMeasureSpinner() {

        sizeEditor.spinnerUnitOfMeasure.setAdapter(getUnitOfMeasureSpinnerAdapter());
        setupSpinnerListeners(sizeEditor.spinnerUnitOfMeasure);
    }

    private SpinnerAdapter getUnitOfMeasureSpinnerAdapter() {

        return new UnitOfMeasureSpinnerAdapter(requireActivity(), unitOfMeasureArrayList());
    }

    private ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureArrayList() {

        List<String> unitOfMeasureHeaders = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_headers));

        String massMetricUnits = removeArrayBraces(Arrays.toString(getResources().
                getStringArray(R.array.unit_of_measure_options_mass_metric)));

        String massImperialUnits = removeArrayBraces(Arrays.toString(getResources().
                getStringArray(R.array.unit_of_measure_options_mass_imperial)));

        String volumeMetricUnits = removeArrayBraces(Arrays.toString(getResources().
                getStringArray(R.array.unit_of_measure_options_volume_metric)));

        String volumeImperialUnits = removeArrayBraces(Arrays.toString(getResources().
                getStringArray(R.array.unit_of_measure_options_volume_imperial)));

        String countUnits = removeArrayBraces(Arrays.toString(getResources().
                getStringArray(R.array.unit_of_measure_options_count)));

        ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureList = new ArrayList<>();

        for (int header = 0; header < unitOfMeasureHeaders.size(); header++) {

            UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                    SpinnerItemType.SECTION_HEADER,
                    unitOfMeasureHeaders.get(header)
            );
            unitOfMeasureList.add(headerItem);

            if (header == MeasurementType.TYPE_MASS.ordinal() - 1) {

                UnitOfMeasureSpinnerItem massMetricItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, massMetricUnits);
                unitOfMeasureList.add(massMetricItem);

                UnitOfMeasureSpinnerItem massImperialItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, massImperialUnits);
                unitOfMeasureList.add(massImperialItem);

            } else if (header == MeasurementType.TYPE_VOLUME.ordinal() - 1) {

                UnitOfMeasureSpinnerItem volumeMetricItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, volumeMetricUnits);
                unitOfMeasureList.add(volumeMetricItem);

                UnitOfMeasureSpinnerItem volumeImperialItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, volumeImperialUnits);
                unitOfMeasureList.add(volumeImperialItem);

            } else if (header == MeasurementType.TYPE_COUNT.ordinal() - 1) {

                UnitOfMeasureSpinnerItem countItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, countUnits);
                unitOfMeasureList.add(countItem);
            }
        }
        return unitOfMeasureList;
    }

    private String removeArrayBraces(String stringWithBrackets) {

        return stringWithBrackets.substring(1, stringWithBrackets.length() - 1);
    }

    private void setupSpinnerListeners(Spinner spinner) {

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
