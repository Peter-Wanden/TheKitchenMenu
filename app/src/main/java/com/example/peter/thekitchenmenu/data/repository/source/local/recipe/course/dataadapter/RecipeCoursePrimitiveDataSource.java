package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceChild;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemEntity;

import javax.annotation.Nonnull;

public interface RecipeCoursePrimitiveDataSource
        extends PrimitiveDataSourceChild<RecipeCourseItemEntity> {

    void getAllByCourseNo(int courseNo,
                          @Nonnull GetAllPrimitiveCallback<RecipeCourseItemEntity> callback);

    void update(@Nonnull RecipeCourseItemEntity e);
}
