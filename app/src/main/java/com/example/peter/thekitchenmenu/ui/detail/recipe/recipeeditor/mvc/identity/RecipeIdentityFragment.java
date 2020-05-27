package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.ui.common.controllers.BaseFragment;

import javax.annotation.Nonnull;

public class RecipeIdentityFragment
        extends
        BaseFragment {

    static RecipeIdentityFragment newInstance() {
        return new RecipeIdentityFragment();
    }

    public RecipeIdentityFragment() {}

    private RecipeIdentityEditorController controller;

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {

        RecipeIdentityEditorViewImpl view = getCompositionRoot().
                getViewFactory().
                getRecipeIdentityEditorView(parent);

        controller = getCompositionRoot().getRecipeIdentityEditorController();
        controller.bindView(view);

        return view.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();
        controller.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.onStop();
    }
}