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
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureClassSelector;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProductEditor extends Fragment {

    private static final String TAG = "ProductEditor";

    private ProductEditorBinding productEditor;
    private ProductEditorViewModel viewModel;

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
        setValidationHandlers();
        setProductInstanceVariables();
        setObservers();
        setupSpinners();

        return rootView;
    }

    private void setViewModel() {

        viewModel = ViewModelProviders.of(requireActivity()).get(ProductEditorViewModel.class);
        productEditor.setProductEditorViewModel(viewModel);
    }

    private void setValidationHandlers() {

        productEditor.setTextValidation(viewModel.getTextValidationHandler());
        productEditor.setNumericValidation(viewModel.getNumericValidationHandler());
    }

    private void setProductInstanceVariables() {

        productEditor.setMeasurement(viewModel.getMeasurement());
        productEditor.setProductModel(viewModel.getProductModel());
    }

    private void setObservers() {

        final Observer<Product> productObserver = product -> {

            if (product != null) {

                viewModel.getProductModel().getValuesFromEntity(product);
                viewModel.getMeasurement().getNumericValuesFromProductModel(
                        viewModel.getProductModel());

                UnitOfMeasure unitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                        requireActivity(), viewModel.getMeasurement().getMeasurementSubType());

                Log.d(TAG, "setObservers: Unit of measure from measurement is: " +
                        unitOfMeasure.getMeasurementSubType());

                boolean numberOfItemsAreSet = unitOfMeasure.setNumberOfItems(
                        viewModel.getMeasurement().getNumberOfItems());

                if (numberOfItemsAreSet) {

                    Log.d(TAG, "setObservers: Number of items are set!");
                    Log.d(TAG, "setObservers: Setting base units to: " +
                            viewModel.getMeasurement().getBaseSiUnits());

                    if (unitOfMeasure.baseSiUnitsAreSet(
                            viewModel.getMeasurement().getBaseSiUnits())) {

                        viewModel.getMeasurement().setPackMeasurementOne(
                                unitOfMeasure.getPackMeasurementOne());

                        Log.d(TAG, "setObservers: Setting pack measurement one to: " +
                                unitOfMeasure.getPackMeasurementOne());

                        viewModel.getMeasurement().setPackMeasurementTwo(
                                unitOfMeasure.getPackMeasurementTwo());

                        Log.d(TAG, "setObservers: Setting pack measurement two to: " +
                                unitOfMeasure.getPackMeasurementTwo());

                    } else Log.d(TAG, "setObservers: Cannot set base units!");

                } else Log.d(TAG, "setObservers: Number of items are not set");
            }};

        viewModel.getProductEntity().observe(this, productObserver);
    }

    private void setupSpinners() {

        setupCategorySpinner();
        setupShelfLifeSpinner();
        setupUnitOfMeasureSpinner();
    }

    private void setupCategorySpinner() {

        productEditor.spinnerCategory.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(), R.array.product_category_options,
                R.layout.list_item_spinner));
        setSpinnerListeners(productEditor.spinnerCategory);
    }

    private void setupShelfLifeSpinner() {

        productEditor.spinnerShelfLife.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(),
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

        return new UnitOfMeasureSpinnerAdapter(requireActivity(), unitOfMeasureArrayList());
    }

    private void setSpinnerListeners(Spinner spinner) {

        spinner.setOnFocusChangeListener((view, b) -> {

            if (view.hasFocus()) {

                ShowHideSoftInput.showKeyboard(view, false);
                // Avoids WindowManager$BadTokenException by waiting for the screen to redraw.
//                new Handler().postDelayed(view::performClick, 100);
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

            if (header == MeasurementType.TYPE_MASS.ordinal() -1) {

                UnitOfMeasureSpinnerItem massMetricItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, massMetricUnits);
                unitOfMeasureList.add(massMetricItem);

                UnitOfMeasureSpinnerItem massImperialItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, massImperialUnits);
                unitOfMeasureList.add(massImperialItem);

            } else if (header == MeasurementType.TYPE_VOLUME.ordinal() -1) {

                UnitOfMeasureSpinnerItem volumeMetricItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, volumeMetricUnits);
                unitOfMeasureList.add(volumeMetricItem);

                UnitOfMeasureSpinnerItem volumeImperialItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM, volumeImperialUnits);
                unitOfMeasureList.add(volumeImperialItem);

            } else if (header == MeasurementType.TYPE_COUNT.ordinal() -1) {

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
}
