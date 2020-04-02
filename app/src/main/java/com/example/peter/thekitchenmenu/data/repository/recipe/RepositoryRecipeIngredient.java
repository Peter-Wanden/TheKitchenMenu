package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientEntity>
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
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> cachedEntities = getFromCachedByRecipeId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DataAccessRecipeIngredient) localDataAccess).getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            cache.put(entity.getDataId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeIngredient) remoteDataAccess).getAllByRecipeId(
                                recipeId,
                                new GetAllDomainModelsCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            cache.put(entity.getDataId(), entity);

                                        callback.onAllLoaded(entities);
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
    public void getAllByProductId(@Nonnull String productId,
                                  @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> cachedEntities = getFromCacheByProductId(productId);

        if (cachedEntities == null || !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DataAccessRecipeIngredient) localDataAccess).getAllByProductId(
                productId,
                new GetAllDomainModelsCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            cache.put(entity.getDataId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeIngredient) remoteDataAccess).getAllByProductId(
                                productId,
                                new GetAllDomainModelsCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            cache.put(entity.getDataId(), entity);

                                        callback.onAllLoaded(entities);
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
    public void getAllByIngredientId(@Nonnull String ingredientId,
                                     @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> entities = getFromCacheByIngredientId(ingredientId);
        if (entities != null || !entities.isEmpty()) {
            callback.onAllLoaded(entities);
            return;
        }
        ((DataAccessRecipeIngredient) localDataAccess).getAllByIngredientId(
                ingredientId,
                new GetAllDomainModelsCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            cache.put(entity.getDataId(), entity);
                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeIngredient) remoteDataAccess).getAllByIngredientId(
                                ingredientId,
                                new GetAllDomainModelsCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            cache.put(entity.getDataId(), entity);

                                        callback.onAllLoaded(entities);
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

    private List<RecipeIngredientEntity> getFromCachedByRecipeId(@Nonnull String recipeId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientEntity> entities = new ArrayList<>();

            for (RecipeIngredientEntity entity : cache.values())
                if (entity.getRecipeId().equals(recipeId))
                    entities.add(entity);

            return entities;
        }
    }

    private List<RecipeIngredientEntity> getFromCacheByProductId(@Nonnull String productId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientEntity> entities = new ArrayList<>();

            for (RecipeIngredientEntity entity : entities)
                if (entity.getProductId().equals(productId))
                    entities.add(entity);

            return entities;
        }
    }

    private List<RecipeIngredientEntity> getFromCacheByIngredientId(@Nonnull String ingredientId) {

        if (cache == null)
            return null;
        else {
            List<RecipeIngredientEntity> entities = new ArrayList<>();
            for (RecipeIngredientEntity entity : entities)
                if (entity.getIngredientId().equals(ingredientId))
                    entities.add(entity);
            return entities;
        }
    }
}
