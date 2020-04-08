package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;

import javax.annotation.Nonnull;

public class CourseLocalDeleteAdapter {

    @Nonnull
    private final RecipeCourseLocalDataSource courseLocalDataAccess;

    public CourseLocalDeleteAdapter(@Nonnull RecipeCourseLocalDataSource courseLocalDataAccess) {
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
