package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

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
            @Nonnull GetDomainModelCallback<RecipePortionsUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipePortionsUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void save(@Nonnull RecipePortionsUseCasePersistenceModel model) {

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
    public void deleteByDomainId(@Nonnull String domainId) {

    }
}
