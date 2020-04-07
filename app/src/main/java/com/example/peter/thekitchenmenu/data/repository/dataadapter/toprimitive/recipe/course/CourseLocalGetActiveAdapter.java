package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalGetActiveAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;
    private DataAccess.GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback;

    public CourseLocalGetActiveAdapter(@Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void getActiveByDomainId(
            String domainId,
            GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        courseLocalDataAccess.getAllByDomainId(
                domainId,
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeCourseEntity>() {
            @Override
            public void onAllLoaded(List<RecipeCourseEntity> entities) {
                callback.onModelLoaded(CourseConverter.convertActiveModels(entities));
            }

            @Override
            public void onDataUnavailable() {
                callback.onModelsUnavailable();
            }
        });
    }
}
