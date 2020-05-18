package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.widget.FrameLayout;

public interface RecipeNavigator {

        void onRecipeClicked(String recipeDomainId);

        void onAddRecipeClicked();

        void onNavigateUpClicked();
}
