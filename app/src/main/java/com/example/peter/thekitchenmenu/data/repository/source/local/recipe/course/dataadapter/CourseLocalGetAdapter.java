package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalGetAdapter {

    @Nonnull
    private final RecipeCourseLocalDataSource courseLocalDataSource;
    @Nonnull
    private final CourseModelConverterParent converter;

    public CourseLocalGetAdapter(@Nonnull RecipeCourseLocalDataSource courseLocalDataSource) {
        this.courseLocalDataSource = courseLocalDataSource;
        converter = new CourseModelConverterParent();
    }

    public void getByDataId(
            String dataId,
            GetDomainModelCallback<RecipeCoursePersistenceModel> callback) {
        courseLocalDataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeCourseEntity entity) {
                        callback.onDomainModelLoaded(converter.convertToModel(entity));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {
        courseLocalDataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getAllByCourse(
            @Nonnull Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {
        courseLocalDataSource.getAllByCourseNo(
                c.getCourseNo(),
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getAllActiveByDomainId(
            String domainId,
            GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {
        courseLocalDataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        callback.onAllDomainModelsLoaded(filterForActive(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    private List<RecipeCoursePersistenceModel> filterForActive(List<RecipeCourseEntity> entities) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        for (RecipeCourseEntity e : entities) {
            if (e.isActive()) {
                models.add(converter.convertToModel(e));
            }
        }
        return models;
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {
        courseLocalDataSource.getAll(
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }
}
