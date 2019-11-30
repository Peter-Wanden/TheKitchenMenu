package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.widget.ListView;

import androidx.databinding.BindingAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist.RecipeIngredientListItemModel;

import java.util.List;

public class RecipeIngredientListBindings {
    @BindingAdapter("recipeIngredientsModels")
    public static void setRecipeIngredients(ListView listView,
                                            List<RecipeIngredientListItemModel> recipeIngredients) {

        RecipeIngredientListFragment.RecipeIngredientListAdapter adapter =
                (RecipeIngredientListFragment.RecipeIngredientListAdapter) listView.getAdapter();
        if (adapter != null)
            adapter.replaceData(recipeIngredients);
    }
}
