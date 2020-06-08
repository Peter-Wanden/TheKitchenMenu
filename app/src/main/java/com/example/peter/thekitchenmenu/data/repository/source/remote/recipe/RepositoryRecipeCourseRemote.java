package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourseRemote
        implements DomainDataAccessRecipeCourse {

    private static RepositoryRecipeCourseRemote INSTANCE;

    public static RepositoryRecipeCourseRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeCourseRemote();
        return INSTANCE;
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModelItem> callback) {
        callback.onDomainModelUnavailable();
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getAllByCourse(
            @Nonnull RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModelItem> callback) {
        callback.onDomainModelUnavailable();
    }

    @Override
    public void getAllActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        callback.onDomainModelsUnavailable();

    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeCoursePersistenceModelItem model) {

    }

    @Override
    public void update(@Nonnull RecipeCoursePersistenceModelItem model) {

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
