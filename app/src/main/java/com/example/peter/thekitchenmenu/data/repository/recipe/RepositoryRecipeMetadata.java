package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

public class RepositoryRecipeMetadata
        extends Repository<RecipeMetadataPersistenceModel>
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

    // todo - this should generate a list of previously changed data. Only return the one with the
    //  latest update
    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetModelCallback<RecipeMetadataPersistenceModel> callback) {

        RecipeMetadataPersistenceModel cachedModel = checkCacheForRecipeId(recipeId);

        if (cachedModel != null) {
            callback.onModelLoaded(cachedModel);
            return;
        }
        ((DataSourceRecipeMetaData)localDataSource).getByRecipeId(
                recipeId,
                new GetModelCallback<RecipeMetadataPersistenceModel>() {
                    @Override
                    public void onModelLoaded(RecipeMetadataPersistenceModel model) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        cache.put(model.getDataId(), model);
                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onModelUnavailable() {
                        ((DataSourceRecipeMetaData)remoteDataSource).getByRecipeId(
                                recipeId, new GetModelCallback<RecipeMetadataPersistenceModel>() {
                                    @Override
                                    public void onModelLoaded(RecipeMetadataPersistenceModel model) {
                                        if (cache == null) cache = new LinkedHashMap<>();
                                        cache.put(model.getDataId(), model);

                                        callback.onModelLoaded(model);
                                    }

                                    @Override
                                    public void onModelUnavailable() {
                                        callback.onModelUnavailable();
                                    }
                                });

                    }
                });
    }

    private RecipeMetadataPersistenceModel checkCacheForRecipeId(String recipeId) {
        RecipeMetadataPersistenceModel model = null;
        if (cache == null || cache.isEmpty())
            return model;
        else {
            for (RecipeMetadataPersistenceModel m : cache.values()) {
                if (recipeId.equals(model.getRecipeId())) {
                    model = m;
                }
            }
            return model;
        }
    }

    @Override
    public void deleteByRecipeId(@Nonnull String recipeId) {
        ((RepositoryRecipeMetadata)localDataSource).deleteByRecipeId(recipeId);
        ((RepositoryRecipeMetadata)remoteDataSource).deleteByRecipeId(recipeId);

        String id = "";

        if (cache != null) {
            for (RecipeMetadataPersistenceModel m : cache.values()) {
                if (recipeId.equals(m.getRecipeId())) {
                    id = m.getDataId();
                }
            }
            if (!id.isEmpty()) {
                cache.remove(id);
            }
        }
    }
}
