package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsPersistenceModel;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

public class RepositoryRecipePortions
        extends Repository<RecipePortionsPersistenceModel>
        implements DataSourceRecipePortions {

    public static RepositoryRecipePortions INSTANCE;

    private RepositoryRecipePortions(@Nonnull DataSourceRecipePortions remoteDataSource,
                                     @Nonnull DataSourceRecipePortions localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipePortions getInstance(
            @Nonnull DataSourceRecipePortions remoteDataSource,
            @Nonnull DataSourceRecipePortions localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipePortions(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {

        RecipePortionsPersistenceModel model = checkCacheForRecipeId(recipeId);

        if (model != null) {
            callback.onModelLoaded(model);
            return;
        }
        ((DataSourceRecipePortions) localDataSource).getByRecipeId(
                recipeId,
                new GetDomainModelCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onModelLoaded(RecipePortionsPersistenceModel model) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();
                        cache.put(model.getDataId(), model);

                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onModelUnavailable() {
                        ((DataSourceRecipePortions) remoteDataSource).getByRecipeId(
                                recipeId,
                                new GetDomainModelCallback<RecipePortionsPersistenceModel>() {
                                    @Override
                                    public void onModelLoaded(RecipePortionsPersistenceModel model) {
                                        if (cache == null)
                                            cache = new LinkedHashMap<>();
                                        cache.put(model.getDataId(), model);

                                        callback.onModelLoaded(model);
                                    }

                                    @Override
                                    public void onModelUnavailable() {
                                        callback.onModelUnavailable();
                                    }
                                }
                        );
                    }
                });
    }

    private RecipePortionsPersistenceModel checkCacheForRecipeId(String recipeId) {

        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipePortionsPersistenceModel model : cache.values()) {
                if (recipeId.equals(model.getRecipeId())) {
                    return model;
                }
            }
            return null;
        }
    }
}
