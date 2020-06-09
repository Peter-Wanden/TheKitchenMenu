package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverterParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

public class CourseModelConverterParent
        implements
        DomainModelConverterParent<RecipeCoursePersistenceModel, RecipeCourseEntity> {

    @Override
    public RecipeCoursePersistenceModelItem convertToModelItem(@Nonnull RecipeCourseEntity entity) {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getRecipeId()).
                setCourse(RecipeCourse.Course.fromInt(entity.getCourseNo())).
                setIsActive(entity.isActive()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLasUpdate()).
                build();
    }

    public RecipeCoursePersistenceModel convertToModel(@Nonnull List<RecipeCourseEntity> entities) {
        Set<RecipeCoursePersistenceModelItem> modelItems = new HashSet<>(convertToModels(entities));

        String domainId = "";
        long createDate = modelItems.isEmpty() ? 0L : Long.MAX_VALUE;
        long lastUpdate = 0L;
        for (RecipeCoursePersistenceModelItem item : modelItems) {
            domainId = item.getDomainId();
            createDate = Math.min(createDate, item.getCreateDate());
            lastUpdate = Math.max(lastUpdate, item.getLastUpdate());
        }

        return new RecipeCoursePersistenceModel.Builder().getDefault().
                setDomainId(domainId).
                setPersistenceModelItems(modelItems).
                setCreateDate(createDate).
                setLastUpdate(lastUpdate).
                build();
    }

    @Override
    public RecipeCourseEntity convertToPrimitive(@Nonnull RecipeCoursePersistenceModelItem parent) {
        return new RecipeCourseEntity(
                parent.getDataId(),
                parent.getDomainId(),
                parent.getCourse().getCourseNo(),
                parent.isActive(),
                parent.getCreateDate(),
                parent.getLastUpdate()
        );
    }

    @Override
    public List<RecipeCoursePersistenceModelItem> convertToModels(
            @Nonnull List<RecipeCourseEntity> entities) {
        List<RecipeCoursePersistenceModelItem> models = new ArrayList<>();
        for (RecipeCourseEntity e : entities) {
            models.add(convertToModelItem(e));
        }
        return models;
    }
}
