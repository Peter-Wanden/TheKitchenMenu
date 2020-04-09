package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class CourseLocalSaveAdapter {

    @Nonnull
    private final RecipeCourseLocalDataSource dataSource;

    public CourseLocalSaveAdapter(@Nonnull RecipeCourseLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(RecipeCoursePersistenceModel m) {
        CourseModelConverter c = new CourseModelConverter();
        dataSource.save(c.convertToPrimitive(m));
    }
}
