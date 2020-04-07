package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalGetAllByCourseAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;
    private GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback;

    public CourseLocalGetAllByCourseAdapter(
            @Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void getAllByCourse(
            @Nonnull RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        this.callback = callback;
        getPrimitives(c);
    }

    private void getPrimitives(Course c) {
        courseLocalDataAccess.getAllByCourseNo(
                c.getCourseNo(),
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        callback.onAllLoaded(CourseConverter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }
}
