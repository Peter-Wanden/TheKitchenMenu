package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainPersistenceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseUseCasePersistenceModel
        extends
        BaseDomainPersistenceModel {

    private List<Course> courses;

    private RecipeCourseUseCasePersistenceModel(){}

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseUseCasePersistenceModel)) return false;

        RecipeCourseUseCasePersistenceModel that = (RecipeCourseUseCasePersistenceModel) o;

        return Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return courses != null ? courses.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseUseCasePersistenceModel{" +
                "courses=" + courses +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeCourseUseCasePersistenceModel> {

        public Builder() {
            super(new RecipeCourseUseCasePersistenceModel());
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
        public Builder basedOnModel(RecipeCourseUseCasePersistenceModel domainModel) {
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
