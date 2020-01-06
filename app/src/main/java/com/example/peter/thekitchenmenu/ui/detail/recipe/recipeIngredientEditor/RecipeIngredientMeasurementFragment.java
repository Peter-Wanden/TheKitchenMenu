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
import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.CountFractionsSpinnerAdapterBuilder;
import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.UnitOfMeasureSpinnerAdapterBuilder;

public class RecipeIngredientMeasurementFragment extends Fragment {

    private RecipeIngredientEditorMeasurementBinding binding;
    private RecipeIngredientCalculatorViewModel viewModel;

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
        setupSpinners();

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

    private void setupSpinners() {
        binding.recipeIngredientUnitOfMeasureSpinner.setAdapter(getUnitOfMeasureSpinnerAdapter());
        binding.recipeIngredientCountUnitTwoSpinner.setAdapter(getCountFractionSpinnerAdapter());
    }

    private SpinnerAdapter getUnitOfMeasureSpinnerAdapter() {
        return UnitOfMeasureSpinnerAdapterBuilder.setActivity(requireActivity()).
                addMetricMass().
                addImperialMass().
                addMetricVolume().
                addImperialVolume().
                addCount().
                addImperialSpoon().
                build();
    }

    private SpinnerAdapter getCountFractionSpinnerAdapter() {
        return CountFractionsSpinnerAdapterBuilder.setActivity(requireActivity()).
                addOneTenth().
                addOneFifth().
                addOneQuarter().
                addThreeTenths().
                addTwoFifths().
                addHalf().
                addThreeFifths().
                addSevenTenths().
                addThreeQuarters().
                addFourFifths().
                addNineTenths().
                build();
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
