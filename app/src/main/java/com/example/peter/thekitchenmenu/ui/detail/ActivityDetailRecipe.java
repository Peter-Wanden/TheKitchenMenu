package com.example.peter.thekitchenmenu.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.AppExecutors;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Recipe;
import com.example.peter.thekitchenmenu.ui.catalog.FragmentCatalogIngredient;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelRecipe;
import com.example.tkmapplibrary.dataValidation.InputValidation;

public class ActivityDetailRecipe extends AppCompatActivity {

    public static final String LOG_TAG = ActivityDetailRecipe.class.getSimpleName();

    // Database instance
    private TKMDatabase
            mDb;

    // Recipe object instance
    private Recipe
            mRecipe;

    // String member variables for the recipe fields
    private String
            mTitle,
            mDescription;

    // Integer member variables for the product fields
    private int
            mRecipeId = Constants.DEFAULT_RECIPE_ID,
            mCategory,
            mSittings,
            mServings;

    // Fields for the views in activity_detail_product
    private EditText
            mTitleET,
            mDescriptionET,
            mServingsET,
            mSittingsET;

    // Spinner
    private Spinner
            mCategorySpinner;

    // Fragments
    FragmentManager mFragmentManager;
    FragmentCatalogIngredient mFragmentCatalogIngredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        /* Construct a default recipe for field value comparison and validation */
        mRecipe = new Recipe(
                Constants.DEFAULT_RECIPE_TITLE,
                Constants.DEFAULT_RECIPE_DESCRIPTION,
                Constants.DEFAULT_RECIPE_CATEGORY,
                Constants.DEFAULT_RECIPE_SERVINGS,
                Constants.DEFAULT_RECIPE_SITTINGS);

        /* Setup the views */
        initialiseViews();

        /* Get an instance of the database */
        mDb = TKMDatabase.getInstance(getApplicationContext());

        /* If it exists get the current recipe from saved instance state */
        if(savedInstanceState != null && savedInstanceState.containsKey(
                Constants.RECIPE_KEY)) {

            mRecipe = savedInstanceState.getParcelable(
                    Constants.RECIPE_KEY);

            mRecipeId = mRecipe.getRecipeId();
        }

        /* If there is a recipe ID passed with the intent, this is an existing recipe */
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.RECIPE_ID)) {

            // This intent has passed a recipe ID, so the recipe is being updated.
            setTitle(getString(R.string.activity_detail_recipe_title_update));

            if(mRecipeId == Constants.DEFAULT_RECIPE_ID) {
                // Set the recipe id from the incoming intent */
                mRecipeId = intent.getIntExtra(
                        Constants.RECIPE_ID, Constants.DEFAULT_RECIPE_ID);

                setupRecipeViewModel();
            }
        } else {
            /* If there is no recipe ID passed in the intent then this is a new recipe */
            setTitle(getString(R.string.activity_detail_recipe_title_add_new));

        }

        // Setup and launch the ingredient list fragment
