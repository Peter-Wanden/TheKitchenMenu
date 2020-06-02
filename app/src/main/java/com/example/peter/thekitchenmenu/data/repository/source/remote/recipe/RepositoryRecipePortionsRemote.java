package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipePortionsRemote
        implements DomainDataAccessRecipePortions {

    private static RepositoryRecipePortionsRemote INSTANCE;

    public static RepositoryRecipePortionsRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipePortionsRemote();
        return INSTANCE;
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {
        callback.dataSourceOnDomainModelUnavailable();
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {
        callback.dataSourceOnDomainModelUnavailable();
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {
        callback.onModelsUnavailable();
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
    public void deleteAll() {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }
}
