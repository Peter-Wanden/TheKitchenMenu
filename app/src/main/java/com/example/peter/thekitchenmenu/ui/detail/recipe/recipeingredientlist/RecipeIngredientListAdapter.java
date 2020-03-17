package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.peter.thekitchenmenu.databinding.RecipeIngredientListItemBinding;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist.RecipeIngredientListItemModel;
import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.MeasurementToSpannableConverter;

import java.util.List;

public class RecipeIngredientListAdapter extends BaseAdapter {

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
