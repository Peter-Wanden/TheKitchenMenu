package com.example.peter.thekitchenmenu.domain.usecase.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeListResponse implements UseCase.Response {
    @Nonnull
    private final RecipeList.ResultStatus resultStatus;

    @Nonnull
    private final List<RecipeListItemModel> recipeListItemModels;

    public RecipeListResponse(
            @Nonnull RecipeList.ResultStatus resultStatus,
            @Nonnull List<RecipeListItemModel> recipeListItemModels) {
        this.resultStatus = resultStatus;
        this.recipeListItemModels = recipeListItemModels;
    }

    @Nonnull
    public RecipeList.ResultStatus getResultStatus() {
        return resultStatus;
    }

    @Nonnull
    public List<RecipeListItemModel> getRecipeListItemModels() {
        return recipeListItemModels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeListResponse that = (RecipeListResponse) o;
        return resultStatus == that.resultStatus &&
                recipeListItemModels.equals(that.recipeListItemModels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultStatus, recipeListItemModels);
    }

    @Nonnull
    @Override
    public String toString() {
        return "Response{" +
                "resultStatus=" + resultStatus +
                ", recipeListItemModels=" + recipeListItemModels +
                '}';
    }
}
