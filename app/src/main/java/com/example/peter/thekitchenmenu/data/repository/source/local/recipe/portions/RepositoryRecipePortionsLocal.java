package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipePortionsLocal
        implements DomainDataAccessRecipePortions {
    @Override
    public void getByRecipeId(@Nonnull String recipeId, @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {

    }

    @Override
    public void getByDataId(@Nonnull String dataId, @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {

    }

    @Override
    public void getActiveByDomainId(@Nonnull String domainId, @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {

    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {

    }

    @Override
    public void save(@Nonnull RecipePortionsPersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
