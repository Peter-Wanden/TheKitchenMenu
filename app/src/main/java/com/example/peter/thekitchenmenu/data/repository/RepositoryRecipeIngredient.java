package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientEntity>
        implements DataSourceRecipeIngredient {

    public static RepositoryRecipeIngredient INSTANCE = null;

    private RepositoryRecipeIngredient(@Nonnull DataSourceRecipeIngredient remoteDataSource,
                                       @Nonnull DataSourceRecipeIngredient localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeIngredient getInstance(
            DataSourceRecipeIngredient remoteDataSource,
            DataSourceRecipeIngredient localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeIngredient(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> cachedEntities = getFromCachedByRecipeId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DataSourceRecipeIngredient)localDataSource).getByRecipeId(
                recipeId,
                new GetAllCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            entityCache.put(entity.getId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        ((DataSourceRecipeIngredient)remoteDataSource).getByRecipeId(
                                recipeId,
                                new GetAllCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            entityCache.put(entity.getId(), entity);

                                        callback.onAllLoaded(entities);
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        callback.onDataNotAvailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getByProductId(@Nonnull String productId,
                               @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> cachedEntities = getFromCacheByProductId(productId);

        if (cachedEntities == null || !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DataSourceRecipeIngredient)localDataSource).getByProductId(
                productId,
                new GetAllCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            entityCache.put(entity.getId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        ((DataSourceRecipeIngredient)remoteDataSource).getByProductId(
                                productId,
                                new GetAllCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            entityCache.put(entity.getId(), entity);

                                        callback.onAllLoaded(entities);
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        callback.onDataNotAvailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getByIngredientId(@Nonnull String ingredientId,
                                  @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> entities = getFromCacheByIngredientId(ingredientId);
        if (entities != null || !entities.isEmpty()) {
            callback.onAllLoaded(entities);
            return;
        }
        ((DataSourceRecipeIngredient)localDataSource).getByIngredientId(
                ingredientId,
                new GetAllCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            entityCache.put(entity.getId(), entity);
                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        ((DataSourceRecipeIngredient)remoteDataSource).getByIngredientId(
                                ingredientId,
                                new GetAllCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            entityCache.put(entity.getId(), entity);

                                        callback.onAllLoaded(entities);
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        callback.onDataNotAvailable();
                                    }
                                });
                    }
                }
        );
    }

    private List<RecipeIngredientEntity> getFromCachedByRecipeId(@Nonnull String recipeId) {

        if (entityCache == null)
            return null;
        else {
            List<RecipeIngredientEntity> entities = new ArrayList<>();

            for (RecipeIngredientEntity entity : entityCache.values())
                if (entity.getRecipeId().equals(recipeId))
                    entities.add(entity);

            return entities;
        }
    }

    private List<RecipeIngredientEntity> getFromCacheByProductId(@Nonnull String productId) {

        if (entityCache == null)
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

        if (entityCache == null)
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
