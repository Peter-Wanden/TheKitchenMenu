package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeMetaData;

import javax.annotation.Nonnull;

public class RecipeMetaDataModelAdapter {

    @Nonnull
    private final RepositoryRecipeMetaData recipeMetaDataRepository;
    @Nonnull
    private final RepositoryRecipeComponentStates recipeComponentStateRepository;
    @Nonnull
    private final RepositoryRecipeFailReasons recipeFailReasonsRepository;
    @Nonnull
    private final TimeProvider timeProvider;


}
