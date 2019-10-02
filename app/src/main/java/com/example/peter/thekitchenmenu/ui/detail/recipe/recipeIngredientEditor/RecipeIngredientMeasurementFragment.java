package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientEditorMeasurementBinding;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.SpinnerItemType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureSpinnerAdapter;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureSpinnerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeIngredientMeasurementFragment extends Fragment {

    private static final String ARGUMENT_RECIPE_ID = "RECIPE_ID";
    private static final String ARGUMENT_INGREDIENT_ID = "INGREDIENT_ID";

    private RecipeIngredientEditorMeasurementBinding binding;
    private RecipeIngredientMeasurementViewModel viewModel;

    static RecipeIngredientMeasurementFragment newInstance(String recipeId, String ingredientId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_RECIPE_ID, recipeId);
        arguments.putString(ARGUMENT_INGREDIENT_ID, ingredientId);
        RecipeIngredientMeasurementFragment fragment = new RecipeIngredientMeasurementFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        String recipeId = getArguments().getString(ARGUMENT_RECIPE_ID);
        String ingredientId = getArguments().getString(ARGUMENT_INGREDIENT_ID);
        viewModel.start(recipeId, ingredientId);
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

        setupUnitOfMeasureSpinner();

        return binding.getRoot();
    }

    public void setViewModel() {
        this.viewModel = RecipeIngredientEditorActivity.
                obtainRecipeIngredientMeasurementViewModel(requireActivity());
    }

    private void setupUnitOfMeasureSpinner() {
        binding.spinnerUnitOfMeasure.setAdapter(getUnitOfMeasureSpinnerAdapter());
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

            if (header == MeasurementSubtype.TYPE_METRIC_MASS.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(massMetricUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_IMPERIAL_MASS.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(massImperialUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_METRIC_VOLUME.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(volumeMetricUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_IMPERIAL_VOLUME.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(volumeImperialUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_COUNT.asInt()) {
                UnitOfMeasureSpinnerItem headerItem = new UnitOfMeasureSpinnerItem(
                        SpinnerItemType.LIST_ITEM,
                        unitOfMeasureHeaders.get(header).
                                concat(" (").
                                concat(countUnits).
                                concat(")"));

                unitOfMeasureList.add(headerItem);
            }

            if (header == MeasurementSubtype.TYPE_IMPERIAL_SPOON.asInt()) {
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
}
