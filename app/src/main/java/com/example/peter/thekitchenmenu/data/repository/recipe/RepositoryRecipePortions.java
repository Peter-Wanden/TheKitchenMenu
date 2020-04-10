package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipePortions
        extends Repository<RecipePortionsPersistenceModel>
        implements DomainDataAccessRecipePortions {

    public static RepositoryRecipePortions INSTANCE;

    private RepositoryRecipePortions(@Nonnull DomainDataAccessRecipePortions remoteDataSource,
                                     @Nonnull DomainDataAccessRecipePortions localDataSource) {
        this.remoteDomainDataAccess = remoteDataSource;
        this.localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipePortions getInstance(
            @Nonnull DomainDataAccessRecipePortions remoteDataSource,
            @Nonnull DomainDataAccessRecipePortions localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipePortions(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {

        List<RecipePortionsPersistenceModel> models = getModelsFromCache(domainId);

        if (!models.isEmpty()) {
            callback.onAllLoaded(models);
            return;
        }
        ((DomainDataAccessRecipePortions) localDomainDataAccess).getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipePortionsPersistenceModel> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipePortionsPersistenceModel m : models) {
                            cache.put(m.getDataId(), m);
                        }
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DomainDataAccessRecipePortions)remoteDomainDataAccess).getAllByDomainId(
                                domainId,
                                new GetAllDomainModelsCallback<RecipePortionsPersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(
                                            List<RecipePortionsPersistenceModel> models) {
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipePortionsPersistenceModel m : models) {
                                            cache.put(m.getDataId(), m);
                                        }
                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onModelsUnavailable() {
                                        callback.onModelsUnavailable();
                                    }
                                }
                        );
                    }
                }
        );
    }

    private List<RecipePortionsPersistenceModel> getModelsFromCache(String domainId) {
        List<RecipePortionsPersistenceModel> models = new ArrayList<>();

        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipePortionsPersistenceModel model : cache.values()) {
                if (domainId.equals(model.getDomainId())) {
                    models.add(model);
                }
            }
            return models;
        }
    }
}
