package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

public interface AddEditRecipeNavigator {

    void reviewNewRecipe(RecipeEntity recipeEntity);

    void updateExistingRecipe(RecipeEntity recipeEntity);

    void addIngredients(String recipeId);

    void cancelEditing();
}
