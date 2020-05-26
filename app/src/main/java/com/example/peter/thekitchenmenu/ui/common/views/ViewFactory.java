package com.example.peter.thekitchenmenu.ui.common.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogListView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogListViewImpl;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogViewImpl;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemViewImpl;
import com.example.peter.thekitchenmenu.ui.common.dialogs.promptdialog.PromptView;
import com.example.peter.thekitchenmenu.ui.common.dialogs.promptdialog.PromptViewImpl;
import com.example.peter.thekitchenmenu.ui.common.toolbar.ToolbarView;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.RecipeEditorParentViewImpl;

import javax.annotation.Nullable;

public class ViewFactory {

    private final LayoutInflater inflater;

    public ViewFactory(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public RecipeCatalogView getRecipeCatalogView(@Nullable ViewGroup parent) {
        return new RecipeCatalogViewImpl(inflater, parent, this);
    }

    public RecipeCatalogListView getRecipeCatalogListView(@Nullable ViewGroup parent) {
        return new RecipeCatalogListViewImpl(inflater, parent, this);
    }

    public RecipeListItemView getRecipeListItemView(@Nullable ViewGroup parent) {
        return new RecipeListItemViewImpl(inflater, parent);
    }

    public ToolbarView getToolbarView(@Nullable ViewGroup parent) {
        return new ToolbarView(inflater, parent);
    }

    public RecipeEditorParentViewImpl getRecipeEditorParentView(@Nullable ViewGroup parent) {
        return new RecipeEditorParentViewImpl(inflater, parent, this);
    }

    public PromptView getPromptView(@Nullable ViewGroup parent) {
        return new PromptViewImpl(inflater, parent);
    }
}
