package com.example.peter.thekitchenmenu.ui.common;

import android.content.Context;
import android.content.Intent;

import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorActivity;

public class ScreensNavigator {

    private final Context context;

    public ScreensNavigator(Context context) {
        this.context = context;
    }

    public void toRecipeEditor(String recipeDomainId) {
        Intent intent = new Intent(context, RecipeEditorActivity.class);
        context.startActivity(intent);
    }

    public void onSupportNavigateUp() {
        context
    }
}
