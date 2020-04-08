package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSourceParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;

import javax.annotation.Nonnull;

public interface RecipeCoursePrimitiveDataSource
        extends
        PrimitiveDataSource<RecipeCourseEntity>,
        PrimitiveDataSourceParent<RecipeCourseEntity> {

    void getAllByCourseNo(int courseNo,
                          @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> callback);

    void update(@Nonnull RecipeCourseEntity e);
}
