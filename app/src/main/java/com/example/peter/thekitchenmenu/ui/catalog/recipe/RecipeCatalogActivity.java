package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.catalog.CatalogFragmentPageAdapter;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogController;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogFragment;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogView;
import com.example.peter.thekitchenmenu.ui.common.controllers.BaseActivity;
import com.google.android.material.tabs.TabLayout;

public class RecipeCatalogActivity
        extends BaseActivity {

    private static final String TAG = "tkm-" + RecipeCatalogActivity.class.getSimpleName() + ":";

    private RecipeCatalogController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeCatalogView view = getCompositionRoot().
                getViewFactory().
                getRecipeCatalogView(null);

        controller = getCompositionRoot().getRecipeCatalogController();
        controller.bindView(view);

        setContentView(view.getRootView());

        setupFragmentPageAdapter();
    }

    private void setupFragmentPageAdapter() {
        CatalogFragmentPageAdapter fragmentPageAdapter =
                new CatalogFragmentPageAdapter(getSupportFragmentManager());

        fragmentPageAdapter.addFragment(RecipeCatalogFragment.newInstance(),
                getString(R.string.activity_catalog_recipes_tab_1_title));
        fragmentPageAdapter.addFragment(RecipeCatalogFavoritesFragment.newInstance(),
                getString(R.string.activity_catalog_recipes_tab_2_title));

        ViewPager viewPager = findViewById(R.id.recipe_catalog_activity_viewPager);
        viewPager.setAdapter(fragmentPageAdapter);
        TabLayout tabLayout = findViewById(R.id.recipe_catalog_activity_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        controller.onStop();
    }
}
