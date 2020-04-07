package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import javax.annotation.Nonnull;

public class CourseLocalUpdateAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;

    public CourseLocalUpdateAdapter(@Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void update(RecipeCourseModelPersistence m) {
        courseLocalDataAccess.update(CourseConverter.convertToPrimitive(m));
    }
}