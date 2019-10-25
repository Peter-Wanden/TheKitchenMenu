package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.widget.ListView;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;

import java.util.List;

public class RecipeIngredientListBindings {
    @BindingAdapter("recipeIngredients")
    public static void setRecipeIngredients(ListView listView,
                                            List<RecipeIngredientQuantityEntity> recipeIngredients) {

        RecipeIngredientListFragment.RecipeIngredientListAdapter adapter =
                (RecipeIngredientListFragment.RecipeIngredientListAdapter) listView.getAdapter();
        if (adapter != null)
            adapter.replaceData(recipeIngredients);
    }
}
