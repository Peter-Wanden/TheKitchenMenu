package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceChild;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity;

import javax.annotation.Nonnull;

public interface RecipeCoursePrimitiveDataSource
        extends PrimitiveDataSourceChild<RecipeCourseEntity> {

    void getAllByCourseNo(int courseNo,
                          @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> callback);
}
