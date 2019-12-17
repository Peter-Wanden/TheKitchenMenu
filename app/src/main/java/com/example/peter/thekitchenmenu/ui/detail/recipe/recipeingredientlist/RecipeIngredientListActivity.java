package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientEditorActivity;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class RecipeIngredientListActivity
        extends AppCompatActivity
        implements RecipeIngredientListNavigator, RecipeIngredientListItemNavigator {

    private static final String TAG = "tkm-" + RecipeIngredientListActivity.class.getSimpleName()
        + ":";

    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";

    private RecipeIngredientListActivityBinding binding;
    private RecipeNameAndPortionsViewModel nameAndPortionsViewModel;
    private RecipeIngredientListViewModel recipeIngredientListViewModel;
    private String recipeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseBindings();
        setupActionBar();
        setupViewModel();
        setupFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        nameAndPortionsViewModel = obtainRecipeNameAndPortionsViewModel(this);
        binding.setNameAndPortions(nameAndPortionsViewModel);

        recipeIngredientListViewModel = obtainRecipeIngredientListViewModel(this);
        recipeIngredientListViewModel.setNavigator(this);
        binding.setViewModel(recipeIngredientListViewModel);
    }

    static RecipeNameAndPortionsViewModel obtainRecipeNameAndPortionsViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeNameAndPortionsViewModel.class);
    }

    static RecipeIngredientListViewModel obtainRecipeIngredientListViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factory = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factory).get(
                RecipeIngredientListViewModel.class);
    }

    private void setupFragment() {
        RecipeIngredientListFragment fragment = findOrCreateRecipeIngredientListFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.recipeIngredientListContentFrame);
    }

    @NonNull
    private RecipeIngredientListFragment findOrCreateRecipeIngredientListFragment() {
        RecipeIngredientListFragment fragment =
                (RecipeIngredientListFragment) getSupportFragmentManager().
                findFragmentById(R.id.recipeIngredientListContentFrame);
        if (fragment == null)
            fragment = new RecipeIngredientListFragment();
        return fragment;
    }

    private void start() {
        recipeId = getIntent().getStringExtra(EXTRA_RECIPE_ID);
        nameAndPortionsViewModel.start(recipeId);
        recipeIngredientListViewModel.start(recipeId);
    }

    @Override
    public void addRecipeIngredient(String recipeId) {
        Intent intent = new Intent(this, IngredientEditorActivity.class);
        intent.putExtra(RecipeIngredientEditorActivity.EXTRA_RECIPE_ID, recipeId);
        startActivityForResult(intent, RecipeIngredientEditorActivity.REQUEST_ADD_RECIPE_INGREDIENT);
    }

    @Override
    protected void onDestroy() {
        recipeIngredientListViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    @Override
    public void editRecipeIngredient(String recipeIngredientId, String ingredientId) {
        Intent intent = new Intent(this, RecipeIngredientEditorActivity.class);
        intent.putExtra(RecipeIngredientEditorActivity.EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(RecipeIngredientEditorActivity.EXTRA_INGREDIENT_ID, ingredientId);
        intent.putExtra(RecipeIngredientEditorActivity.EXTRA_RECIPE_INGREDIENT_ID, recipeIngredientId);

        startActivityForResult(intent, RecipeIngredientEditorActivity.REQUEST_ADD_RECIPE_INGREDIENT);
    }

    @Override
    public void deleteRecipeIngredient(String recipeIngredientId) {
        recipeIngredientListViewModel.deleteRecipeIngredient(recipeIngredientId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recipeIngredientListViewModel.onActivityResult(requestCode, resultCode);
    }
}
