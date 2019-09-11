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
import com.example.peter.thekitchenmenu.databinding.RecipeCourseSelectorFragmentBinding;

public class RecipeCourseSelectorFragment extends Fragment {

    public static RecipeCourseSelectorFragment newInstance() {
        return new RecipeCourseSelectorFragment();
    }

    public RecipeCourseSelectorFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RecipeCourseSelectorFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_course_selector_fragment,
                container,
                false);

        binding.setViewModel(RecipeEditorActivity.obtainCourseSelectorViewModel(
                getActivity()));

        return binding.getRoot();
    }
}