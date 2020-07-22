package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class DataAccessRecipePortions
        extends DataAccess<RecipePortionsPersistenceModel>
        implements DomainDataAccessRecipePortions {

    public static DataAccessRecipePortions INSTANCE;

    private DataAccessRecipePortions(@Nonnull DomainDataAccessRecipePortions remoteDataSource,
                                     @Nonnull DomainDataAccessRecipePortions localDataSource) {
        this.remoteDomainDataAccess = remoteDataSource;
        this.localDomainDataAccess = localDataSource;
    }

    public static DataAccessRecipePortions getInstance(
            @Nonnull DomainDataAccessRecipePortions remoteDataSource,
            @Nonnull DomainDataAccessRecipePortions localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataAccessRecipePortions(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {

        List<RecipePortionsPersistenceModel> models = getModelsFromCache(domainId);

        if (!models.isEmpty()) {
            callback.onAllDomainModelsLoaded(models);
            return;
        }
        ((DomainDataAccessRecipePortions) localDomainDataAccess).getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsPersistenceModel> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipePortionsPersistenceModel m : models) {
                            cache.put(m.getDataId(), m);
                        }
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipePortions)remoteDomainDataAccess).getAllByDomainId(
                                domainId,
                                new GetAllDomainModelsCallback<RecipePortionsPersistenceModel>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(
                                            List<RecipePortionsPersistenceModel> models) {
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipePortionsPersistenceModel m : models) {
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
