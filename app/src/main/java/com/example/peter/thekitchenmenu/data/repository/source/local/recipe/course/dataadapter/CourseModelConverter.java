package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.Course;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseModelConverter {

    @Nonnull
    private UniqueIdProvider idProvider;

    public CourseModelConverter(@Nonnull UniqueIdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public RecipeCourseUseCasePersistenceModel.Builder addParentEntityToDomainModelBuilder(
            @Nonnull RecipeCourseParentEntity entity) {
        return new RecipeCourseUseCasePersistenceModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate());
    }

    public List<RecipeCourseUseCasePersistenceModel.Builder> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeCourseParentEntity> entities) {

        List<RecipeCourseUseCasePersistenceModel.Builder> models = new ArrayList<>();
        entities.forEach(entity -> models.add(addParentEntityToDomainModelBuilder(entity)));
        return models;
    }

    public RecipeCourseParentEntity convertParentDomainModelToEntity(
            @Nonnull RecipeCourseUseCasePersistenceModel model) {
        return new RecipeCourseParentEntity.Builder().
                getDefault().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setCreateDate(model.getCreateDate()).
                setLastUpdate(model.getLastUpdate()).
                build();
    }

    public RecipeCourseEntity[] convertDomainCoursesToEntities(
            @Nonnull List<Course> courses,
            @Nonnull String parentDataId) {

        RecipeCourseEntity[] itemArray = new RecipeCourseEntity[courses.size()];

        for (int i = 0; i < courses.size(); i++) {
            itemArray[i] = new RecipeCourseEntity(
                    idProvider.getUId(),
                    parentDataId,
                    courses.get(i).getId()
            );
        }
        return itemArray;
    }


    public List<Course> convertCourseEntitiesToDomainModels(
            @Nonnull List<RecipeCourseEntity> entities) {
        List<Course> models = new ArrayList<>();
        entities.forEach(entity -> models.add(Course.fromId(entity.getCourseId())));
        return models;
    }
}
