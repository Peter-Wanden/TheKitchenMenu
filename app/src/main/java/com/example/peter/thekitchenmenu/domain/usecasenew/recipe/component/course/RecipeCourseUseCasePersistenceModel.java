package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public final class RecipeCourseUseCasePersistenceModel
        extends
        BaseDomainPersistenceModel {

    @Nonnull
    private List<Course> courses;

    private RecipeCourseUseCasePersistenceModel(){
        courses = new ArrayList<>();
    }

    @Nonnull
    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseUseCasePersistenceModel)) return false;
        if (!super.equals(o)) return false;

        RecipeCourseUseCasePersistenceModel that = (RecipeCourseUseCasePersistenceModel) o;

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
            PersistenceModelBuilder<Builder, RecipeCourseUseCasePersistenceModel> {

        public Builder() {
            domainModel = new RecipeCourseUseCasePersistenceModel();
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

        @Override
        public Builder basedOnRequestModel(RecipeCourseUseCasePersistenceModel domainModel) {
            this.domainModel.dataId = domainModel.getDataId();
            this.domainModel.domainId = domainModel.getDomainId();
            this.domainModel.courses = domainModel.getCourses();
            this.domainModel.createDate = domainModel.getCreateDate();
            this.domainModel.lastUpdate = domainModel.getLastUpdate();
            return self();
        }

        public Builder setCourses(List<Course> items) {
            domainModel.courses = items;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
