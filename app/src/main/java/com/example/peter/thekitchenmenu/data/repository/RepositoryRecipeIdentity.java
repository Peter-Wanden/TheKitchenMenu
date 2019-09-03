package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;

import java.util.LinkedHashMap;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipeIdentity
        extends Repository<RecipeIdentityEntity>
        implements DataSourceRecipeIdentity {

    public static RepositoryRecipeIdentity INSTANCE = null;
    private DataSourceRecipeIdentity recipeIdentityRemoteDataSource;
    private DataSourceRecipeIdentity recipeIdentityLocalDataSource;

    private RepositoryRecipeIdentity(
            @NonNull DataSourceRecipeIdentity remoteDataSource,
            @NonNull DataSourceRecipeIdentity localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
        recipeIdentityRemoteDataSource = remoteDataSource;
        recipeIdentityLocalDataSource = localDataSource;
    }

    public static RepositoryRecipeIdentity getInstance(
            DataSourceRecipeIdentity remoteDataSource,
            DataSourceRecipeIdentity localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeIdentity(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@NonNull String recipeId,
                              @NonNull GetEntityCallback<RecipeIdentityEntity> callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        RecipeIdentityEntity cachedEntity = checkCacheForRecipeId(recipeId);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        recipeIdentityLocalDataSource.getByRecipeId(
                recipeId,
                new GetEntityCallback<RecipeIdentityEntity>() {
            @Override
            public void onEntityLoaded(RecipeIdentityEntity recipeIdentityEntity) {
                if (entityCache == null)
                    entityCache = new LinkedHashMap<>();

                entityCache.put(recipeIdentityEntity.getId(), recipeIdentityEntity);
                callback.onEntityLoaded(recipeIdentityEntity);
            }

            @Override
            public void onDataNotAvailable() {
                recipeIdentityRemoteDataSource.getByRecipeId(
                        recipeId,
                        new GetEntityCallback<RecipeIdentityEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIdentityEntity recipeIdentityEntity) {
                        if (recipeIdentityEntity != null) {
                            onDataNotAvailable();
                            return;
                        }
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();
                        entityCache.put(recipeIdentityEntity.getId(), recipeIdentityEntity);
                        callback.onEntityLoaded(recipeIdentityEntity);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
                callback.onDataNotAvailable();
            }
        });
    }

    private RecipeIdentityEntity checkCacheForRecipeId(String recipeId) {
        checkNotNull(recipeId);

        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (RecipeIdentityEntity identityEntity : entityCache.values()) {
                if (identityEntity.getRecipeId().equals(recipeId))
                    return identityEntity;
            }
            return null;
        }
    }
}
