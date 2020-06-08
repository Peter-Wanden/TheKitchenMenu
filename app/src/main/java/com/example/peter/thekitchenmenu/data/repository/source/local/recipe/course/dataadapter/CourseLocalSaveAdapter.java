package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import javax.annotation.Nonnull;

public class CourseLocalSaveAdapter {

    @Nonnull
    private final RecipeCourseLocalDataSource dataSource;
    @Nonnull
    private final CourseModelConverterParent converter;

    public CourseLocalSaveAdapter(@Nonnull RecipeCourseLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new CourseModelConverterParent();
    }

    public void save(RecipeCoursePersistenceModelItem model) {
        dataSource.save(converter.convertToPrimitive(model));
    }
}
