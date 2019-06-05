package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorMeasurementBinding;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProductMeasurementEditorFragment extends Fragment {

    private static final String TAG = "tkm-MeasurementFragment";

    private ProductEditorMeasurementBinding binding;
    private ProductMeasurementViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_editor_measurement,
                container,
                false);

        View rootView = binding.getRoot();
        binding.setLifecycleOwner(this);

        setViewModel();
        setValidationHandlersToBinding();
        setBindingInstanceVariables();
        setupUnitOfMeasureSpinner();

        return rootView;
    }

    private void setViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).
                get(ProductMeasurementViewModel.class);
    }

    private void setValidationHandlersToBinding() {
        binding.setMeasurementHandler(viewModel.getMeasurementHandler());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupUnitOfMeasureSpinner() {
        binding.spinnerUnitOfMeasure.setAdapter(getUnitOfMeasureSpinnerAdapter());
    }

    private SpinnerAdapter getUnitOfMeasureSpinnerAdapter() {
        return new UnitOfMeasureSpinnerAdapter(requireActivity(), unitOfMeasureArrayList());
    }

    private ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureArrayList() {
        List<String> unitOfMeasureHeaders = Arrays.asList(getResources().getStringArray(
                R.array.unit_of_measure_subtypes_as_string_array));

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

            if (header == MeasurementSubtype.TYPE_METRIC_MASS.ordinal()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(massMetricUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_IMPERIAL_MASS.ordinal()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(massImperialUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_METRIC_VOLUME.ordinal()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(volumeMetricUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_IMPERIAL_VOLUME.ordinal()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(volumeImperialUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_COUNT.ordinal()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(countUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }
        }

        return unitOfMeasureList;
    }

    private String removeArrayBraces(String stringWithBrackets) {
        return stringWithBrackets.substring(1, stringWithBrackets.length() - 1);
    }
}
