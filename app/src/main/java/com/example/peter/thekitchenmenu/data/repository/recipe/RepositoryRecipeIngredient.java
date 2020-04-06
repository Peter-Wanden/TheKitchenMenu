package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientPersistenceModel>
        implements DataAccessRecipeIngredient {

    public static RepositoryRecipeIngredient INSTANCE = null;

    private RepositoryRecipeIngredient(@Nonnull DataAccessRecipeIngredient remoteDataSource,
                                       @Nonnull DataAccessRecipeIngredient localDataSource) {
        this.remoteDataAccess = remoteDataSource;
        this.localDataAccess = localDataSource;
    }

    public static RepositoryRecipeIngredient getInstance(
            DataAccessRecipeIngredient remoteDataSource,
            DataAccessRecipeIngredient localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeIngredient(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {

        List<RecipeIngredientPersistenceModel> cachedEntities = getFromCachedByRecipeId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DataAccessRecipeIngredient) localDataAccess).getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientPersistenceModel> models) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientPersistenceModel m : models)
                            cache.put(m.getDataId(), m);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeIngredient) remoteDataAccess).getAllByRecipeId(
                                recipeId,
                                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientPersistenceModel> models) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientPersistenceModel m : models)
                                            cache.put(m.getDataId(), m);

                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onModelsUnavailable() {
                                        callback.onModelsUnavailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getAllByProductId(
            @Nonnull String productId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {

        List<RecipeIngredientPersistenceModel> cache = getFromCacheByProductId(productId);

        if (cache == null || !cache.isEmpty()) {
            callback.onAllLoaded(cache);
            return;
        }
        ((DataAccessRecipeIngredient) localDataAccess).getAllByProductId(
                productId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientPersistenceModel> models) {
                        if (RepositoryRecipeIngredient.this.cache == null)
                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                        for (RecipeIngredientPersistenceModel m : models)
                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeIngredient) remoteDataAccess).getAllByProductId(
                                productId,
                                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientPersistenceModel> models) {
                                        if (RepositoryRecipeIngredient.this.cache == null)
                                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                                        for (RecipeIngredientPersistenceModel m : models)
                                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);

                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onModelsUnavailable() {
                                        callback.onModelsUnavailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {

        List<RecipeIngredientPersistenceModel> cache = getFromCacheByIngredientId(ingredientId);
        if (cache != null || !cache.isEmpty()) {
            callback.onAllLoaded(cache);
            return;
        }
        ((DataAccessRecipeIngredient) localDataAccess).getAllByIngredientId(
                ingredientId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientPersistenceModel> models) {
                        if (RepositoryRecipeIngredient.this.cache == null)
                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                        for (RecipeIngredientPersistenceModel m : models)
                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeIngredient) remoteDataAccess).getAllByIngredientId(
                                ingredientId,
                                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientPersistenceModel> models) {
                                        if (RepositoryRecipeIngredient.this.cache == null)
                                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                                        for (RecipeIngredientPersistenceModel m : models)
                                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);

                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onModelsUnavailable() {
                                        callback.onModelsUnavailable();
                                    }
                                });
                    }
                }
        );
    }

    private List<RecipeIngredientPersistenceModel> getFromCachedByRecipeId(@Nonnull String recipeId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientPersistenceModel> models = new ArrayList<>();

            for (RecipeIngredientPersistenceModel m : cache.values())
                if (recipeId.equals(m.getDomainId()))
                    models.add(m);

            return models;
        }
    }

    private List<RecipeIngredientPersistenceModel> getFromCacheByProductId(@Nonnull String productId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientPersistenceModel> models = new ArrayList<>();

            for (RecipeIngredientPersistenceModel m : models)
                if (productId.equals(m.getProductId()))
                    models.add(m);

            return models;
        }
    }

    private List<RecipeIngredientPersistenceModel> getFromCacheByIngredientId(@Nonnull String ingredientId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientPersistenceModel> models = new ArrayList<>();
            for (RecipeIngredientPersistenceModel m : models)
                if (ingredientId.equals(m.getIngredientDomainId()))
                    models.add(m);
            return models;
        }
    }
}
