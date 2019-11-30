package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.List;

public class UseCaseRecipeIngredientListResponse implements  UseCase.ResponseValues {
    @NonNull
    private final List<RecipeIngredientListItemModel> listItemModels;

    public UseCaseRecipeIngredientListResponse(
            @NonNull List<RecipeIngredientListItemModel> listItemModels) {
        this.listItemModels = listItemModels;
    }

    @NonNull
    public List<RecipeIngredientListItemModel> getListItemModels() {
        return listItemModels;
    }

    @NonNull
    @Override
    public String toString() {
        return "ResponseValues{" +
                "listItemModels=" + listItemModels +
                '}';
    }
}
