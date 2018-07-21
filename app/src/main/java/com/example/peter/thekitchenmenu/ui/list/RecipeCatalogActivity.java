package com.example.peter.thekitchenmenu.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.model.Recipe;
import com.example.peter.thekitchenmenu.ui.detail.ProductDetailActivity;
import com.example.peter.thekitchenmenu.ui.detail.RecipeDetailActivity;

import java.util.List;

public class RecipeCatalogActivity
        extends
        AppCompatActivity
        implements
        RecipeCatalogAdapter.RecipeCatalogAdapterOnClickHandler {

    public static final String LOG_TAG = RecipeCatalogActivity.class.getSimpleName();

    /* Adapter for the recipe list view */
    public RecipeCatalogAdapter mCatalogAdapter;

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
                Intent addRecipeIntent = new Intent(RecipeCatalogActivity.this,
                        RecipeDetailActivity.class);
                startActivity(addRecipeIntent);
            }
        });

        /* Create the adapter and pass in the this class context and the listener (which is also
        this class as this class implements the click handler. */
        mCatalogAdapter = new RecipeCatalogAdapter(this, this);
        mRecyclerView.setAdapter(mCatalogAdapter);

        /* Retrieve the content of the products table */
        setupRecipesViewModel();
    }

    private void setupViews() {
        /* Get a reference to the views */
        mRecyclerView = findViewById(R.id.activity_catalog_recipe_rv);
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
     * the RecipeDetailActivity.
     * @param recipeId - The ID of the selected recipe
     */
    @Override
    public void onClick(int recipeId) {
        Intent intent = new Intent(
                RecipeCatalogActivity.this, RecipeDetailActivity.class);
        intent.putExtra(Constants.RECIPE_ID, recipeId);
        startActivity(intent);
    }

    /* Retrieve recipes from the database and set an observer to watch for changes */
    private void setupRecipesViewModel() {

        /* Call ViewModelProviders */
        RecipeCatalogViewModel viewModel =
                ViewModelProviders
                        .of(this)
                        .get(RecipeCatalogViewModel.class);

        /* Set observer for any data changes */
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                // Set the list to the adapter
                mCatalogAdapter.setRecipes(recipes);

                // Set empty view
                if (recipes.size() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        });
    }
}
