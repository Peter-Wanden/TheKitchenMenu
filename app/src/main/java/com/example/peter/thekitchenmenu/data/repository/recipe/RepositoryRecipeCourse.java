package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends
        Repository<RecipeCoursePersistenceModel>
        implements
        DomainDataAccessRecipeCourse {

    protected static RepositoryRecipeCourse INSTANCE = null;

    private RepositoryRecipeCourse(@Nonnull DomainDataAccessRecipeCourse remoteDataSource,
                                   @Nonnull DomainDataAccessRecipeCourse localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeCourse getInstance(DomainDataAccessRecipeCourse remoteDataSource,
                                                     DomainDataAccessRecipeCourse localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeCourse(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModel> callback) {

        RecipeCoursePersistenceModel model = checkCacheForRecipeId(domainId);

        if (model != null) {
            callback.onDomainModelLoaded(model);
            return;
        }
        ((DomainDataAccessRecipeCourse)localDomainDataAccess).getByDomainId(
                domainId,
                new GetDomainModelCallback<RecipeCoursePersistenceModel>() {
                    @Override
                    public void onDomainModelLoaded(RecipeCoursePersistenceModel model) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        cache.put(model.getDataId(), model);
                        callback.onDomainModelLoaded(model);
                    }

                    @Override
                    public void onDomainModelUnavailable() {
                        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).getByDomainId(
                                domainId,
                                new GetDomainModelCallback<RecipeCoursePersistenceModel>() {
                                    @Override
                                    public void onDomainModelLoaded(
                                            RecipeCoursePersistenceModel model) {
                                        if (model == null) {
                                            onDomainModelUnavailable();
                                            return;
                                        }
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        cache.put(model.getDataId(), model);
                                        callback.onDomainModelLoaded(model);
                                    }

                                    @Override
                                    public void onDomainModelUnavailable() {
                                        callback.onDomainModelUnavailable();
                                    }
                                });
                    }
                });
    }

    private RecipeCoursePersistenceModel checkCacheForRecipeId(String recipeId) {
        RecipeCoursePersistenceModel model = null;
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (RecipeCoursePersistenceModel m : cache.values()) {
                if (m.getDataId().equals(recipeId)) {
                    model = m;
                }
            }
            return model;
        }
    }

    @Override
    public void update(@Nonnull RecipeCoursePersistenceModel model) {
        ((DomainDataAccessRecipeCourse)remoteDomainDataAccess).update(model);
        ((DomainDataAccessRecipeCourse)localDomainDataAccess).update(model);
        cache.put(model.getDataId(), model);
    }
}