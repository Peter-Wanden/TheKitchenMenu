package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
                        callback.onDomainModelLoaded(converter.convertToModel(
                                Collections.singletonList(entity)));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModel> callback) {
        courseLocalDataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        callback.onDomainModelLoaded(converter.convertToModel(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    public void getAllByCourse(
            @Nonnull Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
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
            GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
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

    private List<RecipeCoursePersistenceModelItem> filterForActive(List<RecipeCourseEntity> entities) {
        List<RecipeCoursePersistenceModelItem> models = new ArrayList<>();
        for (RecipeCourseEntity e : entities) {
            if (e.isActive()) {
                models.add(converter.convertToModelItem(e));
            }
        }
        return models;
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {

        HashMap<String, List<RecipeCoursePersistenceModelItem>> modelItems = new LinkedHashMap<>();
        HashMap<String, RecipeCoursePersistenceModel> models = new LinkedHashMap<>();

        courseLocalDataSource.getAll(
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {

                        entities.forEach(entity -> {
                            RecipeCoursePersistenceModelItem item = converter.
                                    convertToModelItem(entity);

                            String domainId = item.getDomainId();
                            boolean isInList = modelItems.containsKey(item.getDomainId());

                            if (isInList) {
                                modelItems.get(domainId).add(item);
                            } else {
                                List<RecipeCoursePersistenceModelItem> newItems = new ArrayList<>();
                                newItems.add(item);
                                modelItems.put(domainId, newItems);
                            }
                        });
                        for (String domainId : modelItems.keySet()) {

                            RecipeCoursePersistenceModel model =
                                    new RecipeCoursePersistenceModel.Builder();
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }
}
