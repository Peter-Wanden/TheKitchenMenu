package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipePortions
        extends Repository<RecipePortionsEntity>
        implements DataSourceRecipePortions {

    public static RepositoryRecipePortions INSTANCE;

    private RepositoryRecipePortions(@Nonnull DataSourceRecipePortions remoteDataSource,
                                     @Nonnull DataSourceRecipePortions localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipePortions getInstance(DataSourceRecipePortions remoteDataSource,
                                                       DataSourceRecipePortions localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipePortions(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getPortionsForRecipe(@Nonnull String recipeId,
                                     @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {

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
                    public void onDataUnavailable() {
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
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
                                    }
                                }
                        );
                    }
                });
    }

    private RecipePortionsEntity checkCacheForId(String recipeId) {

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
