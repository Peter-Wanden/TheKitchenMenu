package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Recipe;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailRecipe;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogRecipe;

import java.util.List;

public class ActivityCatalogRecipe
        extends
        AppCompatActivity
        implements
        AdapterCatalogRecipe.RecipeCatalogAdapterOnClickHandler {

    public static final String LOG_TAG = ActivityCatalogRecipe.class.getSimpleName();

    /* Adapter for the recipe list view */
    public AdapterCatalogRecipe mCatalogAdapter;

    /* RecyclerView for the list view */
    private RecyclerView mRecyclerView;

    /* Floating action button */
    private FloatingActionButton mFab;

    /* Empty view */
    private View mEmptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_recipe);

        setupViews();

        // Todo - change color of + in fab to white
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRecipeIntent = new Intent(ActivityCatalogRecipe.this,
                        ActivityDetailRecipe.class);
                startActivity(addRecipeIntent);
            }
        });

        /* Create the adapter and pass in the this class context and the listener (which is also
        this class as this class implements the click handler. */
        mCatalogAdapter = new AdapterCatalogRecipe(this, this);
        mRecyclerView.setAdapter(mCatalogAdapter);

        /* Retrieve the content of the products table */
        setupRecipesViewModel();
    }

    private void setupViews() {
        /* Get a reference to the views */
        mRecyclerView = findViewById(R.id.fragment_catalog_ingredients_rv);
        mFab = findViewById(R.id.activity_catalog_recipe_fab);
        mEmptyView = findViewById(R.id.activity_catalog_recipe_empty_view);

        /* Create and set the layout manager */
        // Todo - Make cards use LinearLayout and GridView depending on the orientation and size of the screen
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * A recipe has been clicked in the RecyclerView. Add its ID to an intent and go to
     * the ActivityDetailRecipe.
     * @param recipeId - The ID of the selected recipe
     */
    @Override
    public void onClick(int recipeId) {
        Intent intent = new Intent(
                ActivityCatalogRecipe.this, ActivityDetailRecipe.class);
        intent.putExtra(Constants.RECIPE_ID, recipeId);
        startActivity(intent);
    }

    /* Retrieve recipes from the database and set an observer to watch for changes */
    private void setupRecipesViewModel() {

        /* Call ViewModelProviders */
        ViewModelCatalogRecipe viewModel =
                ViewModelProviders
                        .of(this)
                        .get(ViewModelCatalogRecipe.class);

        /* Set observer for any data changes */
        viewModel.getRecipes().observe(this, recipes -> {
            // Set the list to the adapter
            mCatalogAdapter.setRecipes(recipes);

            // Set empty view
            if (recipes.size() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        });
    }
}
