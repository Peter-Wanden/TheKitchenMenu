package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseConverter
        implements
        DomainModelConverter<RecipeCoursePersistenceModel, RecipeCourseEntity>,
        DomainModelConverter.ActiveList<RecipeCoursePersistenceModel, RecipeCourseEntity> {

    @Override
    public RecipeCoursePersistenceModel convertToModel(@Nonnull RecipeCourseEntity e) {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId(e.getId()).
                setDomainId(e.getRecipeId()).
                setCourse(RecipeCourse.Course.fromInt(e.getCourseNo())).
                setIsActive(e.isActive()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLasUpdate()).
                build();
    }

    @Override
    public RecipeCourseEntity convertToPrimitive(@Nonnull RecipeCoursePersistenceModel m) {
        return new RecipeCourseEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getCourse().getCourseNo(),
                m.isActive(),
                m.getCreateDate(),
                m.getLasUpdate()
        );
    }

    @Override
    public List<RecipeCoursePersistenceModel> convertToModels(
            @Nonnull List<RecipeCourseEntity> entities) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        for (RecipeCourseEntity e : entities) {
            models.add(convertToModel(e));
        }
        return models;
    }

    @Override
    public List<RecipeCoursePersistenceModel> convertToActiveModels(
            @Nonnull List<RecipeCourseEntity> entities) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        for (RecipeCourseEntity e : entities) {
            if (e.isActive()) {
                models.add(convertToModel(e));
            }
        }
        return models;
    }
}
