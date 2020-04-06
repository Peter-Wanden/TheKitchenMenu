package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

public class RepositoryRecipePortions
        extends Repository<RecipePortionsPersistenceModel>
        implements DataAccessRecipePortions {

    public static RepositoryRecipePortions INSTANCE;

    private RepositoryRecipePortions(@Nonnull DataAccessRecipePortions remoteDataSource,
                                     @Nonnull DataAccessRecipePortions localDataSource) {
        this.remoteDataAccess = remoteDataSource;
        this.localDataAccess = localDataSource;
    }

    public static RepositoryRecipePortions getInstance(
            @Nonnull DataAccessRecipePortions remoteDataSource,
            @Nonnull DataAccessRecipePortions localDataSource) {
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
        ((DataAccessRecipePortions) localDataAccess).getByRecipeId(
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
                        ((DataAccessRecipePortions) remoteDataAccess).getByRecipeId(
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
                if (recipeId.equals(model.getDomainId())) {
                    return model;
                }
            }
            return null;
        }
    }
}
