package com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.
        UseCaseRecipeIdentityAndDurationList.*;

public class UseCaseRecipeIdentityAndDurationListResponseModel
        implements UseCaseInteractor.Response {

    @Nonnull
    private final ResultStatus resultStatus;

    @Nonnull
    private final List<RecipeListItemModel> recipeListItemModels;

    public UseCaseRecipeIdentityAndDurationListResponseModel(
            @Nonnull ResultStatus resultStatus,
            @Nonnull List<RecipeListItemModel> recipeListItemModels) {
        this.resultStatus = resultStatus;
        this.recipeListItemModels = recipeListItemModels;
    }

    @Nonnull
    public ResultStatus getResultStatus() {
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
        UseCaseRecipeIdentityAndDurationListResponseModel that =
                (UseCaseRecipeIdentityAndDurationListResponseModel) o;
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
        return "UseCaseRecipeIdentityAndDurationListResponseModel{" +
                "resultStatus=" + resultStatus +
                ", recipeListItemModels=" + recipeListItemModels +
                '}';
    }
}
