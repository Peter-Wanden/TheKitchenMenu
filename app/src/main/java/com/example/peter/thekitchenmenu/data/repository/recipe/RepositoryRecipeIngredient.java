package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

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
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> cachedEntities = getFromCachedByRecipeId(recipeId);

        if (cachedEntities != null && !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DataSourceRecipeIngredient)localDataSource).getAllByRecipeId(
                recipeId,
                new GetAllCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            cache.put(entity.getId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((DataSourceRecipeIngredient)remoteDataSource).getAllByRecipeId(
                                recipeId,
                                new GetAllCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            cache.put(entity.getId(), entity);

                                        callback.onAllLoaded(entities);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getAllByProductId(@Nonnull String productId,
                                  @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> cachedEntities = getFromCacheByProductId(productId);

        if (cachedEntities == null || !cachedEntities.isEmpty()) {
            callback.onAllLoaded(cachedEntities);
            return;
        }
        ((DataSourceRecipeIngredient)localDataSource).getAllByProductId(
                productId,
                new GetAllCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            cache.put(entity.getId(), entity);

                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((DataSourceRecipeIngredient)remoteDataSource).getAllByProductId(
                                productId,
                                new GetAllCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            cache.put(entity.getId(), entity);

                                        callback.onAllLoaded(entities);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
                                    }
                                });
                    }
                });
    }

    @Override
    public void getAllByIngredientId(@Nonnull String ingredientId,
                                     @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {

        List<RecipeIngredientEntity> entities = getFromCacheByIngredientId(ingredientId);
        if (entities != null || !entities.isEmpty()) {
            callback.onAllLoaded(entities);
            return;
        }
        ((DataSourceRecipeIngredient)localDataSource).getAllByIngredientId(
                ingredientId,
                new GetAllCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeIngredientEntity entity : entities)
                            cache.put(entity.getId(), entity);
                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((DataSourceRecipeIngredient)remoteDataSource).getAllByIngredientId(
                                ingredientId,
                                new GetAllCallback<RecipeIngredientEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeIngredientEntity entity : entities)
                                            cache.put(entity.getId(), entity);

                                        callback.onAllLoaded(entities);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
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
