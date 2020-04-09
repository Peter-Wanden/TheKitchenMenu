package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public interface DomainDataAccessRecipeCourse extends DomainDataAccess<RecipeCoursePersistenceModel> {

    void getAllByCourse(
            RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback);

    void getAllByDomainId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback);

    void getAllActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback);

    void update(@Nonnull RecipeCoursePersistenceModel model);
}
