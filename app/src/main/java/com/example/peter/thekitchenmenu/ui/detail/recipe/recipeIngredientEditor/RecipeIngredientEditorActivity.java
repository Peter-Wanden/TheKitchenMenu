package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.Intent;
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
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryIngredient;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientViewerViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeNameAndPortionsViewModel;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class RecipeIngredientEditorActivity
        extends AppCompatActivity implements RecipeIngredientEditorNavigator {

    private static final String TAG = "tkm-RecipeIngredientEdt";

    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";
    public static final String EXTRA_INGREDIENT_ID = "INGREDIENT_ID";

    private RecipeIngredientEditorBinding binding;
    private RecipeIngredientEditorViewModel recipeIngredientEditorViewModel;
    private RecipeNameAndPortionsViewModel recipeNameAndPortionsViewModel;
    private IngredientViewerViewModel ingredientViewerViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupActionBar();
        setupViewModels();
        setupFragments();
        start();
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

    private void setupViewModels() {
        recipeIngredientEditorViewModel = obtainRecipeIngredientEditorViewModel(this);
        recipeIngredientEditorViewModel.setNavigator(this);
        binding.setIngredientMeasurements(recipeIngredientEditorViewModel);

        recipeNameAndPortionsViewModel = obtainRecipeNameAndPortionsViewModel(this);
        binding.setNameAndPortions(recipeNameAndPortionsViewModel);

        ingredientViewerViewModel = obtainIngredientViewerViewModel(this);
        binding.setIngredient(ingredientViewerViewModel);
    }

    static RecipeIngredientEditorViewModel obtainRecipeIngredientEditorViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeIngredientEditorViewModel.class);
    }

    static RecipeNameAndPortionsViewModel obtainRecipeNameAndPortionsViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeNameAndPortionsViewModel.class);
    }

    static RecipeIngredientMeasurementViewModel obtainRecipeIngredientMeasurementViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeIngredientMeasurementViewModel.class);
    }

    static IngredientViewerViewModel obtainIngredientViewerViewModel(
            FragmentActivity activity) {
        ViewModelFactoryIngredient factory = ViewModelFactoryIngredient.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(IngredientViewerViewModel.class);
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
        // setResult() - to go back to RecipeIngredientListActivity;
        // finish
    }

    @Override
    public void addRecommendedProduct() {

    }

    @Override
    public void cancelEditing() {

    }

    @Override
    protected void onDestroy() {
        recipeIngredientEditorViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    private void start() {
        Intent intent = getIntent();
        String recipeId = intent.getStringExtra(EXTRA_RECIPE_ID);
        String ingredientId = intent.getStringExtra(EXTRA_INGREDIENT_ID);
        recipeNameAndPortionsViewModel.start(recipeId);
        ingredientViewerViewModel.start(ingredientId);
        recipeIngredientEditorViewModel.start(recipeId, ingredientId);
    }
}
