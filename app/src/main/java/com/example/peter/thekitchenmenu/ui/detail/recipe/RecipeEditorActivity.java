package com.example.peter.thekitchenmenu.ui.detail.recipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.databinding.RecipeEditorActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.ui.imageeditor.ImageEditorFragment;
import com.example.peter.thekitchenmenu.ui.imageeditor.ImageEditorViewModel;
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
    private ImageEditorViewModel imageEditorViewModel;

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
        setupObservers();
        setupToolbar();
        setupActionBar();
        setupFragments();
        loadData();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.recipe_editor_activity);
        binding.setLifecycleOwner(this);
    }

    private void setViewModels() {
        recipeEditorViewModel = obtainRecipeEditorViewModel(this);
        imageEditorViewModel = ViewModelProviders.of(this).get(ImageEditorViewModel.class);
        recipeIdentityViewModel = obtainIdentityViewModel(this);
    }

    private void setupObservers() {
        recipeEditorViewModel.imageModel.observe(this, imageModel ->
                imageEditorViewModel.getExistingImageModel().setValue(imageModel));

        recipeEditorViewModel.recipeIdentityModel.observe(this, recipeIdentityModel ->
                recipeIdentityViewModel.start(recipeIdentityModel));
    }

    private static RecipeEditorViewModel obtainRecipeEditorViewModel(FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factoryRecipe).get(RecipeEditorViewModel.class);
    }

    static RecipeIdentityViewModel obtainIdentityViewModel(FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factoryRecipe).get(RecipeIdentityViewModel.class);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.recipeEditorToolbar);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (getIntent().getStringExtra(EXTRA_RECIPE_ID) != null)
            actionBar.setTitle(R.string.activity_title_edit_recipe);
        else
            actionBar.setTitle(R.string.activity_title_add_new_recipe);
    }

    private void setupFragments() {
        RecipeIdentityFragment recipeIdentityFragment = obtainRecipeIdentityFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                recipeIdentityFragment,
                R.id.recipe_editor_recipe_identity_content_frame);

        ImageEditorFragment imageEditorFragment = obtainImageEditorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                imageEditorFragment,
                R.id.recipe_editor_image_editor_content_frame);
    }

    @NotNull
    private RecipeIdentityFragment obtainRecipeIdentityFragment() {
        RecipeIdentityFragment recipeIdentityFragment =
                (RecipeIdentityFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_recipe_identity_content_frame);

        if (recipeIdentityFragment == null)
            recipeIdentityFragment = RecipeIdentityFragment.newInstance(null); //todo add the recipeID
        return recipeIdentityFragment;
    }

    @NonNull
    private ImageEditorFragment obtainImageEditorFragment() {
        ImageEditorFragment imageEditorFragment =
                (ImageEditorFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_image_editor_content_frame);

        if (imageEditorFragment == null)
            imageEditorFragment = ImageEditorFragment.newInstance();
        return imageEditorFragment;
    }

    private void loadData() {
        if (getIntent().getStringExtra(EXTRA_RECIPE_ID) != null)
            recipeEditorViewModel.start(getIntent().getStringExtra(EXTRA_RECIPE_ID));
        else
            recipeEditorViewModel.start(null);
    }
}
