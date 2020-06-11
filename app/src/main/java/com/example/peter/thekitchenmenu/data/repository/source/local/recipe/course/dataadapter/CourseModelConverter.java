package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseModelConverter {

    public RecipeCoursePersistenceModel.Builder convertParentEntityToDomainModel(
            @Nonnull RecipeCourseParentEntity entity) {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate());
    }

    public List<RecipeCoursePersistenceModel.Builder> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeCourseParentEntity> entities) {

        List<RecipeCoursePersistenceModel.Builder> models = new ArrayList<>();
        entities.forEach(entity -> models.add(convertParentEntityToDomainModel(entity)));
        return models;
    }

    public RecipeCourseParentEntity convertParentDomainModelToEntity(
            @Nonnull RecipeCoursePersistenceModel model) {
        return new RecipeCourseParentEntity.Builder().
                getDefault().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setCreateDate(model.getCreateDate()).
                setLastUpdate(model.getLastUpdate()).
                build();
    }

    public RecipeCourseItemEntity[] convertDomainCourseItemsToEntities(
            @Nonnull List<RecipeCoursePersistenceModelItem> items,
            @Nonnull String parentDataId) {

        RecipeCourseItemEntity[] itemArray = new RecipeCourseItemEntity[items.size()];

        int i=0;
        for (RecipeCoursePersistenceModelItem item : items) {
            itemArray[i] = new RecipeCourseItemEntity(
                    item.getDataId(),
                    parentDataId,
                    item.getDomainId(),
                    item.getCourse().getId(),
                    item.isActive(),
                    item.getCreateDate(),
                    item.getLastUpdate()
            );
            i++;
        }
        return itemArray;
    }


    public List<RecipeCoursePersistenceModelItem> convertCourseItemEntitiesToDomainModels(
            @Nonnull List<RecipeCourseItemEntity> entities) {
        List<RecipeCoursePersistenceModelItem> models = new ArrayList<>();
        entities.forEach(entity -> {
            models.add(convertCourseItemEntityToDomainModel(entity));
        });
        return models;
    }

    public RecipeCoursePersistenceModelItem convertCourseItemEntityToDomainModel(
            @Nonnull RecipeCourseItemEntity entity) {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setCourse(RecipeCourse.Course.fromId(entity.getCourseId())).
                setIsActive(entity.isActive()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLasUpdate()).
                build();
    }
}
