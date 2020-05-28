package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc;

import androidx.appcompat.widget.Toolbar;

import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.AddEditRecipeNavigator;

public interface RecipeEditorView
        extends
        ObservableViewMvc<AddEditRecipeNavigator> {

    Toolbar getToolbar();

    void setTitle(int titleResourceId);

    void setIngredientsButtonText(int stringResourceId);
}