//        mFragmentCatalogIngredients = new FragmentCatalogIngredient();
//        mFragmentCatalogIngredients.setArguments(getIntent().getExtras());
//        mFragmentManager.beginTransaction().add
//                (R.id.activity_detail_recipe_fl_ingredients_fragment_container,
//                        mFragmentCatalogIngredients).commit();

        setupIngredientsViewModel();
    }

    /* View model for the recipe */
    private void setupRecipeViewModel() {
        // Get the recipe from the database
        ViewModelFactoryRecipe factory =
                new ViewModelFactoryRecipe(mDb, mRecipeId);

        final ViewModelRecipe viewModel
                = ViewModelProviders
                .of(this, factory)
                .get(ViewModelRecipe.class);

        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                viewModel.getRecipe().removeObserver(this);
                mRecipe = recipe;
                populateUi(mRecipe);
            }
        });
    }

    /* Retrieve ingredients and products from the database and set an observer to watch for changes */
    private void setupIngredientsViewModel() {

        /* Call ViewModelProviders */
//        ViewModelCatalogIngredientsAndProduct viewModel =
//                ViewModelProviders
//                        .of(this)
//                        .get(ViewModelCatalogIngredientsAndProduct.class);
//
//        /* Set observer for any data changes */
//        viewModel.getIngredients().observe(this, ingredients -> {
//             // Set the list to the adapter
//             mIngredientsAdapter.setProducts(ingredients);
//
//            // Set empty view
//            if(ingredients.size() == 0) {
//                mIngredientsBinding.fragmentCatalogContainerIngredientEmptyView
//                        .setVisibility(View.VISIBLE);
//            } else {
//                mIngredientsBinding.fragmentCatalogContainerIngredientEmptyView
//                        .setVisibility(View.GONE);
//            }
//            Log.e(LOG_TAG, String.valueOf(ingredients));
//        });
    }

    /* Called when updating a recipe */
    private void populateUi(Recipe recipe) {

        /* Update EditText fields */
        mTitleET.setText(recipe.getTitle());
        mDescriptionET.setText(recipe.getDescription());
        mServingsET.setText(String.valueOf(recipe.getServings()));
        mSittingsET.setText(String.valueOf(recipe.getSittings()));

        /* Update spinner */
        mCategorySpinner.setSelection(mRecipe.getCategory());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /* Save the current recipe */
        outState.putParcelable(Constants.RECIPE_KEY, mRecipe);
    }

    private void initialiseViews() {

        // Editable fields
        mTitleET = findViewById(R.id.activity_detail_recipe_et_title);
        mDescriptionET = findViewById(R.id.activity_detail_recipe_et_description);
        mServingsET = findViewById(R.id.activity_detail_recipe_et_servings);
        mSittingsET = findViewById(R.id.activity_detail_recipe_et_sittings);

        // Spinner field
        mCategorySpinner = findViewById(R.id.activity_detail_recipe_spinner_category);

        setupCategorySpinner();

        /* Start up a fragment manager instance for the ingredient and steps fragments */
        mFragmentManager = getSupportFragmentManager();
    }

    /* Setup the dropdown spinner that allows the user to select the recipe's category */
    private void setupCategorySpinner() {

        // Create an adapter for the spinner. The list options are from the String array in
        // arrays.xml. The spinner will use the default layout.
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_recipe_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        // Set the integer mUoM to the constant values
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.recipe_category_option_0))) {
                        mCategory = getResources().getInteger(R.integer.item_not_selected);

                    } else if (selection.equals(getString(R.string.recipe_category_option_1))) {
                        mCategory = getResources().getInteger(R.integer.category_option_1);

                    } else if (selection.equals(getString(R.string.recipe_category_option_2))) {
                        mCategory = getResources().getInteger(R.integer.category_option_2);

                    } else if (selection.equals(getString(R.string.recipe_category_option_3))) {
                        mCategory = getResources().getInteger(R.integer.category_option_3);

                    } else if (selection.equals(getString(R.string.recipe_category_option_4))) {
                        mCategory = getResources().getInteger(R.integer.category_option_4);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = getResources().getInteger(R.integer.item_not_selected);
            }
        });
    }

    /* Inflate the menu options from res/menu/menu_recipe_editor.xml. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_editor, menu);
        return true;
    }

    /* This method is called after invalidateOptionsMenu(), so the menu can be updated (some menu
   items can be hidden or made visible). */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new recipe, hide the "Delete" menu item.
        if (mRecipeId == -1) {
            MenuItem menuItem = menu.findItem(R.id.menu_recipe_editor_action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    /* This method manages the actions as they are selected from the AppBar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Save (tick) menu option selected
            case R.id.menu_recipe_editor_action_save:
                // Save recipe and exit activity
                saveRecipe();
                break;
            case R.id.menu_recipe_editor_action_delete:
                // Delete product
                deleteRecipe();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Saves the recipe, its steps and recipe specific product information */
    private void saveRecipe() {
        /*
        Todo Implement on back pressed save changes and touch listener (from pets)
        TODO - if a field fails, reset focus to that field and wit for input
        Title
        */
        mTitle = mTitleET.getText().toString().trim();
        int validateTitle = InputValidation.validateRecipeTitle(mTitle);

        if (validateTitle != 0) {
            mTitleET.requestFocus();

            switch (validateTitle) {
                case 1:
                    mTitleET.setError(getResources()
                            .getString(R.string.input_error_recipe_title_too_short));
                    break;

                case 2:
                    mTitleET.setError(getResources()
                            .getString(R.string.input_error_recipe_title_too_long));
                    break;
            }
            return;
        }

        /* Description */
        mDescription = mDescriptionET.getText().toString().trim();
        int validateRecipeDescription = InputValidation.validateRecipeDescription(mDescription);

        if (validateRecipeDescription != 0) {
            mDescriptionET.requestFocus();

            mDescriptionET.setError(getResources()
                    .getString(R.string.input_error_recipe_description_too_long));
            return;
        }

        /* Servings */
        String servings = mServingsET.getText().toString();

        if (servings.length() > 0) {
            mServings = Integer.parseInt(servings);
        } else {
            mServingsET.setError(getResources()
                    .getString(R.string.input_error_recipe_servings));
        }

        /* Sittings */
        String sittings = mSittingsET.getText().toString().trim();

        if (sittings.length() > 0) {
            mSittings = Integer.parseInt(sittings);
        } else {
            mSittings = Constants.DEFAULT_RECIPE_SITTINGS;
        }


        // Check to see if this is a new recipe and if the fields are blank
        if (mRecipeId == -1
                && TextUtils.isEmpty(mTitle)
                && TextUtils.isEmpty(mDescription)
                && mServings == 0
                && mSittings == 1) {

            // As no fields have been modified we can safely return without performing any actions
            return;
        }

        // Update the default recipe by adding the validated input fields
        mRecipe.setTitle(mTitle);
        mRecipe.setDescription(mDescription);
        mRecipe.setCategory(mCategory);
        mRecipe.setServings(mServings);
        mRecipe.setSittings(mSittings);

        final Recipe recipe = mRecipe;

        AppExecutors.getInstance().diskIO().execute(() -> {
            // Determine if this is an insert or update
            if (mRecipeId == Constants.DEFAULT_PRODUCT_ID) {
                // Insert new
                mDb.getRecipeDao().insertRecipe(recipe);
            } else {
                // Update existing
                recipe.setRecipeId(mRecipeId);
                mDb.getRecipeDao().updateRecipe(recipe);
            }
            finish();
        });
    }

    /* Deletes the current recipe and associated index's in all relational tables */
    // TODO - Cascade delete the recipes steps and any recipe specific product information
    private void deleteRecipe() {

        AppExecutors.getInstance().diskIO().execute(() ->
                mDb.getRecipeDao().deleteRecipe(mRecipe));
        finish();
    }
}
