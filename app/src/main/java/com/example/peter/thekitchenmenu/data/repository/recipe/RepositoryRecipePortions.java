package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsModelPersistence;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

public class RepositoryRecipePortions
        extends Repository<RecipePortionsModelPersistence>
        implements DomainDataAccessRecipePortions {

    public static RepositoryRecipePortions INSTANCE;

    private RepositoryRecipePortions(@Nonnull DomainDataAccessRecipePortions remoteDataSource,
                                     @Nonnull DomainDataAccessRecipePortions localDataSource) {
        this.remoteDomainDataAccess = remoteDataSource;
        this.localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipePortions getInstance(
            @Nonnull DomainDataAccessRecipePortions remoteDataSource,
            @Nonnull DomainDataAccessRecipePortions localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipePortions(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetDomainModelCallback<RecipePortionsModelPersistence> callback) {

        RecipePortionsModelPersistence model = checkCacheForRecipeId(recipeId);

        if (model != null) {
            callback.onModelLoaded(model);
            return;
        }
        ((DomainDataAccessRecipePortions) localDomainDataAccess).getByRecipeId(
                recipeId,
                new GetDomainModelCallback<RecipePortionsModelPersistence>() {
                    @Override
                    public void onModelLoaded(RecipePortionsModelPersistence model) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();
                        cache.put(model.getDataId(), model);

                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onModelUnavailable() {
                        ((DomainDataAccessRecipePortions) remoteDomainDataAccess).getByRecipeId(
                                recipeId,
                                new GetDomainModelCallback<RecipePortionsModelPersistence>() {
                                    @Override
                                    public void onModelLoaded(RecipePortionsModelPersistence model) {
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

    private RecipePortionsModelPersistence checkCacheForRecipeId(String recipeId) {

        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipePortionsModelPersistence model : cache.values()) {
                if (recipeId.equals(model.getDomainId())) {
                    return model;
                }
            }
            return null;
        }
    }
}
