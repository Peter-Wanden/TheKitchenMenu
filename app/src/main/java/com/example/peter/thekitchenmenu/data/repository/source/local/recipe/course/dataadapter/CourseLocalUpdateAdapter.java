package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import javax.annotation.Nonnull;

public class CourseLocalUpdateAdapter {

    @Nonnull
    private final RecipeCourseLocalDataSource courseLocalDataAccess;

    public CourseLocalUpdateAdapter(@Nonnull RecipeCourseLocalDataSource courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void update(RecipeCoursePersistenceModelItem m) {
        CourseModelConverterParent c = new CourseModelConverterParent();
        courseLocalDataAccess.update(c.convertToPrimitive(m));
    }
}