package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientEditorMeasurementBinding;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.ui.utils.SpinnerItemType;
import com.example.peter.thekitchenmenu.ui.utils.UnitOfMeasureSpinnerAdapter;
import com.example.peter.thekitchenmenu.ui.utils.UnitOfMeasureSpinnerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeIngredientMeasurementFragment extends Fragment {

    private RecipeIngredientEditorMeasurementBinding binding;
    private RecipeIngredientMeasurementViewModel viewModel;

    static RecipeIngredientMeasurementFragment newInstance() {
        return new RecipeIngredientMeasurementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_ingredient_editor_measurement,
                container,
                false);

        binding.setLifecycleOwner(this);
        setViewModel();
        binding.setViewModel(viewModel);

        setObservers();
        setupUnitOfMeasureSpinner();

        return binding.getRoot();
    }

    public void setViewModel() {
        this.viewModel = RecipeIngredientEditorActivity.
                obtainRecipeIngredientMeasurementViewModel(requireActivity());
    }

    private void setObservers() {
        viewModel.getIsValidMeasurement().observe(getViewLifecycleOwner(), showMenu -> {
            if (showMenu) {
                setHasOptionsMenu(true);
            } else {
                setHasOptionsMenu(false);
            }
        });
    }

    private void setupUnitOfMeasureSpinner() {
        binding.recipeIngredientUnitOfMeasureSpinner.setAdapter(getUnitOfMeasureSpinnerAdapter());
    }

    private SpinnerAdapter getUnitOfMeasureSpinnerAdapter() {
        return new UnitOfMeasureSpinnerAdapter(requireActivity(), unitOfMeasureSpinnerItemArrayList());
    }

    private ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureSpinnerItemArrayList() {

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

        String spoonUnits = removeArrayBraces(Arrays.toString(getResources().
                getStringArray(R.array.unit_of_measure_options_volume_teaspoon)));

        ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureList = new ArrayList<>();

        for (int header = 0; header < unitOfMeasureHeaders.size(); header++) {

            if (header == MeasurementSubtype.METRIC_MASS.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(massMetricUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.IMPERIAL_MASS.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(massImperialUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.METRIC_VOLUME.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(volumeMetricUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.IMPERIAL_VOLUME.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(volumeImperialUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.COUNT.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(countUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.IMPERIAL_SPOON.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(spoonUnits).
                                concat(")"));
                unitOfMeasureList.add(headerItem);
            }
        }

        return unitOfMeasureList;
    }

    private String removeArrayBraces(String stringWithBrackets) {
        return stringWithBrackets.substring(1, stringWithBrackets.length() - 1);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe_ingredient_measurement, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_recipe_ingredient_editor_action_use) {
            viewModel.donePressed();
            return true;
        }
        return false;
    }
}
