package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientPersistenceModel>
        implements DomainDataAccessRecipeIngredient {

    public static RepositoryRecipeIngredient INSTANCE = null;

    private RepositoryRecipeIngredient(@Nonnull DomainDataAccessRecipeIngredient remoteDataSource,
                                       @Nonnull DomainDataAccessRecipeIngredient localDataSource) {
        this.remoteDomainDataAccess = remoteDataSource;
        this.localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeIngredient getInstance(
            @Nonnull DomainDataAccessRecipeIngredient remoteDataSource,
            @Nonnull DomainDataAccessRecipeIngredient localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeIngredient(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {

        List<RecipeIngredientPersistenceModel> cachedEntities = getFromCachedByRecipeDomainId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByRecipeId(
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
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByRecipeId(
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

        List<RecipeIngredientPersistenceModel> cache = getFromCacheByProductDataId(productId);

        if (cache == null || !cache.isEmpty()) {
            callback.onAllLoaded(cache);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByProductId(
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
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByProductId(
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
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByIngredientId(
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
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByIngredientId(
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
                });
    }

    private List<RecipeIngredientPersistenceModel> getFromCachedByRecipeDomainId(
            String recipeDomainId) {

        if (cache == null) {
            return null;
        } else {
            List<RecipeIngredientPersistenceModel> recipeIngredients = new ArrayList<>();

            cache.values().forEach((recipeIngredient) -> {
                if (recipeDomainId.equals(recipeIngredient.getRecipeDomainId())) {
                    recipeIngredients.add(recipeIngredient);
                }
            });

            return recipeIngredients;
        }
    }

    private List<RecipeIngredientPersistenceModel> getFromCacheByProductDataId(
            String productDataId) {

        if (cache == null) {
            return null;
        } else {
            List<RecipeIngredientPersistenceModel> recipeIngredients = new ArrayList<>();

            cache.values().forEach((recipeIngredient) -> {
                if (productDataId.equals(recipeIngredient.getProductDataId())) {
                    recipeIngredients.add(recipeIngredient);
                }
            });

            return recipeIngredients;
        }
    }

    private List<RecipeIngredientPersistenceModel> getFromCacheByIngredientId(
            String recipeIngredientDomainId) {

        if (cache == null) {
            return null;
        } else {
            List<RecipeIngredientPersistenceModel> recipeIngredients = new ArrayList<>();

            cache.values().forEach((recipeIngredient) -> {
                if (recipeIngredientDomainId.equals(recipeIngredient.getDomainId())) {
                    recipeIngredients.add(recipeIngredient);
                }
            });

            return recipeIngredients;
        }
    }
}

