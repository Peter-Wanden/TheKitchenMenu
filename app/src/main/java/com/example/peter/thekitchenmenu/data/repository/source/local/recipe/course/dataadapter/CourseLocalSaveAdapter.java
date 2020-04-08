package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import javax.annotation.Nonnull;

public class CourseLocalSaveAdapter {

    @Nonnull
    private final RecipeCourseLocalDataSource courseLocalDataAccess;

    public CourseLocalSaveAdapter(@Nonnull RecipeCourseLocalDataSource courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void save(RecipeCourseModelPersistence m) {
        CourseConverter c = new CourseConverter();
        courseLocalDataAccess.save(c.convertToPrimitive(m));
    }
}
