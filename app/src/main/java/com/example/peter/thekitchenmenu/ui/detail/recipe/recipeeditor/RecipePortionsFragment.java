package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipePortionsEditorBinding;

public class RecipePortionsFragment extends Fragment {

    static RecipePortionsFragment newInstance() {
        return new RecipePortionsFragment();
    }

    public RecipePortionsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RecipePortionsEditorBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_portions_editor,
                container,
                false);

        binding.setViewModel(RecipeEditorActivity.obtainPortionsViewModel(getActivity()));

        return binding.getRoot();
    }
}
