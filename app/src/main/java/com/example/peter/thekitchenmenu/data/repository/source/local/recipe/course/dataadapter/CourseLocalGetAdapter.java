package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalGetAdapter {

    @Nonnull
    private final RecipeCourseParentLocalDataSource parentLocalDataSource;
    @Nonnull
    private final RecipeCourseItemLocalDataSource itemLocalDataSource;
    @Nonnull
    private final CourseModelConverter converter;

    private RecipeCoursePersistenceModel.Builder modelBuilder;
    private GetDomainModelCallback<RecipeCoursePersistenceModel> courseCallback;
    private GetAllDomainModelsCallback<RecipeCoursePersistenceModel> courseListCallback;

    public CourseLocalGetAdapter(@Nonnull RecipeCourseParentLocalDataSource parentLocalDataSource,
                                 @Nonnull RecipeCourseItemLocalDataSource itemLocalDataSource) {
        this.parentLocalDataSource = parentLocalDataSource;
        this.itemLocalDataSource = itemLocalDataSource;

        converter = new CourseModelConverter();
    }

    public void getByDataId(
            String dataId,
            GetDomainModelCallback<RecipeCoursePersistenceModel> callback) {

        parentLocalDataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeCourseParentEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeCourseParentEntity entity) {
                        courseCallback = callback;
                        addParentEntityToModelBuilder(entity);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                });
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModel> callback) {
        parentLocalDataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeCourseParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseParentEntity> entities) {
                        RecipeCourseParentEntity activeEntity = null;
                        long lastUpdate = 0L;

                        for (RecipeCourseParentEntity e : entities) {
                            if (e.getLastUpdate() > lastUpdate) {
                                lastUpdate = e.getLastUpdate();
                                activeEntity = e;
                            }
                        }

                        if (activeEntity == null) {
                            callback.onDomainModelUnavailable();
                        } else {
                            courseCallback = callback;
                            addParentEntityToModelBuilder(activeEntity);
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }

        );
    }

    public void addParentEntityToModelBuilder(RecipeCourseParentEntity entity) {
        modelBuilder = converter.convertParentEntityToDomainModel(entity);
        addCourseModelItemsToModelBuilder(entity.getDataId());
    }

    private void addCourseModelItemsToModelBuilder(String parentDataId) {
        itemLocalDataSource.getAllByParentDataId(
                parentDataId,
                new GetAllPrimitiveCallback<RecipeCourseItemEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseItemEntity> entities) {
                        if (entities.isEmpty()) {
                            addActiveItems(new ArrayList<>());
                        } else {
                            addActiveItems(
                                    converter.convertCourseItemEntitiesToDomainModels(entities));
                            returnModel();
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        addActiveItems(new ArrayList<>());
                    }

                    private void addActiveItems(List<RecipeCoursePersistenceModelItem> items) {
                        List<RecipeCoursePersistenceModelItem> modelItems = new ArrayList<>();
                        if (!items.isEmpty()) {
                            items.forEach(item -> {
                                if (item.isActive()) {
                                    modelItems.add(item);
                                }
                            });
                        }
                        modelBuilder.setPersistenceModelItems(modelItems);
                        returnModel();
                    }
                });
    }

    private void returnModel() {
        courseCallback.onDomainModelLoaded(modelBuilder.build());
    }

    public void getAllByCourse(
            @Nonnull Course course,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {

        itemLocalDataSource.getAllByCourseNo(
                course.getId(),
                new GetAllPrimitiveCallback<RecipeCourseItemEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseItemEntity> entities) {
                        callback.onAllDomainModelsLoaded(
                                converter.convertCourseItemEntitiesToDomainModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {
        courseListCallback = callback;

        List<RecipeCoursePersistenceModel> modelItems = new ArrayList<>();

        parentLocalDataSource.getAll(
                new GetAllPrimitiveCallback<RecipeCourseParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseParentEntity> entities) {

                        entities.forEach(entity -> {
                            getByDataId(
                                    entity.getDataId(),
                                    new GetDomainModelCallback<RecipeCoursePersistenceModel>() {
                                        @Override
                                        public void onDomainModelLoaded(
                                                RecipeCoursePersistenceModel model) {
                                            modelItems.add(model);
                                        }

                                        @Override
                                        public void onDomainModelUnavailable() {

                                        }
                                    });
                        });
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }
}
