package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourseRemote
        implements
        DomainDataAccess<RecipeCoursePersistenceModel> {

    private static RepositoryRecipeCourseRemote INSTANCE;

    public static RepositoryRecipeCourseRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeCourseRemote();
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeCoursePersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
