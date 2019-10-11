package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipeIngredient
        extends Repository<RecipeIngredientQuantityEntity>
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
                              @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        List<RecipeIngredientQuantityEntity> cachedEntities = getFromCachedByRecipeId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        recipeIngredientLocalDataSource.getByRecipeId(
                recipeId,
                new GetAllCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> entities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeIngredientQuantityEntity entity : entities)
                            entityCache.put(entity.getId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        recipeIngredientRemoteDataSource.getByRecipeId(
                                recipeId,
                                new GetAllCallback<RecipeIngredientQuantityEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> entities) {
                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeIngredientQuantityEntity entity : entities)
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
                               @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback) {
        checkNotNull(productId);
        checkNotNull(callback);

        List<RecipeIngredientQuantityEntity> cachedEntities = getFromCacheByProductId(productId);

        if (cachedEntities == null || !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        recipeIngredientLocalDataSource.getByProductId(
                productId,
                new GetAllCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> entities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeIngredientQuantityEntity entity : entities)
                            entityCache.put(entity.getId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        recipeIngredientRemoteDataSource.getByProductId(
                                productId,
                                new GetAllCallback<RecipeIngredientQuantityEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> entities) {
                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeIngredientQuantityEntity entity : entities)
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
                                  @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback) {
        checkNotNull(ingredientId);
        checkNotNull(callback);

        List<RecipeIngredientQuantityEntity> entities = getFromCacheByIngredientId(ingredientId);
        if (entities != null || !entities.isEmpty()) {
            callback.onAllLoaded(entities);
            return;
        }
        recipeIngredientLocalDataSource.getByIngredientId(
                ingredientId,
                new GetAllCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> entities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeIngredientQuantityEntity entity : entities)
                            entityCache.put(entity.getId(), entity);
                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        recipeIngredientRemoteDataSource.getByIngredientId(
                                ingredientId,
                                new GetAllCallback<RecipeIngredientQuantityEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> entities) {
                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeIngredientQuantityEntity entity : entities)
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

    private List<RecipeIngredientQuantityEntity> getFromCachedByRecipeId(String recipeId) {
        checkNotNull(recipeId);

        if (entityCache == null)
            return null;
        else {
            List<RecipeIngredientQuantityEntity> entities = new ArrayList<>();

            for (RecipeIngredientQuantityEntity entity : entityCache.values())
                if (entity.getRecipeId().equals(recipeId))
                    entities.add(entity);

            return entities;
        }
    }

    private List<RecipeIngredientQuantityEntity> getFromCacheByProductId(String productId) {
        checkNotNull(productId);

        if (entityCache == null)
            return null;
        else {
            List<RecipeIngredientQuantityEntity> entities = new ArrayList<>();

            for (RecipeIngredientQuantityEntity entity : entities)
                if (entity.getProductId().equals(productId))
                    entities.add(entity);

            return entities;
        }
    }

    private List<RecipeIngredientQuantityEntity> getFromCacheByIngredientId(String ingredientId) {
        checkNotNull(ingredientId);

        if (entityCache == null)
            return null;
        else {
            List<RecipeIngredientQuantityEntity> entities = new ArrayList<>();
            for (RecipeIngredientQuantityEntity entity : entities)
                if (entity.getIngredientId().equals(ingredientId))
                    entities.add(entity);
            return entities;
        }
    }
}
