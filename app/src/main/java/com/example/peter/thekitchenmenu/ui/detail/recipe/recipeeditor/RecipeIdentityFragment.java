package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIdentityEditorFragmentBinding;

public class RecipeIdentityFragment extends Fragment {

    static RecipeIdentityFragment newInstance() {
        return new RecipeIdentityFragment();
    }

    public RecipeIdentityFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RecipeIdentityEditorFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_identity_editor_fragment,
                container,
                false);
        binding.setLifecycleOwner(this);

        binding.setViewModel(new ViewModelProvider(requireActivity()).
                get(RecipeIdentityEditorViewModel.class));

        return binding.getRoot();
    }
}