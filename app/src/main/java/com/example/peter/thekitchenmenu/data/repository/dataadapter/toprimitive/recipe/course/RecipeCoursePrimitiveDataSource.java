package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;

import javax.annotation.Nonnull;

public interface RecipeCoursePrimitiveDataSource extends PrimitiveDataSource<RecipeCourseEntity> {

    void getAllByCourseNo(int courseNo,
                          @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> callback);

    void getAllByDomainId(String domainId,
                          @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> callback);

    void update(@Nonnull RecipeCourseEntity e);
}
