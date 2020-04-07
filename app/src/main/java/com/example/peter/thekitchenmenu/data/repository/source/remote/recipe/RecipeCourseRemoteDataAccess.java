package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import javax.annotation.Nonnull;

public class RecipeCourseRemoteDataAccess implements DataAccessRecipeCourse {

    private static RecipeCourseRemoteDataAccess INSTANCE;

    public static RecipeCourseRemoteDataAccess getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeCourseRemoteDataAccess();
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAllByCourseNo(
            int courseNo,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeCourseModelPersistence model) {

    }

    @Override
    public void update(@Nonnull RecipeCourseModelPersistence model) {

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
