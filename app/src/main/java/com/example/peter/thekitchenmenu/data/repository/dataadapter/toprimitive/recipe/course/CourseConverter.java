package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.ArrayList;
import java.util.List;

class CourseConverter {

    static RecipeCourseModelPersistence convertToModel(RecipeCourseEntity e) {
        return new RecipeCourseModelPersistence.Builder().
                setDataId(e.getId()).
                setDomainId(e.getRecipeId()).
                setCourse(RecipeCourse.Course.fromInt(e.getCourseNo())).
                setIsActive(e.isActive()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLasUpdate()).
                build();
    }


    static RecipeCourseEntity convertToPrimitive(RecipeCourseModelPersistence m) {
        return new RecipeCourseEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getCourse().getCourseNo(),
                m.isActive(),
                m.getCreateDate(),
                m.getLasUpdate()
        );
    }

    static List<RecipeCourseModelPersistence> convertToModels(List<RecipeCourseEntity> entities) {
        List<RecipeCourseModelPersistence> models = new ArrayList<>();
        for (RecipeCourseEntity e : entities) {
            models.add(CourseConverter.convertToModel(e));
        }
        return models;
    }

    static List<RecipeCourseModelPersistence> convertActiveModels(
            List<RecipeCourseEntity> entities) {

        List<RecipeCourseModelPersistence> models = new ArrayList<>();
        for (RecipeCourseEntity e : entities) {
            if (e.isActive()) {
                models.add(CourseConverter.convertToModel(e));
            }
        }
        return models;
    }
}
