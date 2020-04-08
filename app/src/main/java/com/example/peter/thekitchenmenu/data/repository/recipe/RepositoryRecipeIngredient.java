package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientModelPersistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientModelPersistence>
        implements DomainDataAccessRecipeIngredient {

    public static RepositoryRecipeIngredient INSTANCE = null;

    private RepositoryRecipeIngredient(@Nonnull DomainDataAccessRecipeIngredient remoteDataSource,
                                       @Nonnull DomainDataAccessRecipeIngredient localDataSource) {
        this.remoteDomainDataAccess = remoteDataSource;
        this.localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeIngredient getInstance(
            DomainDataAccessRecipeIngredient remoteDataSource,
            DomainDataAccessRecipeIngredient localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeIngredient(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback) {

        List<RecipeIngredientModelPersistence> cachedEntities = getFromCachedByRecipeId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeIngredientModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientModelPersistence> models) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientModelPersistence m : models)
                            cache.put(m.getDataId(), m);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByRecipeId(
                                recipeId,
                                new GetAllDomainModelsCallback<RecipeIngredientModelPersistence>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientModelPersistence> models) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientModelPersistence m : models)
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback) {

        List<RecipeIngredientModelPersistence> cache = getFromCacheByProductId(productId);

        if (cache == null || !cache.isEmpty()) {
            callback.onAllLoaded(cache);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByProductId(
                productId,
                new GetAllDomainModelsCallback<RecipeIngredientModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientModelPersistence> models) {
                        if (RepositoryRecipeIngredient.this.cache == null)
                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                        for (RecipeIngredientModelPersistence m : models)
                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByProductId(
                                productId,
                                new GetAllDomainModelsCallback<RecipeIngredientModelPersistence>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientModelPersistence> models) {
                                        if (RepositoryRecipeIngredient.this.cache == null)
                                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                                        for (RecipeIngredientModelPersistence m : models)
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback) {

        List<RecipeIngredientModelPersistence> cache = getFromCacheByIngredientId(ingredientId);
        if (cache != null || !cache.isEmpty()) {
            callback.onAllLoaded(cache);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByIngredientId(
                ingredientId,
                new GetAllDomainModelsCallback<RecipeIngredientModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientModelPersistence> models) {
                        if (RepositoryRecipeIngredient.this.cache == null)
                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                        for (RecipeIngredientModelPersistence m : models)
                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByIngredientId(
                                ingredientId,
                                new GetAllDomainModelsCallback<RecipeIngredientModelPersistence>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientModelPersistence> models) {
                                        if (RepositoryRecipeIngredient.this.cache == null)
                                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                                        for (RecipeIngredientModelPersistence m : models)
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

    private List<RecipeIngredientModelPersistence> getFromCachedByRecipeId(@Nonnull String recipeId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientModelPersistence> models = new ArrayList<>();

            for (RecipeIngredientModelPersistence m : cache.values())
                if (recipeId.equals(m.getDomainId()))
                    models.add(m);

            return models;
        }
    }

    private List<RecipeIngredientModelPersistence> getFromCacheByProductId(@Nonnull String productId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientModelPersistence> models = new ArrayList<>();

            for (RecipeIngredientModelPersistence m : models)
                if (productId.equals(m.getProductId()))
                    models.add(m);

            return models;
        }
    }

    private List<RecipeIngredientModelPersistence> getFromCacheByIngredientId(@Nonnull String ingredientId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientModelPersistence> models = new ArrayList<>();
            for (RecipeIngredientModelPersistence m : models)
                if (ingredientId.equals(m.getIngredientDomainId()))
                    models.add(m);
            return models;
        }
    }
}
