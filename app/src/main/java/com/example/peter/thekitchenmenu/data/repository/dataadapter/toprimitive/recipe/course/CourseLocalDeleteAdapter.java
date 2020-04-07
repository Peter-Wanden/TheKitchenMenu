package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;

import javax.annotation.Nonnull;

public class CourseLocalDeleteAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;

    public CourseLocalDeleteAdapter(@Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void deleteByDataId(@Nonnull String dataId) {
        courseLocalDataAccess.deleteByDataId(dataId);
    }

    public void deleteAllByDomainId(@Nonnull String domainId) {
        courseLocalDataAccess.deleteAllByDomainId(domainId);
    }

    public void deleteAll() {
        courseLocalDataAccess.deleteAll();
    }
}
