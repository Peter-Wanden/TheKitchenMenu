package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata;
import com.example.peter.thekitchenmenu.ui.common.toolbar.ToolbarView;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;
import com.example.peter.thekitchenmenu.ui.common.views.ViewFactory;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeEditorViewImpl
        extends
        BaseObservableViewMvc<RecipeEditorView.ListenerViewInput>
        implements
        RecipeEditorView {

    private final List<ListenerViewActions> viewActions = new ArrayList<>();

    private final ToolbarView toolbarView;
    private final Toolbar toolbar;
    private final MaterialButton ingredientsButton;
    private final FrameLayout imageContentFrame;
    private final FrameLayout identityContentFrame;
    private final FrameLayout courseContentFrame;
    private final FrameLayout durationContentFrame;
    private final FrameLayout portionsContentFrame;
    private final FrameLayout infoContentFrame;

    private Menu optionsMenu;
    private int menuResourceId = R.menu.menu_recipe_editor;
    private boolean isShowReviewButton;

    public RecipeEditorViewImpl(LayoutInflater inflater,
                                @Nullable ViewGroup parent,
                                ViewFactory viewFactory) {

        setRootView(inflater.inflate(
                R.layout.recipe_editor_activity, parent, false)
        );

        optionsMenu = findViewById(menuResourceId);

        toolbar = findViewById(R.id.toolbar);
        toolbarView = viewFactory.getToolbarView(toolbar);
        toolbar.addView(toolbarView.getRootView());
        setToolbarListeners();

        ingredientsButton = findViewById(R.id.recipe_editor_add_ingredients_button);

        imageContentFrame = findViewById(R.id.recipe_editor_image_editor_content_frame);
        identityContentFrame = findViewById(R.id.recipe_editor_recipe_identity_content_frame);
        courseContentFrame = findViewById(R.id.recipe_editor_course_selector_content_frame);
        durationContentFrame = findViewById(R.id.recipe_duration_content_frame);
        portionsContentFrame = findViewById(R.id.recipe_portions_content_frame);
        infoContentFrame = findViewById(R.id.recipe_editor_information_content_frame);
    }

    public void registerListenerViewActions(ListenerViewActions listener) {
        viewActions.add(listener);
    }

    public void unregisterListenerViewActions(ListenerViewActions listener) {
        viewActions.remove(listener);
    }

    private void setToolbarListeners() {
        toolbarView.enableUpButtonAndListen(() -> getListeners().
                forEach(ListenerViewInput::onNavigateUp));
    }

    @Override
    public void isNewRecipe(boolean isNewRecipe) {
        if (isNewRecipe) {
            toolbar.setTitle(getString(R.string.activity_title_add_new_recipe));
        } else {
            toolbar.setTitle(getString(R.string.activity_title_edit_recipe));
        }

        ingredientsButton.setText(isNewRecipe ?
                R.string.add_ingredients :
                R.string.edit_ingredients);
    }

    @Override
    public void setRecipeState(UseCaseMetadata.ComponentState recipeState) {
        if (UseCaseMetadata.ComponentState.VALID_CHANGED == recipeState) {
            ingredientsButton.setVisibility(View.VISIBLE);
            isShowReviewButton = true;

        } else if (UseCaseMetadata.ComponentState.VALID_UNCHANGED == recipeState) {
            ingredientsButton.setVisibility(View.VISIBLE);
            isShowReviewButton = false;

        } else {
            ingredientsButton.setVisibility(View.GONE);
            isShowReviewButton = false;
        }
        invalidateOptionsMenu();
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public int getMenuResourceId() {
        return menuResourceId;
    }

    @Override
    public Menu getOptionsMenu() {
        return optionsMenu;
    }

    public void setOptionsMenu(Menu menu) {
        optionsMenu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem reviewButton = menu.findItem(R.id.menu_recipe_editor_action_review);
        reviewButton.setVisible(isShowReviewButton);
    }

    private void invalidateOptionsMenu() {
        viewActions.forEach(RecipeEditorView.ListenerViewActions::refreshOptionsMenu);
    }

    @Override
    public boolean isReviewButtonPressed(@Nonnull MenuItem item) {
        if (R.id.menu_recipe_editor_action_review == item.getItemId()) {
            getListeners().forEach(ListenerViewInput::onReviewButtonClicked);
            return true;
        }
        return false;
    }

    @Override
    public FrameLayout getImageContentFrame() {
        return imageContentFrame;
    }

    @Override
    public FrameLayout getIdentityContentFrame() {
        return identityContentFrame;
    }

    @Override
    public FrameLayout getCourseContentFrame() {
        return courseContentFrame;
    }

    @Override
    public FrameLayout getDurationContentFrame() {
        return durationContentFrame;
    }

    @Override
    public FrameLayout getPortionsContentFrame() {
        return portionsContentFrame;
    }

    @Override
    public FrameLayout getInfoContentFrame() {
        return infoContentFrame;
    }
}
