package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.model.BasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public final class RecipeCoursePersistenceModel
        extends
        BasePersistenceModel {

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

    @Nonnull
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
            persistenceModel = new RecipeCoursePersistenceModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.courses = new ArrayList<>();
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeCoursePersistenceModel persistenceModel) {
            this.persistenceModel.dataId = persistenceModel.getDataId();
            this.persistenceModel.domainId = persistenceModel.getDomainId();
            this.persistenceModel.courses = persistenceModel.getCourses();
            this.persistenceModel.createDate = persistenceModel.getCreateDate();
            this.persistenceModel.lastUpdate = persistenceModel.getLastUpdate();
            return self();
        }

        public Builder setCourses(List<Course> items) {
            persistenceModel.courses = items;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
