package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import javax.annotation.Nonnull;

public class CourseLocalUpdateAdapter {

    @Nonnull
    private final RecipeCourseItemLocalDataSource courseLocalDataAccess;

    public CourseLocalUpdateAdapter(@Nonnull RecipeCourseItemLocalDataSource courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void update(RecipeCoursePersistenceModelItem m) {
        CourseModelConverter c = new CourseModelConverter();
        courseLocalDataAccess.update(c.convertToPrimitive(m));
    }
}