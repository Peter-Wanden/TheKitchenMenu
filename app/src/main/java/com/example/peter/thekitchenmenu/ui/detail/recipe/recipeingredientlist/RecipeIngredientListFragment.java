package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientListFragmentBinding;
import com.example.peter.thekitchenmenu.databinding.RecipeIngredientListItemBinding;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist.RecipeIngredientListItemModel;
import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.MeasurementToSpannableConverter;

import java.util.ArrayList;
import java.util.List;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
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

    public static class RecipeIngredientListAdapter extends BaseAdapter {

        @Nullable
        private RecipeIngredientListItemNavigator navigator;
        private List<RecipeIngredientListItemModel> recipeIngredients;

        RecipeIngredientListAdapter(List<RecipeIngredientListItemModel> recipeIngredientList,
                                    @Nullable RecipeIngredientListItemNavigator navigator) {
            this.navigator = navigator;
            setList(recipeIngredientList);
        }

        public void onDestroy() {
            navigator = null;
        }

        void replaceData(List<RecipeIngredientListItemModel> ingredientList) {
            setList(ingredientList);
        }

        private void setList(List<RecipeIngredientListItemModel> recipeIngredients) {
            this.recipeIngredients = recipeIngredients;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return recipeIngredients != null ? recipeIngredients.size() : 0;
        }

        @Override
        public RecipeIngredientListItemModel getItem(int i) {
            return recipeIngredients.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RecipeIngredientListItemModel recipeIngredient = getItem(i);
            RecipeIngredientListItemBinding binding;

            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                binding = RecipeIngredientListItemBinding.inflate(
                        inflater,
                        viewGroup,
                        false);
            } else {
                binding = DataBindingUtil.getBinding(view);
            }

            Resources resources = binding.getRoot().getContext().getResources();

            final RecipeIngredientListItemViewModel viewModel =
                    new RecipeIngredientListItemViewModel(
                            new MeasurementToSpannableConverter(resources));

            viewModel.setNavigator(navigator);
            binding.setViewModel(viewModel);
            viewModel.setListItemModel(recipeIngredient);
            return binding.getRoot();
        }
    }
}
