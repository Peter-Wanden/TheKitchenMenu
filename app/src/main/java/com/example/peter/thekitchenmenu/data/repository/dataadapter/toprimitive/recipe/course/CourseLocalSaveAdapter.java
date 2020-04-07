package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import javax.annotation.Nonnull;

public class CourseLocalSaveAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;

    public CourseLocalSaveAdapter(@Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void save(RecipeCourseModelPersistence m) {
        CourseConverter c = new CourseConverter();
        courseLocalDataAccess.save(c.convertToPrimitive(m));
    }
}
