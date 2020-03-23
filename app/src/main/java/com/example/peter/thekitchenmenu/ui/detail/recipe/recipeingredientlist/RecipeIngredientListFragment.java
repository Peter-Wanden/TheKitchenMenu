package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientListFragmentBinding;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class RecipeIngredientListFragment extends Fragment {

    private RecipeIngredientListFragmentBinding binding;
    private RecipeIngredientListViewModel viewModel;
    private RecipeIngredientListAdapter adapter;

    public RecipeIngredientListFragment() {
    }

    static RecipeIngredientListFragment getInstance() {
        return new RecipeIngredientListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_ingredient_list_fragment,
                container,
                false);
        binding.setLifecycleOwner(this);

        setupViewModel();
        setBindingInstanceVariables();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListAdapter();
    }

    private void setupViewModel() {
        viewModel = RecipeIngredientListActivity.obtainRecipeIngredientListViewModel(getActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setView(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void onDestroy() {
        adapter.onDestroy();
        super.onDestroy();
    }

    private void setupListAdapter() {
        ListView listView = binding.recipeIngredientsList;

        adapter = new RecipeIngredientListAdapter(
                new ArrayList<>(0),
                (RecipeIngredientListActivity) getActivity());

        listView.setAdapter(adapter);
    }
}
