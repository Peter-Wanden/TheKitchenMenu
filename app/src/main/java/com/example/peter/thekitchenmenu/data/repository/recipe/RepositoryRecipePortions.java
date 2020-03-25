package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

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
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {

        RecipePortionsEntity cachedEntity = checkCacheForRecipeId(recipeId);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        ((DataSourceRecipePortions)localDataSource).getByRecipeId(
                recipeId,
                new GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity entity) {
                        if (entityCache == null) entityCache = new LinkedHashMap<>();

                        entityCache.put(entity.getId(), entity);
                        callback.onEntityLoaded(entity);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((DataSourceRecipePortions)remoteDataSource).getByRecipeId(
                                recipeId,
                                new GetEntityCallback<RecipePortionsEntity>() {
                                    @Override
                                    public void onEntityLoaded(RecipePortionsEntity entity) {
                                        if (entityCache == null) entityCache = new LinkedHashMap<>();
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

    private RecipePortionsEntity checkCacheForRecipeId(String recipeId) {

        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (RecipePortionsEntity entity : entityCache.values()) {
                if (recipeId.equals(entity.getRecipeId())) {
                    return entity;
                }
            }
            return null;
        }
    }
}
