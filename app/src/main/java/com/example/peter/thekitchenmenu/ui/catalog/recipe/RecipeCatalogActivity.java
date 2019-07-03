package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeCatalogActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.ui.detail.recipe.RecipeEditorActivity;

public class RecipeCatalogActivity extends AppCompatActivity implements RecipeNavigator {

    private static final String TAG = "tkm-RecipeCatalogAct";

    private RecipeCatalogViewModel recipeCatalogViewModel;
    private RecipeCatalogActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViewModel();
        initialiseBindings();
        setupToolbar();
        setTitle(this.getResources().getString(R.string.activity_title_recipe_catalog));
    }

    private void setupViewModel() {
        recipeCatalogViewModel = obtainViewModel(this);
        recipeCatalogViewModel.setNavigator(this);
    }

    @Override
    protected void onDestroy() {
        recipeCatalogViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    public static RecipeCatalogViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factoryRecipe).get(RecipeCatalogViewModel.class);
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.recipe_catalog_activity);
        binding.setViewModel(recipeCatalogViewModel);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.recipeCatalogActivityToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void addRecipe() {
        Intent intent = new Intent(this, RecipeEditorActivity.class);
        startActivity(intent);
    }

    @Override
    public void editRecipe(String recipeId) {

    }

    @Override
    public void addRecipeToPlanner(String recipeId) {

    }
}
