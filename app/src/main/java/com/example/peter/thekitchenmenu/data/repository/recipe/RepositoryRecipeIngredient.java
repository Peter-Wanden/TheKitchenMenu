package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceDomainModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientPersistenceDomainModel>
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {

        List<RecipeIngredientPersistenceDomainModel> cachedEntities = getFromCachedByRecipeDomainId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllDomainModelsLoaded(cachedEntities);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientPersistenceDomainModel m : models)
                            cache.put(m.getDataId(), m);

                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByRecipeId(
                                recipeId,
                                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientPersistenceDomainModel m : models)
                                            cache.put(m.getDataId(), m);

                                        callback.onAllDomainModelsLoaded(models);
                                    }

                                    @Override
                                    public void onDomainModelsUnavailable() {
                                        callback.onDomainModelsUnavailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getAllByProductId(
            @Nonnull String productId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {

        List<RecipeIngredientPersistenceDomainModel> cache = getFromCacheByProductDataId(productId);

        if (cache == null || !cache.isEmpty()) {
            callback.onAllDomainModelsLoaded(cache);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByProductId(
                productId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                        if (RepositoryRecipeIngredient.this.cache == null)
                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                        for (RecipeIngredientPersistenceDomainModel m : models)
                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);

                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByProductId(
                                productId,
                                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                                        if (RepositoryRecipeIngredient.this.cache == null)
                                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                                        for (RecipeIngredientPersistenceDomainModel m : models)
                                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);

                                        callback.onAllDomainModelsLoaded(models);
                                    }

                                    @Override
                                    public void onDomainModelsUnavailable() {
                                        callback.onDomainModelsUnavailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {

        List<RecipeIngredientPersistenceDomainModel> cache = getFromCacheByIngredientId(ingredientId);
        if (cache != null || !cache.isEmpty()) {
            callback.onAllDomainModelsLoaded(cache);
            return;
        }
        ((DomainDataAccessRecipeIngredient) localDomainDataAccess).getAllByIngredientId(
                ingredientId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                        if (RepositoryRecipeIngredient.this.cache == null)
                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                        for (RecipeIngredientPersistenceDomainModel m : models)
                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipeIngredient) remoteDomainDataAccess).getAllByIngredientId(
                                ingredientId,
                                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                                        if (RepositoryRecipeIngredient.this.cache == null)
                                            RepositoryRecipeIngredient.this.cache = new LinkedHashMap<>();

                                        for (RecipeIngredientPersistenceDomainModel m : models)
                                            RepositoryRecipeIngredient.this.cache.put(m.getDataId(), m);

                                        callback.onAllDomainModelsLoaded(models);
                                    }

                                    @Override
                                    public void onDomainModelsUnavailable() {
                                        callback.onDomainModelsUnavailable();
                                    }
                                });
                    }
                });
    }

    private List<RecipeIngredientPersistenceDomainModel> getFromCachedByRecipeDomainId(
            String recipeDomainId) {

        if (cache == null) {
            return null;
        } else {
            List<RecipeIngredientPersistenceDomainModel> recipeIngredients = new ArrayList<>();

            cache.values().forEach((recipeIngredient) -> {
                if (recipeDomainId.equals(recipeIngredient.getRecipeDomainId())) {
                    recipeIngredients.add(recipeIngredient);
                }
            });

            return recipeIngredients;
        }
    }

    private List<RecipeIngredientPersistenceDomainModel> getFromCacheByProductDataId(
            String productDataId) {

        if (cache == null) {
            return null;
        } else {
            List<RecipeIngredientPersistenceDomainModel> recipeIngredients = new ArrayList<>();

            cache.values().forEach((recipeIngredient) -> {
                if (productDataId.equals(recipeIngredient.getProductDataId())) {
                    recipeIngredients.add(recipeIngredient);
                }
            });

            return recipeIngredients;
        }
    }

    private List<RecipeIngredientPersistenceDomainModel> getFromCacheByIngredientId(
            String recipeIngredientDomainId) {

        if (cache == null) {
            return null;
        } else {
            List<RecipeIngredientPersistenceDomainModel> recipeIngredients = new ArrayList<>();

            cache.values().forEach((recipeIngredient) -> {
                if (recipeIngredientDomainId.equals(recipeIngredient.getDomainId())) {
                    recipeIngredients.add(recipeIngredient);
                }
            });

            return recipeIngredients;
        }
    }
}

