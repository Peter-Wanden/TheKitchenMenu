package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProductMeasurementEditorFragment extends Fragment {

    private static final String TAG = "tkm-MeasurementFragment";

    private ProductEditorMeasurementBinding binding;
    private ProductMeasurementViewModel viewModel;
    private ConstraintSet constraintTwoUnits = new ConstraintSet();
    private ConstraintSet constraintOneUnit = new ConstraintSet();
    private ConstraintLayout constraintLayout;

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
        subscribeToObservableEvents();
        setUpConstraintViewChanges();

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

    private void subscribeToObservableEvents() {
        viewModel.getSetDisplayToOneMeasurementUnitEvent().observe(this, event ->
                setDisplayToOneMeasurementUnit());
        viewModel.getSetDisplayToTwoMeasurementUnitsEvent().observe(this, event->
                setDisplayToTwoMeasurementUnits());
    }

    // see:{https://developer.android.com/reference/android/support/constraint/ConstraintSet}
    private void setDisplayToOneMeasurementUnit() {
        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintOneUnit.applyTo(constraintLayout);

    }

    private void setDisplayToTwoMeasurementUnits() {
        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintTwoUnits.applyTo(constraintLayout);
    }

    private void setUpConstraintViewChanges() {
        Context context = requireActivity();

        constraintOneUnit.clone(context, R.layout.product_editor_measurement_one_unit);
        constraintLayout = binding.productMeasurementUnits.productMeasurementUnitsParent;
        constraintTwoUnits.clone(constraintLayout);
    }
}