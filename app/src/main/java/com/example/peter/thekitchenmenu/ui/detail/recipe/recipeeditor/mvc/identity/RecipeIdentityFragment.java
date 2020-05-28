package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.ui.common.controllers.BaseFragment;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity.RecipeIdentityEditorController.UseCaseComponentChangedListener;

import javax.annotation.Nonnull;

public class RecipeIdentityFragment
        extends
        BaseFragment {

    private static final String RECIPE_DOMAIN_ID = "RECIPE_DOMAIN_ID";

    public static RecipeIdentityFragment newInstance(String recipeDomainId) {
        Bundle arguments = new Bundle();
        arguments.putString(RECIPE_DOMAIN_ID, recipeDomainId);
        RecipeIdentityFragment fragment = new RecipeIdentityFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

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
        controller.setRecipeDomainId(getRecipeDomainId());

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
        controller.unregisterUseCaseComponentChangedListener(
                (UseCaseComponentChangedListener) requireActivity());
        controller.onStop();
    }

    private String getRecipeDomainId() {
        return getArguments().getString(RECIPE_DOMAIN_ID);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensures the host activity implements component changed callback
        try {
            controller.registerUseCaseComponentChangedListener(
                    (UseCaseComponentChangedListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement UseCaseComponentChangedListener");
        }
    }
}