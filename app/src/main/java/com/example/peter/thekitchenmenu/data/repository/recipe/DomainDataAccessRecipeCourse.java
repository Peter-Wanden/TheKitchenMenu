package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import javax.annotation.Nonnull;

public interface DomainDataAccessRecipeCourse
        extends
        DomainDataAccess<RecipeCoursePersistenceModel> {

    void getByDomainId(
            @Nonnull String recipeId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModel> callback);

    void update(@Nonnull RecipeCoursePersistenceModel model);
}
