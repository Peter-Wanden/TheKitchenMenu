package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.widget.ListView;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;

import java.util.List;

public class RecipeIngredientListBindings {
    @BindingAdapter("app:recipeIngredients")
    public static void setRecipeIngredients(ListView listView,
                                            List<RecipeIngredientEntity> recipeIngredients) {

        RecipeIngredientListFragment.RecipeIngredientListAdapter adapter =
                (RecipeIngredientListFragment.RecipeIngredientListAdapter) listView.getAdapter();
        if (adapter != null)
            adapter.replaceData(recipeIngredients);
    }
}
