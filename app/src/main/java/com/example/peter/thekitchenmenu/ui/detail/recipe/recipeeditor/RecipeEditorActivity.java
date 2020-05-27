package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.ui.UnsavedChangesDialogFragment;
import com.example.peter.thekitchenmenu.ui.common.ScreensNavigator;
import com.example.peter.thekitchenmenu.ui.common.controllers.BackPressedDispatcher;
import com.example.peter.thekitchenmenu.ui.common.controllers.BackPressedListener;
import com.example.peter.thekitchenmenu.ui.common.controllers.BaseActivity;
import com.example.peter.thekitchenmenu.ui.common.fragmentFrameHelper.FragmentFrameWrapper;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.RecipeEditorParentViewImpl;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity.RecipeIdentityFragment;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeIngredientListActivity;
import com.example.peter.thekitchenmenu.ui.imageeditor.ImageEditorFragment;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe.CREATE_NEW_RECIPE;

public class RecipeEditorActivity
        extends
        BaseActivity
        implements
        AddEditRecipeNavigator,
        BackPressedDispatcher,
        FragmentFrameWrapper {

    private static final String TAG = "tkm-" + RecipeEditorActivity.class.getSimpleName() + ": ";

    public static final String EXTRA_RECIPE_DOMAIN_ID = "";
    public static final int REQUEST_ADD_EDIT_RECIPE = 50;
    public static final int RESULT_ADD_EDIT_RECIPE_OK = RESULT_FIRST_USER + 1;
    public static final int RESULT_ADD_EDIT_RECIPE_CANCELLED = RESULT_FIRST_USER + 2;

    private final Set<BackPressedListener> backPressedListeners = new HashSet<>();

    private RecipeEditorParentViewImpl view;
    private ScreensNavigator screensNavigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screensNavigator = getCompositionRoot().getScreensNavigator();
        view = getCompositionRoot().getViewFactory().getRecipeEditorParentView(null);
        setContentView(view.getRootView());
    }

    private void setupActionBar() {
//        setSupportActionBar(binding.recipeEditorToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (getIntent().getStringExtra(EXTRA_RECIPE_DOMAIN_ID) != null) {
            actionBar.setTitle(R.string.activity_title_edit_recipe);
        } else {
            actionBar.setTitle(R.string.activity_title_add_new_recipe);
        }
    }

    private void setupFragments() {
        ImageEditorFragment imageEditorFragment = findOrCreateImageEditorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                imageEditorFragment,
                R.id.recipe_editor_image_editor_content_frame);

        RecipeIdentityFragment recipeIdentityFragment = findOrCreateIdentityEditorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                recipeIdentityFragment,
                R.id.recipe_editor_recipe_identity_content_frame);

        RecipeCourseEditorFragment courseSelectorFragment = findOrCreateCourseSelectorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                courseSelectorFragment,
                R.id.recipe_editor_course_selector_content_frame);

        RecipeDurationFragment durationFragment = findOrCreateDurationEditorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                durationFragment,
                R.id.recipe_duration_content_frame);

        RecipePortionsFragment portionsFragment = findOrCreatePortionsEditorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                portionsFragment,
                R.id.recipe_portions_content_frame);
    }

    @Nonnull
    private ImageEditorFragment findOrCreateImageEditorFragment() {
        ImageEditorFragment imageEditorFragment =
                (ImageEditorFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_image_editor_content_frame);

        if (imageEditorFragment == null) {
            imageEditorFragment = ImageEditorFragment.newInstance();
        }
        return imageEditorFragment;
    }

    @Nonnull
    private RecipeIdentityFragment findOrCreateIdentityEditorFragment() {
        RecipeIdentityFragment recipeIdentityFragment =
                (RecipeIdentityFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_recipe_identity_content_frame);

        if (recipeIdentityFragment == null) {
            recipeIdentityFragment = RecipeIdentityFragment.newInstance();
        }
        return recipeIdentityFragment;
    }

    @Nonnull
    private RecipeCourseEditorFragment findOrCreateCourseSelectorFragment() {
        RecipeCourseEditorFragment courseSelectorFragment =
                (RecipeCourseEditorFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_editor_course_selector_content_frame);

        if (courseSelectorFragment == null) {
            courseSelectorFragment = RecipeCourseEditorFragment.newInstance();
        }
        return courseSelectorFragment;
    }

    @Nonnull
    private RecipeDurationFragment findOrCreateDurationEditorFragment() {
        RecipeDurationFragment durationFragment =
                (RecipeDurationFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_duration_content_frame);

        if (durationFragment == null) {
            durationFragment = RecipeDurationFragment.newInstance();
        }
        return durationFragment;
    }

    @Nonnull
    private RecipePortionsFragment findOrCreatePortionsEditorFragment() {
        RecipePortionsFragment portionsFragment =
                (RecipePortionsFragment) getSupportFragmentManager().
                        findFragmentById(R.id.recipe_portions_content_frame);

        if (portionsFragment == null) {
            portionsFragment = RecipePortionsFragment.newInstance();
        }
        return portionsFragment;
    }

    private void start() {
        String recipeId = getIntent().hasExtra(EXTRA_RECIPE_DOMAIN_ID) ?
                getIntent().getStringExtra(EXTRA_RECIPE_DOMAIN_ID) :
                CREATE_NEW_RECIPE;

        RecipeRequest request = new RecipeRequest.Builder().setDomainId(recipeId).build();
        // TODO -
        //  if there is no recipe id create a new recipe
        //  if there is a recipe id, load the recipe:
        //  - if the recipe creator is not the user, copy the recipe
        //  - if the recipe creator is the user, edit the recipe
        //  - if the recipe is being used by others, make a copy and allow uses to update their
        //     copy if they want to
        //  See {@link RecipeEditorViewModel}
    }

    @Override
    public void setActivityTitle(int activityTitleResourceId) {
        setTitle(activityTitleResourceId);
    }

    @Override
    public void refreshOptionsMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.menu_recipe_editor_action_review).setVisible(
//                recipeEditorViewModel.isShowReviewButton());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item) {
        if (item.getItemId() == R.id.menu_recipe_editor_action_review) {
//            recipeEditorViewModel.reviewButtonPressed();
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
//        recipeEditorViewModel.upOrBackPressed();
    }

    @Override
    public void showUnsavedChangedDialog() {
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
        boolean isBackPressedConsumedByAnyListener = false;
        for (BackPressedListener listener : backPressedListeners) {
            if (listener.onBackPressed()) {
                isBackPressedConsumedByAnyListener = true;
            }
        }
        if (isBackPressedConsumedByAnyListener) {
            setResult(RESULT_ADD_EDIT_RECIPE_CANCELLED);
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        view.registerListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        view.unregisterListener(this);
    }

    @Override
    public void registerListener(BackPressedListener listener) {
        backPressedListeners.add(listener);
    }

    @Override
    public void unregisterListener(BackPressedListener listener) {
        backPressedListeners.remove(listener);
    }

    @Override
    public FrameLayout getFragmentFrame() {
        return view.getFragmentFrame();
    }

    /*
    Static methods for starting this activity
    */
    // Create new recipe
    public static void launch(Context context) {
        Intent intent = new Intent(context, RecipeEditorActivity.class);
        context.startActivity(intent);
    }

    // Edit recipe
    public static void launch(Context context, String recipeDomainId) {
        Intent intent = new Intent(context, RecipeEditorActivity.class);
        intent.putExtra(EXTRA_RECIPE_DOMAIN_ID, recipeDomainId);
        context.startActivity(intent);
    }
}