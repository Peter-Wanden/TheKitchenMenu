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
import com.example.peter.thekitchenmenu.databinding.RecipeDurationFragmentBinding;

public class RecipeDurationFragment extends Fragment {

    static RecipeDurationFragment newInstance() {
        return new RecipeDurationFragment();
    }

    public RecipeDurationFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RecipeDurationFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_duration_fragment,
                container,
                false);

        binding.setViewModel(RecipeEditorActivity.obtainDurationViewModel(getActivity()));

        return binding.getRoot();
    }
}
