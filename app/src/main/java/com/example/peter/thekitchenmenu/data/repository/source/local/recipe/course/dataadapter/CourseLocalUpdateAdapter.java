package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class CourseLocalUpdateAdapter {

    @Nonnull
    private final RecipeCourseLocalDataSource courseLocalDataAccess;

    public CourseLocalUpdateAdapter(@Nonnull RecipeCourseLocalDataSource courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void update(RecipeCoursePersistenceModel m) {
        CourseConverter c = new CourseConverter();
        courseLocalDataAccess.update(c.convertToPrimitive(m));
    }
}