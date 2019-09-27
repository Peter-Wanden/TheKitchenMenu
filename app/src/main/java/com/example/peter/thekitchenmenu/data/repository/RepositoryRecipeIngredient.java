package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientEntity>
        implements DataSourceRecipeIngredient {

    public static RepositoryRecipeIngredient INSTANCE = null;
    private DataSourceRecipeIngredient recipeIngredientRemoteDataSource;
    private DataSourceRecipeIngredient recipeIngredientLocalDataSource;

    private RepositoryRecipeIngredient(@NonNull DataSourceRecipeIngredient remoteDataSource,
                                       @NonNull DataSourceRecipeIngredient localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
        recipeIngredientRemoteDataSource = checkNotNull(remoteDataSource);
        recipeIngredientLocalDataSource = checkNotNull(localDataSource);
    }

    public static RepositoryRecipeIngredient getInstance(
            DataSourceRecipeIngredient remoteDataSource,
            DataSourceRecipeIngredient localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeIngredient(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@NonNull String recipeId,
                              @NonNull GetAllCallback<RecipeIngredientEntity> callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        List<RecipeIngredientEntity> cachedEntities = getFromCachedByRecipeId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        recipeIngredientLocalDataSource.getByRecipeId(
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
                        recipeIngredientRemoteDataSource.getByRecipeId(
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
    public void getByProductId(@NonNull String productId,
                               @NonNull GetAllCallback<RecipeIngredientEntity> callback) {
        checkNotNull(productId);
        checkNotNull(callback);

        List<RecipeIngredientEntity> cachedEntities = getFromCacheByProductId(productId);

        if (cachedEntities == null || !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        recipeIngredientLocalDataSource.getByProductId(
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
                        recipeIngredientRemoteDataSource.getByProductId(
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
    public void getByIngredientId(@NonNull String ingredientId,
                                  @NonNull GetAllCallback<RecipeIngredientEntity> callback) {
        checkNotNull(ingredientId);
        checkNotNull(callback);

        List<RecipeIngredientEntity> entities = getFromCacheByIngredientId(ingredientId);
        if (entities != null || !entities.isEmpty()) {
            callback.onAllLoaded(entities);
            return;
        }
        recipeIngredientLocalDataSource.getByIngredientId(
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
                        recipeIngredientRemoteDataSource.getByIngredientId(
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

    private List<RecipeIngredientEntity> getFromCachedByRecipeId(String recipeId) {
        checkNotNull(recipeId);

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

    private List<RecipeIngredientEntity> getFromCacheByProductId(String productId) {
        checkNotNull(productId);

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

    private List<RecipeIngredientEntity> getFromCacheByIngredientId(String ingredientId) {
        checkNotNull(ingredientId);

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
