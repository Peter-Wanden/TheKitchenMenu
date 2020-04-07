package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import javax.annotation.Nonnull;

public interface DataAccessRecipeCourse extends DataAccess<RecipeCourseModelPersistence> {

    void getAllByCourse(
            RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback);

    void getAllByDomainId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback);

    void getAllActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback);

    void update(@Nonnull RecipeCourseModelPersistence model);
}
