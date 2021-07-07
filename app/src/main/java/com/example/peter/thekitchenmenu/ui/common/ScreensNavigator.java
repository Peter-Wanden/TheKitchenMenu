package com.example.peter.thekitchenmenu.ui.common;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorActivity;

public class ScreensNavigator {

    private final Activity activity;
    private final FragmentManager fragmentManager;

    public ScreensNavigator(Activity activity,
                            FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    public void toRecipeEditor() {
        RecipeEditorActivity.launch(activity);
    }

    public void toRecipeEditor(String recipeDomainId) {
        RecipeEditorActivity.launch(activity, recipeDomainId);
    }

    public void onSupportNavigateUp() {
        activity.onNavigateUp();
    }
}
