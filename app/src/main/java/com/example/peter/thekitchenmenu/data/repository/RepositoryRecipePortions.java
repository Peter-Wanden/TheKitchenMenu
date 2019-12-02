package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;

import java.util.LinkedHashMap;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipePortions
        extends Repository<RecipePortionsEntity>
        implements DataSourceRecipePortions {

    public static RepositoryRecipePortions INSTANCE;

    private RepositoryRecipePortions(@NonNull DataSourceRecipePortions remoteDataSource,
                                     @NonNull DataSourceRecipePortions localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static RepositoryRecipePortions getInstance(DataSourceRecipePortions remoteDataSource,
                                                       DataSourceRecipePortions localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipePortions(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getPortionsForRecipe(@NonNull String recipeId,
                                     @NonNull GetEntityCallback<RecipePortionsEntity> callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        RecipePortionsEntity cachedEntity = checkCacheForId(recipeId);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        ((DataSourceRecipePortions)localDataSource).getPortionsForRecipe(
                recipeId,
                new GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity entity) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        entityCache.put(entity.getId(), entity);
                        callback.onEntityLoaded(entity);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        ((DataSourceRecipePortions)remoteDataSource).getPortionsForRecipe(
                                recipeId,
                                new GetEntityCallback<RecipePortionsEntity>() {
                                    @Override
                                    public void onEntityLoaded(RecipePortionsEntity entity) {
                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        entityCache.put(entity.getId(), entity);
                                        callback.onEntityLoaded(entity);
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        callback.onDataNotAvailable();
                                    }
                                }
                        );
                    }
                });
    }

    private RecipePortionsEntity checkCacheForId(String recipeId) {
        checkNotNull(recipeId);

        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (RecipePortionsEntity entity : entityCache.values()) {
                if (entity.getRecipeId().equals(recipeId))
                    return entity;
            }
            return null;
        }
    }
}
