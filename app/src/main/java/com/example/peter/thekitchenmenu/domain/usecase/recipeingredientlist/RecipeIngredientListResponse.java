package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.List;

import javax.annotation.Nonnull;

public final class RecipeIngredientListResponse implements UseCaseInteractor.Response {
    @Nonnull
    private final List<RecipeIngredientListItemModel> listItemModels;

    public RecipeIngredientListResponse(
            @Nonnull List<RecipeIngredientListItemModel> listItemModels) {
        this.listItemModels = listItemModels;
    }

    @Nonnull
    public List<RecipeIngredientListItemModel> getListItemModels() {
        return listItemModels;
    }

    @Nonnull
    @Override
    public String toString() {
        return "Response{" +
                "listItemModels=" + listItemModels +
                '}';
    }
}
