package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientEditorMeasurementBinding;

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
        binding.setViewModel(RecipeIngredientEditorActivity.
                obtainRecipeIngredientMeasurementViewModel(getActivity()));

        return binding.getRoot();
    }
}
