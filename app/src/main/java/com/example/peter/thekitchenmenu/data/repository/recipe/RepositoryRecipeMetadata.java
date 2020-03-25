package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

public class RepositoryRecipeMetadata
        extends Repository<RecipeMetadataEntity>
        implements DataSourceRecipeMetaData {

    private RepositoryRecipeMetadata(
            @Nonnull DataSourceRecipeMetaData remoteDataSource,
            @Nonnull DataSourceRecipeMetaData localDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeMetadata getInstance(
            @Nonnull DataSourceRecipeMetaData remoteDataSource,
            @Nonnull DataSourceRecipeMetaData localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeMetadata(remoteDataSource, localDataSource);
        return (RepositoryRecipeMetadata) INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipeMetadataEntity> callback) {

        RecipeMetadataEntity cachedEntity = checkCacheForRecipeId(recipeId);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        ((DataSourceRecipeMetaData)localDataSource).getByRecipeId(
                recipeId,
                new GetEntityCallback<RecipeMetadataEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeMetadataEntity e) {
                        if (entityCache == null) entityCache = new LinkedHashMap<>();

                        entityCache.put(e.getId(), e);
                        callback.onEntityLoaded(e);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((DataSourceRecipeMetaData)remoteDataSource).getByRecipeId(
                                recipeId, new GetEntityCallback<RecipeMetadataEntity>() {
                                    @Override
                                    public void onEntityLoaded(RecipeMetadataEntity e) {
                                        if (entityCache == null) entityCache = new LinkedHashMap<>();
                                        entityCache.put(e.getId(), e);

                                        callback.onEntityLoaded(e);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
                                    }
                                });

                    }
                });
    }

    private RecipeMetadataEntity checkCacheForRecipeId(String recipeId) {
        RecipeMetadataEntity e = null;
        if (entityCache == null || entityCache.isEmpty())
            return e;
        else {
            for (RecipeMetadataEntity entity : entityCache.values()) {
                if (recipeId.equals(e.getRecipeId())) {
                    e = entity;
                }
            }
            return e;
        }
    }

    @Override
    public void deleteByRecipeId(@Nonnull String recipeId) {
        ((RepositoryRecipeMetadata)localDataSource).deleteByRecipeId(recipeId);
        ((RepositoryRecipeMetadata)remoteDataSource).deleteByRecipeId(recipeId);

        String id = "";

        if (entityCache != null) {
            for (RecipeMetadataEntity e : entityCache.values()) {
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
