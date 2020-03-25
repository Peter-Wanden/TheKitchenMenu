package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;


public class RepositoryRecipeComponentState
        extends Repository<RecipeComponentStateEntity>
        implements DataSourceRecipeComponentState {

    private RepositoryRecipeComponentState(
            @Nonnull DataSourceRecipeComponentState remoteDataSource,
            @Nonnull DataSourceRecipeComponentState localDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeComponentState getInstance(
            @Nonnull DataSourceRecipeComponentState remoteDataSource,
            @Nonnull DataSourceRecipeComponentState localDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeComponentState(remoteDataSource, localDataSource);
        }
        return (RepositoryRecipeComponentState) INSTANCE;
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeComponentStateEntity> callback) {

        List<RecipeComponentStateEntity> entities = checkCacheForRecipeId(recipeId);

        if (entities != null) {
            callback.onAllLoaded(entities);
            return;
        }
        ((RepositoryRecipeComponentState) localDataSource).getAllByRecipeId(
                recipeId,
                new GetAllCallback<RecipeComponentStateEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeComponentStateEntity> entities) {
                        if (entityCache == null) {
                            entityCache = new LinkedHashMap<>();
                        }
                        for (RecipeComponentStateEntity e : entities) {
                            entityCache.put(e.getId(), e);
                        }
                        callback.onAllLoaded(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((RepositoryRecipeComponentState) remoteDataSource).getAllByRecipeId(
                                recipeId,
                                new GetAllCallback<RecipeComponentStateEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeComponentStateEntity> entities) {
                                        if (entities == null) {
                                            onDataUnavailable();
                                            return;
                                        }
                                        if (entityCache == null) {
                                            entityCache = new LinkedHashMap<>();
                                        }
                                        for (RecipeComponentStateEntity e : entities) {
                                            entityCache.put(e.getId(), e);
                                        }
                                        callback.onAllLoaded(entities);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
                                    }
                                }
                        );
                    }
                }
        );
    }

    private List<RecipeComponentStateEntity> checkCacheForRecipeId(String recipeId) {
        List<RecipeComponentStateEntity> entities = new ArrayList<>();
        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (RecipeComponentStateEntity e : entityCache.values()) {
                if (recipeId.equals(e.getRecipeId())) {
                    entities.add(e);
                }
            }
            return entities.isEmpty() ? null : entities;
        }
    }

    @Override
    public void deleteAllByRecipeId(@Nonnull String recipeId) {
        ((RepositoryRecipeComponentState)localDataSource).deleteAllByRecipeId(recipeId);
        ((RepositoryRecipeComponentState)remoteDataSource).deleteAllByRecipeId(recipeId);

        String id = "";

        if (entityCache != null) {
            for (RecipeComponentStateEntity e : entityCache.values()) {
                if (recipeId.equals(e.getRecipeId())) {
                    id = e.getId();
                }
            }
            if (!id.isEmpty()) {
                entityCache.remove(id);
            }
        }
    }
}