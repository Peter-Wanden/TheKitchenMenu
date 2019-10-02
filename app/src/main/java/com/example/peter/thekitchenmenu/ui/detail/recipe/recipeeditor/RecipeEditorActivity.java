package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeEditorActivityBinding;
import com.example.peter.thekitchenmenu.ui.UnsavedChangesDialogFragment;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryRecipe;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeIngredientListActivity;
import com.example.peter.thekitchenmenu.ui.imageeditor.ImageEditorFragment;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class RecipeEditorActivity
        extends AppCompatActivity
        implements AddEditRecipeNavigator {


    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";
    public static final int REQUEST_ADD_EDIT_RECIPE = 50;
    public static final int RESULT_ADD_EDIT_RECIPE_OK = RESULT_FIRST_USER + 1;
    public static final int RESULT_ADD_EDIT_RECIPE_CANCELLED = RESULT_FIRST_USER + 2;

    private RecipeEditorActivityBinding binding;
    private RecipeEditorViewModel recipeEditorViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupActionBar();
        setupViewModels();
        setViewModelObservers();
        setupFragments();
        start();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.recipe_editor_activity);
        binding.setLifecycleOwner(this);
    }

    private void setupActionBar() {
        setSupportActionBar(binding.recipeEditorToolbar);
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

    private void setupViewModels() {
        recipeEditorViewModel = findOrCreateEditorViewModel(this);
        recipeEditorViewModel.setNavigator(this);
        binding.setViewModel(recipeEditorViewModel);

        RecipeModelComposite recipeModelComposite = new RecipeModelComposite();
        recipeEditorViewModel.setRecipeModelComposite(recipeModelComposite);

        RecipeIdentityViewModel recipeIdentityViewModel =
                obtainIdentityViewModel(this);
        recipeIdentityViewModel.setModelValidationSubmitter(
                recipeEditorViewModel.getValidator());
        recipeModelComposite.registerModel(recipeIdentityViewModel);

        RecipeCourseSelectorViewModel courseSelectorViewModel =
                obtainCourseSelectorViewModel(this);
        courseSelectorViewModel.setModelValidationSubmitter(
                recipeEditorViewModel.getValidator());
        recipeModelComposite.registerModel(courseSelectorViewModel);

        RecipeDurationViewModel durationViewModel =
                obtainDurationViewModel(this);
        durationViewModel.setModelValidationSubmitter(
                recipeEditorViewModel.getValidator());
        recipeModelComposite.registerModel(durationViewModel);

        RecipePortionsViewModel portionsViewModel =
                obtainPortionsViewModel(this);
        portionsViewModel.setModelValidationSubmitter(
                recipeEditorViewModel.getValidator());
        recipeModelComposite.registerModel(portionsViewModel);
    }

    private void setViewModelObservers() {
        // Ui Events
        recipeEditorViewModel.getSetActivityTitleEvent().observe(this, this::setTitle);
        recipeEditorViewModel.getEnableReviewButtonEvent().observe(this, aVoid ->
                invalidateOptionsMenu());
        recipeEditorViewModel.getShowUnsavedChangesDialogEvent().observe(this, aVoid ->
                showUnsavedChangesDialogEvent());
    }

    private RecipeEditorViewModel findOrCreateEditorViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factoryRecipe).get(
                        RecipeEditorViewModel.class);
    }

    static RecipeIdentityViewModel obtainIdentityViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factoryRecipe).get(
                RecipeIdentityViewModel.class);
    }

    static RecipeCourseSelectorViewModel obtainCourseSelectorViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factoryRecipe).get(
                RecipeCourseSelectorViewModel.class);
    }

    static RecipeDurationViewModel obtainDurationViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factoryRecipe).get(
                RecipeDurationViewModel.class);
    }

    static RecipePortionsViewModel obtainPortionsViewModel(
            FragmentActivity activity) {
        ViewModelFactoryRecipe factoryRecipe = ViewModelFactoryRecipe.getInstance(
                activity.getApplication());
        return new ViewModelProvider(activity, factoryRecipe).get(
                RecipePortionsViewModel.class);
    }

    private void setupFragments() {
        ImageEditorFragment imageEditorFragment = findOrCreateImageEditorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                imageEditorFragment,
                R.id.recipe_editor_image_editor_content_frame);

        RecipeIdentityFragment recipeIdentityFragment = findOrCreateRecipeIdentityFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                recipeIdentityFragment,
                R.id.recipe_editor_recipe_identity_content_frame);

        RecipeCourseSelectorFragment courseSelectorFragment = findOrCreateCourseSelectorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                courseSelectorFragment,
                R.id.recipe_editor_course_selector_content_frame);

        RecipeDurationFragment durationFragment = findOrCreateDurationFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                durationFragment,
                R.id.recipe_duration_content_frame);

        RecipePortionsFragment portionsFragment = findOrCreatePortionsFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                portionsFragment,
                R.id.recipe_portions_content_frame);
    }

    @NonNull
    private RecipeIdentityFragment findOrCreateRecipeIdentityFragment() {
        RecipeIdentityFragment recipeIdentityFragment =
                (RecipeIdentityFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_recipe_identity_content_frame);

        if (recipeIdentityFragment == null)
            recipeIdentityFragment = RecipeIdentityFragment.newInstance();
        return recipeIdentityFragment;
    }

    @NonNull
    private ImageEditorFragment findOrCreateImageEditorFragment() {
        ImageEditorFragment imageEditorFragment =
                (ImageEditorFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_image_editor_content_frame);

        if (imageEditorFragment == null)
            imageEditorFragment = ImageEditorFragment.newInstance();
        return imageEditorFragment;
    }

    @NonNull
    private RecipeCourseSelectorFragment findOrCreateCourseSelectorFragment() {
        RecipeCourseSelectorFragment courseSelectorFragment =
                (RecipeCourseSelectorFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_course_selector_content_frame);

        if (courseSelectorFragment == null)
            courseSelectorFragment = RecipeCourseSelectorFragment.newInstance();
        return courseSelectorFragment;
    }

    @NonNull
    private RecipeDurationFragment findOrCreateDurationFragment() {
        RecipeDurationFragment durationFragment =
                (RecipeDurationFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_duration_content_frame);

        if (durationFragment == null)
            durationFragment = RecipeDurationFragment.newInstance();
        return durationFragment;
    }

    @NonNull
    private RecipePortionsFragment findOrCreatePortionsFragment() {
        RecipePortionsFragment portionsFragment =
                (RecipePortionsFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_portions_content_frame);

        if (portionsFragment == null)
            portionsFragment = RecipePortionsFragment.newInstance();
        return portionsFragment;
    }

    private void start() {
        if (getIntent().hasExtra(EXTRA_RECIPE_ID)) {
            recipeEditorViewModel.start(getIntent().getStringExtra(EXTRA_RECIPE_ID));
        } else {
            recipeEditorViewModel.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_recipe_editor_action_review).setVisible(
                recipeEditorViewModel.isShowReviewButton());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_recipe_editor_action_review) {
            recipeEditorViewModel.reviewButtonPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void reviewNewRecipe(String recipeId) {

    }

    @Override
    public void reviewEditedRecipe(String recipeId) {

    }

    @Override
    public void reviewClonedRecipe(String recipeId) {

    }

    @Override
    public void addIngredients(String recipeId) {
        Intent intent = new Intent(this, RecipeIngredientListActivity.class);
        intent.putExtra(RecipeIngredientListActivity.EXTRA_RECIPE_ID, recipeId);
        startActivity(intent);
    }

    @Override
    public void editIngredients(String recipeId) {
    }

    @Override
    public void reviewIngredients(String recipeId) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        recipeEditorViewModel.upOrBackPressed();
    }

    private void showUnsavedChangesDialogEvent() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment previousDialog = getSupportFragmentManager().findFragmentByTag(
                UnsavedChangesDialogFragment.TAG);

        if (previousDialog != null)
            ft.remove(previousDialog);
        ft.addToBackStack(null);

        UnsavedChangesDialogFragment dialogFragment = UnsavedChangesDialogFragment.newInstance(
                this.getTitle().toString());
        dialogFragment.show(ft, UnsavedChangesDialogFragment.TAG);
    }

    @Override
    public void cancelEditing() {
        setResult(RESULT_ADD_EDIT_RECIPE_CANCELLED);
        finish();
    }

    @Override
    protected void onDestroy() {
        recipeEditorViewModel.onActivityDestroyed();
        super.onDestroy();
    }
}