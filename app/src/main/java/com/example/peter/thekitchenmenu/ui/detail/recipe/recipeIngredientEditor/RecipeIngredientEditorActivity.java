package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientEditorBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class RecipeIngredientEditorActivity
        extends AppCompatActivity implements RecipeIngredientEditorNavigator {

    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";
    public static final String EXTRA_INGREDIENT_ID = "INGREDIENT_ID";

    private RecipeIngredientEditorBinding binding;
    private RecipeIngredientEditorViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupActionBar();
        setupViewModel();
        setupFragments();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this,
                R.layout.recipe_ingredient_editor);
        binding.setLifecycleOwner(this);
    }

    private void setupActionBar() {
        setSupportActionBar(binding.recipeIngredientEditorToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle(R.string.activity_title_recipe_ingredient_editor);
    }

    private void setupViewModel() {
        viewModel = obtainRecipeIngredientEditorViewModel(this);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
    }

    static RecipeIngredientEditorViewModel obtainRecipeIngredientEditorViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeIngredientEditorViewModel.class);
    }

    static RecipeIngredientMeasurementViewModel obtainRecipeIngredientMeasurementViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeIngredientMeasurementViewModel.class);
    }

    private void setupFragments() {
        RecipeIngredientMeasurementFragment measurementFragment = obtainMeasurementFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                measurementFragment,
                R.id.recipe_ingredient_editor_measurement_content_frame
        );
    }

    @NonNull
    private RecipeIngredientMeasurementFragment obtainMeasurementFragment() {
        RecipeIngredientMeasurementFragment fragment =
                (RecipeIngredientMeasurementFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_ingredient_editor_measurement_content_frame);

        if (fragment == null)
            fragment = new RecipeIngredientMeasurementFragment();
        return fragment;
    }

    @Override
    public void donePressed() {

    }

    @Override
    public void addRecommendedProduct() {

    }

    @Override
    public void cancelEditing() {

    }

    @Override
    protected void onDestroy() {
        viewModel.onActivityDestroyed();
        super.onDestroy();
    }
}
