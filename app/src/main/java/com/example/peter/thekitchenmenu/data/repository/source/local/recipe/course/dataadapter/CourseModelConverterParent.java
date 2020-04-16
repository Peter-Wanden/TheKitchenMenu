package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverterParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseModelConverterParent
        implements
        DomainModelConverterParent<RecipeCoursePersistenceModel, RecipeCourseEntity> {

    @Override
    public RecipeCoursePersistenceModel convertToModel(@Nonnull RecipeCourseEntity e) {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId(e.getDataId()).
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
                m.getLastUpdate()
        );
    }

    @Override
    public List<RecipeCoursePersistenceModel> convertToModels(
            @Nonnull List<RecipeCourseEntity> es) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        for (RecipeCourseEntity e : es) {
            models.add(convertToModel(e));
        }
        return models;
    }
}
