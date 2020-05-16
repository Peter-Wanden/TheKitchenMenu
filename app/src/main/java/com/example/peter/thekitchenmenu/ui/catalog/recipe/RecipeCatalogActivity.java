package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeCatalogActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.ui.catalog.CatalogFragmentPageAdapter;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogFragment;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogView;
import com.example.peter.thekitchenmenu.ui.common.controllers.BaseActivity;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeCatalogActivity
        extends BaseActivity
        implements RecipeNavigator {

    private static final String TAG = "tkm-" + RecipeCatalogActivity.class.getSimpleName() + ":";

    private RecipeCatalogView parentViewMvc;

    private RecipeCatalogViewModel viewModel;
    private RecipeCatalogActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentViewMvc = new RecipeCatalogView(LayoutInflater.from(this), null);

        setContentView(parentViewMvc.getRootView());

        initialiseBindings();
        setupToolbar();
        setupFab();
        setupViewModel();
        setupFragmentPageAdapter();
        setTitle(this.getResources().getString(R.string.activity_title_recipe_catalog));
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.recipe_catalog_activity);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        FloatingActionButton fab = findViewById(R.id.recipe_catalog_activity_fab);
        fab.setOnClickListener(view -> viewModel.addRecipe());
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

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.recipe_catalog_activity_fab);
        fab.setOnClickListener(view -> viewModel.addRecipe());
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();
    }

    private void setupFragmentPageAdapter() {
        CatalogFragmentPageAdapter fragmentPageAdapter =
                new CatalogFragmentPageAdapter(getSupportFragmentManager());

        fragmentPageAdapter.addFragment(RecipeCatalogFragment.newInstance(),
                getString(R.string.activity_catalog_recipes_tab_1_title));
        fragmentPageAdapter.addFragment(RecipeCatalogFavoritesFragment.newInstance(),
                getString(R.string.activity_catalog_recipes_tab_2_title));

        binding.recipeCatalogActivityViewPager.setAdapter(fragmentPageAdapter);
        binding.recipeCatalogActivityTabLayout.setupWithViewPager(
                binding.recipeCatalogActivityViewPager);
    }

    public static RecipeCatalogViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factoryRecipe).get(RecipeCatalogViewModel.class);
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
    public void viewRecipe(String recipeDomainId) {

    }
}
