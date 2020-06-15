package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public final class RecipeCoursePersistenceModel
        extends
        BaseDomainPersistenceModel {

    @Nonnull
    private List<Course> courses;

    private RecipeCoursePersistenceModel(){
        courses = new ArrayList<>();
    }

    @Nonnull
    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCoursePersistenceModel)) return false;
        if (!super.equals(o)) return false;

        RecipeCoursePersistenceModel that = (RecipeCoursePersistenceModel) o;

        return courses.equals(that.courses);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + courses.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RecipeCoursePersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", courses=" + courses +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeCoursePersistenceModel> {

        public Builder() {
            domainModel = new RecipeCoursePersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.courses = new ArrayList<>();
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        public Builder basedOnModel(RecipeCoursePersistenceModel persistenceModel) {
            domainModel.dataId = persistenceModel.getDataId();
            domainModel.domainId = persistenceModel.getDomainId();
            domainModel.courses = persistenceModel.getCourses();
            domainModel.createDate = persistenceModel.getCreateDate();
            domainModel.lastUpdate = persistenceModel.getLastUpdate();
            return self();
        }

        public Builder setCourses(List<Course> items) {
            domainModel.courses = items;
            return self();
        }
    }
}
