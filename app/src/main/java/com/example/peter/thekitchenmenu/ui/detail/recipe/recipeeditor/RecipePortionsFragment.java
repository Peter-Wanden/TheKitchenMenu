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

public class RecipePortionsFragment extends Fragment {

    static RecipePortionsFragment newInstance() {
        return new RecipePortionsFragment();
    }

    public RecipePortionsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        RecipePortionsEditorBinding binding = DataBindingUtil.inflate(
//                inflater,
//                R.layout.recipe_portions_editor,
//                container,
//                false);
//        binding.setLifecycleOwner(this);

//        binding.setViewModel(
//                new ViewModelProvider(requireActivity()).
//                get(RecipePortionsEditorViewModel.class));

//        return binding.getRoot();
        return null;
    }
}
