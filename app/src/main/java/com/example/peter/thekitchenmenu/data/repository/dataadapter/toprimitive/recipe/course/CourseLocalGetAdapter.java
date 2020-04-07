package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.DataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalGetAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;

    public CourseLocalGetAdapter(@Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void getAllByCourse(
            @Nonnull Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        courseLocalDataAccess.getAllByCourseNo(
                c.getCourseNo(),
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        CourseConverter c = new CourseConverter();
                        callback.onAllLoaded(c.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    public void getAllActiveByDomainId(
            String domainId,
            GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        courseLocalDataAccess.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        CourseConverter c = new CourseConverter();
                        callback.onAllLoaded(c.convertActiveModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        courseLocalDataAccess.getAll(
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        CourseConverter c = new CourseConverter();
                        callback.onAllLoaded(c.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        courseLocalDataAccess.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        CourseConverter c = new CourseConverter();
                        callback.onAllLoaded(c.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    public void getByDataId(
            String dataId,
            GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        courseLocalDataAccess.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeCourseEntity e) {
                        CourseConverter c = new CourseConverter();
                        callback.onModelLoaded(c.convertToModel(e));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }
}
