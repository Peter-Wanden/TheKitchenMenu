package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceDomainModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalGetAdapter {

    @Nonnull
    private final RecipeCourseParentLocalDataSource parentLocalDataSource;
    @Nonnull
    private final RecipeCourseItemLocalDataSource courseItemLocalDataSource;
    @Nonnull
    private final CourseModelConverter converter;

    private String parentDataId;
    private int noOfParentsAdded;
    private int noOfCourseListsAdded;
    private boolean courseItemsComplete;

    private boolean isListCallback;
    private GetDomainModelCallback<RecipeCoursePersistenceDomainModel> courseCallback;
    private GetAllDomainModelsCallback<RecipeCoursePersistenceDomainModel> courseListCallback;

    private HashMap<String, RecipeCoursePersistenceDomainModel.Builder> modelBuilders;

    public CourseLocalGetAdapter(@Nonnull RecipeCourseParentLocalDataSource parentLocalDataSource,
                                 @Nonnull RecipeCourseItemLocalDataSource courseItemLocalDataSource) {
        this.parentLocalDataSource = parentLocalDataSource;
        this.courseItemLocalDataSource = courseItemLocalDataSource;

        converter = new CourseModelConverter(new UniqueIdProvider());
        modelBuilders = new LinkedHashMap<>();
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceDomainModel> callback) {
        courseCallback = callback;
        isListCallback = false;

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
                            callback.onPersistenceModelUnavailable();
                        } else {
                            parentDataId = activeEntity.getDataId();

                            addParentEntityToModelBuilder(activeEntity);
                            getChildren();
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }

        );
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceDomainModel> callback) {
        courseListCallback = callback;
        isListCallback = true;

        parentLocalDataSource.getAll(
                new GetAllPrimitiveCallback<RecipeCourseParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseParentEntity> parentEntities) {
                        noOfParentsAdded = parentEntities.size();

                        if (noOfParentsAdded == 0) {
                            callback.onDomainModelsUnavailable();
                        } else {
                            parentEntities.forEach(parentEntity ->
                                    addParentEntityToModelBuilder(parentEntity)
                            );
                            getChildren();
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getByDataId(
            String dataId,
            GetDomainModelCallback<RecipeCoursePersistenceDomainModel> callback) {
        courseCallback = callback;
        isListCallback = false;

        noOfParentsAdded = 1;
        parentLocalDataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeCourseParentEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeCourseParentEntity entity) {
                        addParentEntityToModelBuilder(entity);
                        getChildren();
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                });
    }

    public void addParentEntityToModelBuilder(RecipeCourseParentEntity parentEntity) {
        parentDataId = parentEntity.getDataId();

        if (!modelBuilders.containsKey(parentDataId)) {
            addNewModelBuilder(parentDataId);
        }

        modelBuilders.get(parentDataId).
                setDomainId(parentEntity.getDomainId()).
                setCreateDate(parentEntity.getCreateDate()).
                setLastUpdate(parentEntity.getLastUpdate());

    }

    private void addNewModelBuilder(String dataId) {
        RecipeCoursePersistenceDomainModel.Builder builder = new RecipeCoursePersistenceDomainModel.Builder();
        builder.setDataId(dataId);
        modelBuilders.put(dataId, builder);
    }

    private void getChildren() {
        noOfCourseListsAdded = 0;
        courseItemsComplete = false;

        for (String parentDataId : modelBuilders.keySet()) {
            getCourseListItemsForParent(parentDataId);
        }
    }

    private void getCourseListItemsForParent(String parentDataId) {
        courseItemLocalDataSource.getAllByParentDataId(
                parentDataId,
                new GetAllPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> entities) {
                        addCoursesToBuilder(
                                parentDataId,
                                converter.convertCourseEntitiesToDomainModels(entities)
                        );
                    }

                    @Override
                    public void onDataUnavailable() {
                        addCoursesToBuilder(parentDataId, new ArrayList<>());
                    }
                }
        );
    }

    private void addCoursesToBuilder(String parentDataId, List<RecipeCourse.Course> courses) {
        modelBuilders.get(parentDataId).setCourses(courses);

        noOfCourseListsAdded++;
        courseItemsComplete = noOfParentsAdded == noOfCourseListsAdded;

        if (courseItemsComplete) {
            returnResult();
        }
    }

    private void returnResult() {
        if (isListCallback) {

            final List<RecipeCoursePersistenceDomainModel> domainModels = new ArrayList<>();

            Iterator<RecipeCoursePersistenceDomainModel.Builder> iterator = modelBuilders.values().
                    iterator();

            iterator.forEachRemaining(builder -> {
                domainModels.add(builder.build());
                iterator.remove();
            });

            courseListCallback.onAllDomainModelsLoaded(domainModels);

        } else {
            RecipeCoursePersistenceDomainModel domainModel = modelBuilders.get(parentDataId).build();
            modelBuilders.remove(parentDataId);
            courseCallback.onPersistenceModelLoaded(domainModel);
        }
    }
}
