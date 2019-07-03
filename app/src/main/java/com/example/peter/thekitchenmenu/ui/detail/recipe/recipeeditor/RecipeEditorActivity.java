package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeEditorActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;
import com.google.firebase.database.annotations.NotNull;

public class RecipeEditorActivity extends AppCompatActivity implements AddEditRecipeNavigator {

    private static final String TAG = "tkm-RecipeEditorActivity";

    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";
    public static final int REQUEST_ADD_EDIT_RECIPE = 50;
    public static final int RESULT_ADD_EDIT_RECIPE_OK = RESULT_FIRST_USER + 50;
    public static final int RESULT_ADD_EDIT_RECIPE_CANCELLED = RESULT_FIRST_USER + 51;

    private RecipeEditorActivityBinding binding;
    private RecipeEditorViewModel recipeEditorViewModel;
    private RecipeIdentityViewModel recipeIdentityViewModel;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void viewRecipeOnSaved() {
        setResult(RESULT_ADD_EDIT_RECIPE_OK);
        finish();
    }

    @Override
    public void addIngredients(String recipeId) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setViewModels();
        setupToolbar();
        setupFragments();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.recipe_editor_activity);
        binding.setLifecycleOwner(this);
    }

    private void setViewModels() {
        recipeEditorViewModel = obtainViewModel(this);
    }

    private static RecipeEditorViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factoryRecipe).get(RecipeEditorViewModel.class);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.recipeEditorToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setupFragments() {
        RecipeIdentityFragment recipeIdentityFragment = obtainRecipeIdentityFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                recipeIdentityFragment,
                R.id.recipe_editor_recipe_identity_content_frame);
    }

    @NotNull
    private RecipeIdentityFragment obtainRecipeIdentityFragment() {
        RecipeIdentityFragment recipeIdentityFragment =
                (RecipeIdentityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_editor_recipe_identity_content_frame);

        if (recipeIdentityFragment == null) {
            recipeIdentityFragment = RecipeIdentityFragment.newInstance(null); //todo add the recipeID
        }
        return recipeIdentityFragment;
    }
}
