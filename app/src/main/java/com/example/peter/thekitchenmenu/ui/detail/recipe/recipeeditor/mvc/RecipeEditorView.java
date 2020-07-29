package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;

import javax.annotation.Nonnull;

public interface RecipeEditorView
        extends
        ObservableViewMvc<RecipeEditorView.ListenerViewInput> {

    interface ListenerViewInput {

        void onReviewButtonClicked();

        void onNavigateUp();

    }

    interface ListenerViewActions {
        void refreshOptionsMenu();
    }

    Toolbar getToolbar();

    int getMenuResourceId();

    Menu getOptionsMenu();

    void onPrepareOptionsMenu(Menu menu);

    boolean isReviewButtonPressed(@Nonnull MenuItem item);

    void isNewRecipe(boolean isNewRecipe);

    void setRecipeState(ComponentState recipeState);

    FrameLayout getImageContentFrame();

    FrameLayout getIdentityContentFrame();

    FrameLayout getCourseContentFrame();

    FrameLayout getDurationContentFrame();

    FrameLayout getPortionsContentFrame();

    FrameLayout getInfoContentFrame();
}
