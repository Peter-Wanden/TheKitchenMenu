package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.common.toolbar.ToolbarView;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;
import com.example.peter.thekitchenmenu.ui.common.views.ViewFactory;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.AddEditRecipeNavigator;

import javax.annotation.Nullable;

public class RecipeEditorParentViewImpl
        extends
        BaseObservableViewMvc<AddEditRecipeNavigator>
        implements
        RecipeEditorParentView {

    private final ToolbarView toolbarView;
    private final Toolbar toolbar;

    public RecipeEditorParentViewImpl(LayoutInflater inflater,
                                      @Nullable ViewGroup parent,
                                      ViewFactory viewFactory) {

        setRootView(inflater.inflate(
                R.layout.recipe_editor_activity, parent, false)
        );

        toolbar = findViewById(R.id.toolbar);
        toolbarView = viewFactory.getToolbarView(toolbar);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbarView.setTitle(getContext().
                getResources().
                getString(R.string.activity_title_edit_recipe));
        toolbar.addView(toolbarView.getRootView());
        toolbarView.enableUpButtonAndListen(() -> getListeners().
                forEach(AddEditRecipeNavigator::cancelEditing));
    }
}
