package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverterParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseModelConverterParent
        implements
        DomainModelConverterParent<RecipeCoursePersistenceModelItem, RecipeCourseEntity> {

    @Override
    public RecipeCoursePersistenceModelItem convertToModel(@Nonnull RecipeCourseEntity e) {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId(e.getDataId()).
                setDomainId(e.getRecipeId()).
                setCourse(RecipeCourse.Course.fromInt(e.getCourseNo())).
                setIsActive(e.isActive()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLasUpdate()).
                build();
    }

    @Override
    public RecipeCourseEntity convertToPrimitive(@Nonnull RecipeCoursePersistenceModelItem m) {
        return new RecipeCourseEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getCourse().getCourseNo(),
                m.isActive(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }

    @Override
    public List<RecipeCoursePersistenceModelItem> convertToModels(
            @Nonnull List<RecipeCourseEntity> es) {
        List<RecipeCoursePersistenceModelItem> models = new ArrayList<>();
        for (RecipeCourseEntity e : es) {
            models.add(convertToModel(e));
        }
        return models;
    }
}
