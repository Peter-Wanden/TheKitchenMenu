package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;

import javax.annotation.Nonnull;

public class RecipeCourseEditorFragment extends Fragment {


    public static RecipeCourseEditorFragment newInstance() {
        return new RecipeCourseEditorFragment();
    }

    public RecipeCourseEditorFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        RecipeCourseEditorFragmentBinding binding = DataBindingUtil.inflate(
//                inflater,
//                R.layout.recipe_course_editor_fragment,
//                container,
//                false);

//        binding.setViewModel(new ViewModelProvider(requireActivity()).
//                get(RecipeCourseEditorViewModel.class));

//        return binding.getRoot();
        return null;
    }
}