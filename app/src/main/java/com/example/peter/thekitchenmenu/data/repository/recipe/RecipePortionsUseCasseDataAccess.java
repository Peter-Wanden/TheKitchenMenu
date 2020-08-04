package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipePortionsUseCasseDataAccess
        extends DataAccess<RecipePortionsUseCasePersistenceModel>
        implements DomainDataAccessRecipePortions {

    public static RecipePortionsUseCasseDataAccess INSTANCE;

    private RecipePortionsUseCasseDataAccess(@Nonnull DomainDataAccessRecipePortions remoteDataSource,
                                             @Nonnull DomainDataAccessRecipePortions localDataSource) {
        this.remoteDomainDataAccess = remoteDataSource;
        this.localDomainDataAccess = localDataSource;
    }

    public static RecipePortionsUseCasseDataAccess getInstance(
            @Nonnull DomainDataAccessRecipePortions remoteDataSource,
            @Nonnull DomainDataAccessRecipePortions localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipePortionsUseCasseDataAccess(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel> callback) {

        List<RecipePortionsUseCasePersistenceModel> models = getModelsFromCache(domainId);

        if (!models.isEmpty()) {
            callback.onAllDomainModelsLoaded(models);
            return;
        }
        ((DomainDataAccessRecipePortions) localDomainDataAccess).getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsUseCasePersistenceModel> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipePortionsUseCasePersistenceModel m : models) {
                            cache.put(m.getDataId(), m);
                        }
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipePortions)remoteDomainDataAccess).getAllByDomainId(
                                domainId,
                                new GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(
                                            List<RecipePortionsUseCasePersistenceModel> models) {
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipePortionsUseCasePersistenceModel m : models) {
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

    private List<RecipePortionsUseCasePersistenceModel> getModelsFromCache(String domainId) {
        List<RecipePortionsUseCasePersistenceModel> models = new ArrayList<>();

        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipePortionsUseCasePersistenceModel model : cache.values()) {
                if (domainId.equals(model.getDomainId())) {
                    models.add(model);
                }
            }
            return models;
        }
    }
}
