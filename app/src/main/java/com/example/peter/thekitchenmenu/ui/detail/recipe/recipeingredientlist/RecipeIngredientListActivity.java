package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientListActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class RecipeIngredientListActivity
        extends AppCompatActivity
        implements RecipeIngredientListNavigator, RecipeIngredientListItemNavigator {

    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";

    private RecipeIngredientListActivityBinding binding;
    private RecipeIngredientListRecipeViewModel recipeViewModel;
    private RecipeIngredientListViewModel recipeIngredientListViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseBindings();
        setupActionBar();
        setupViewModel();
        setupFragment();
        start();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this,
                R.layout.recipe_ingredient_list_activity);
        binding.setLifecycleOwner(this);
    }

    private void setupActionBar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.activity_title_recipe_ingredient_list_activity);
    }

    private void setupViewModel() {
        recipeViewModel = obtainRecipeIngredientListRecipeViewModel(this);
        recipeViewModel.setNavigator(this);
        binding.setViewModel(recipeViewModel);

        recipeIngredientListViewModel = obtainRecipeIngredientListViewModel(this);
    }

    static RecipeIngredientListRecipeViewModel obtainRecipeIngredientListRecipeViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeIngredientListRecipeViewModel.class);
    }

    static RecipeIngredientListViewModel obtainRecipeIngredientListViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeIngredientListViewModel.class);
    }

    private void setupFragment() {
        RecipeIngredientListFragment fragment = obtainRecipeIngredientListFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.recipeIngredientListContentFrame
        );
    }

    @NonNull
    private RecipeIngredientListFragment obtainRecipeIngredientListFragment() {
        RecipeIngredientListFragment fragment = (RecipeIngredientListFragment) getSupportFragmentManager().
                findFragmentById(R.id.recipeIngredientListContentFrame);
        if (fragment == null)
            fragment = new RecipeIngredientListFragment();
        return fragment;
    }

    private void start() {
        String recipeId = getIntent().getStringExtra(EXTRA_RECIPE_ID);
        recipeViewModel.start(recipeId);
        recipeIngredientListViewModel.start(recipeId);
    }

    @Override
    public void addRecipeIngredient() {

    }

    @Override
    protected void onDestroy() {
        recipeViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    @Override
    public void deleteIngredient(String ingredientId) {

    }
}
