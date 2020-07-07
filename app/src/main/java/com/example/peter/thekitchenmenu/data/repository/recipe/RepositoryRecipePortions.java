package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceDomainModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipePortions
        extends Repository<RecipePortionsPersistenceDomainModel>
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
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceDomainModel> callback) {

        List<RecipePortionsPersistenceDomainModel> models = getModelsFromCache(domainId);

        if (!models.isEmpty()) {
            callback.onAllDomainModelsLoaded(models);
            return;
        }
        ((DomainDataAccessRecipePortions) localDomainDataAccess).getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsPersistenceDomainModel> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipePortionsPersistenceDomainModel m : models) {
                            cache.put(m.getDataId(), m);
                        }
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipePortions)remoteDomainDataAccess).getAllByDomainId(
                                domainId,
                                new GetAllDomainModelsCallback<RecipePortionsPersistenceDomainModel>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(
                                            List<RecipePortionsPersistenceDomainModel> models) {
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipePortionsPersistenceDomainModel m : models) {
                                            cache.put(m.getDataId(), m);
                                        }
                                        callback.onAllDomainModelsLoaded(models);
                                    }

                                    @Override
                                    public void onDomainModelsUnavailable() {
                                        callback.onDomainModelsUnavailable();
                                    }
                                }
                        );
                    }
                }
        );
    }

    private List<RecipePortionsPersistenceDomainModel> getModelsFromCache(String domainId) {
        List<RecipePortionsPersistenceDomainModel> models = new ArrayList<>();

        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipePortionsPersistenceDomainModel model : cache.values()) {
                if (domainId.equals(model.getDomainId())) {
                    models.add(model);
                }
            }
            return models;
        }
    }
}
