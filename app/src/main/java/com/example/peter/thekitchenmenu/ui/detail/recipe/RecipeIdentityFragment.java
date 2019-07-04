package com.example.peter.thekitchenmenu.ui.detail.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIdentityFragmentBinding;

public class RecipeIdentityFragment extends Fragment {

    private static final String TAG = "tkm-RecipeIdentityFrag";

    public static final String ARGUMENT_EDIT_RECIPE_ID = "EDIT_RECIPE_ID";

    private RecipeIdentityFragmentBinding binding;
    private RecipeIdentityViewModel viewModel;

    static RecipeIdentityFragment newInstance(String recipeId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_EDIT_RECIPE_ID, recipeId);
        RecipeIdentityFragment fragment = new RecipeIdentityFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public RecipeIdentityFragment(){}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        viewModel.getTitleErrorEvent().observe(this, this::titleValidationError);
        viewModel.getDescriptionErrorEvent().observe(this, this::descriptionValidationError);
        viewModel.getPrepTimeErrorEvent().observe(this, this::prepTimeValidationErrorEvent);
        viewModel.getCookTimeErrorEvent().observe(this, this::cookTimeValidationErrorEvent);
    }

    private void titleValidationError(String errorMessage) {
        binding.editableRecipeEditorTitle.setError(errorMessage);
    }

    private void descriptionValidationError(String errorMessage) {
        binding.editableRecipeDescription.setError(errorMessage);
    }

    private void prepTimeValidationErrorEvent(String errorMessage) {

    }

    private void cookTimeValidationErrorEvent(String errorMessage) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_identity_fragment,
                container,
                false);

        setupViewModel();
        setBindingInstanceVariables();

        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    private void setupViewModel() {
        viewModel = RecipeEditorActivity.obtainIdentityViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }
}
