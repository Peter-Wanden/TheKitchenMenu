package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeRepository implements RecipeDataSource {

    private static RecipeRepository INSTANCE = null;
    private final RecipeDataSource remoteDataSource;
    private final RecipeDataSource localDataSource;
    Map<String, RecipeEntity> recipesCache;
    boolean cacheIsDirty;

    private RecipeRepository(@NonNull RecipeDataSource remoteDataSource,
                             @NonNull RecipeDataSource localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static RecipeRepository getInstance(RecipeDataSource remoteDataSource,
                                               RecipeDataSource localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RecipeRepository(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getRecipes(@NonNull LoadRecipesCallback callback) {
        checkNotNull(callback);
        if (recipesCache != null) {
            callback.onRecipesLoaded(new ArrayList<>(recipesCache.values()));
            return;
        }
        if (cacheIsDirty)
            getRecipesFromRemoteDataSource(callback);
        else {
            localDataSource.getRecipes(new LoadRecipesCallback() {
                @Override
                public void onRecipesLoaded(List<RecipeEntity> recipeEntities) {
                    refreshRecipesCache(recipeEntities);
                    callback.onRecipesLoaded(new ArrayList<>(recipesCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getRecipesFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getRecipesFromRemoteDataSource(@NonNull final LoadRecipesCallback callback) {
        remoteDataSource.getRecipes(new LoadRecipesCallback() {
            @Override
            public void onRecipesLoaded(List<RecipeEntity> recipeEntities) {
                refreshRecipesCache(recipeEntities);
                refreshLocalDataSource(recipeEntities);
                callback.onRecipesLoaded(new ArrayList<>(recipesCache.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshRecipesCache(List<RecipeEntity> recipeEntityList) {
        if (recipesCache == null)
            recipesCache = new LinkedHashMap<>();
        recipesCache.clear();

        for (RecipeEntity recipeEntity : recipeEntityList)
            recipesCache.put(recipeEntity.getId(), recipeEntity);
        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<RecipeEntity> recipeEntities) {
        localDataSource.deleteAll();
        for (RecipeEntity recipeEntity : recipeEntities)
            localDataSource.saveRecipe(recipeEntity);
    }

    @Override
    public void getRecipe(@NonNull String recipeId, @NonNull GetRecipeCallback callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        RecipeEntity cachedRecipeEntity = getRecipeWithId(recipeId);
        if (cachedRecipeEntity != null) {
            callback.onRecipeLoaded(cachedRecipeEntity);
            return;
        }

        localDataSource.getRecipe(recipeId, new GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(RecipeEntity recipeEntity) {
                if (recipesCache == null)
                    recipesCache = new LinkedHashMap<>();
                recipesCache.put(recipeEntity.getId(), recipeEntity);
                callback.onRecipeLoaded(recipeEntity);
            }

            @Override
            public void onDataNotAvailable() {
                remoteDataSource.getRecipe(recipeId, new GetRecipeCallback() {
                    @Override
                    public void onRecipeLoaded(RecipeEntity recipeEntity) {
                        if (recipesCache == null)
                            recipesCache = new LinkedHashMap<>();
                        recipesCache.put(recipeEntity.getId(), recipeEntity);
                        callback.onRecipeLoaded(recipeEntity);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });

            }
        });
    }

    @Nullable
    private RecipeEntity getRecipeWithId(@NonNull String id) {
        checkNotNull(id);
        if (recipesCache == null || recipesCache.isEmpty())
            return null;
        else
            return recipesCache.get(id);
    }

    @Override
    public void saveRecipe(@NonNull RecipeEntity recipeEntity) {
        remoteDataSource.saveRecipe(recipeEntity);
        localDataSource.saveRecipe(recipeEntity);

        if (recipesCache == null)
            recipesCache = new LinkedHashMap<>();
        recipesCache.put(recipeEntity.getId(), recipeEntity);
    }

    @Override
    public void refreshRecipes() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAll() {
        remoteDataSource.deleteAll();
        localDataSource.deleteAll();

        if (recipesCache == null)
            recipesCache = new LinkedHashMap<>();
        recipesCache.clear();
    }

    @Override
    public void deleteById(@NonNull String id) {
        remoteDataSource.deleteById(id);
        localDataSource.deleteById(id);
        recipesCache.remove(id);
    }
}