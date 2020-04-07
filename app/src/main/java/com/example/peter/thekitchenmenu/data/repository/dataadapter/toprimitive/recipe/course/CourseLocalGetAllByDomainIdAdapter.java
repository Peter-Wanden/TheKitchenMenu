package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalGetAllByDomainIdAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;
    private DataAccess.GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback;

    public CourseLocalGetAllByDomainIdAdapter(
            @Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        this.callback = callback;
        getPrimitives(domainId);
    }

    private void getPrimitives(String domainId) {
        courseLocalDataAccess.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
            @Override
            public void onAllLoaded(List<RecipeCourseEntity> entities) {
                callback.onAllLoaded(CourseConverter.convertToModels(entities));
            }

            @Override
            public void onDataUnavailable() {
                callback.onModelsUnavailable();
            }
        });
    }
}
