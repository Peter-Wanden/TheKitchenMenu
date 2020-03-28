package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsPersistenceModel;

import javax.annotation.Nonnull;

public interface DataSourceRecipePortions extends DataSource<RecipePortionsPersistenceModel> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetModelCallback<RecipePortionsPersistenceModel> callback);
}
