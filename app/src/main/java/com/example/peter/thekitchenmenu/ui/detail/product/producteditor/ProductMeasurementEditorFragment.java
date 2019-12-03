package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorMeasurementBinding;
import com.example.peter.thekitchenmenu.ui.utils.UnitOfMeasureSpinnerAdapterBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ProductMeasurementEditorFragment extends Fragment {

    private ProductEditorMeasurementBinding binding;
    private ProductMeasurementViewModel viewModel;

    static ProductMeasurementEditorFragment newInstance() {
        return new ProductMeasurementEditorFragment();
    }

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
        viewModel = new ViewModelProvider(requireActivity()).get(ProductMeasurementViewModel.class);
    }

    private void setValidationHandlersToBinding() {
        binding.setMeasurementHandler(viewModel.getMeasurementHandler());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupUnitOfMeasureSpinner() {
        binding.unitOfMeasureSpinner.setAdapter(getUnitOfMeasureSpinnerAdapter());
    }

    private SpinnerAdapter getUnitOfMeasureSpinnerAdapter() {

        return UnitOfMeasureSpinnerAdapterBuilder.
                setActivity(requireActivity()).
                addMetricMass().
                addImperialMass().
                addMetricVolume().
                addImperialVolume().
                addCount().
                build();
    }
}