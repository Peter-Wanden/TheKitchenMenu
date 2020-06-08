package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCoursePersistenceModelItem
        extends
        BaseDomainPersistenceModel {

    private Course course;
    private boolean isActive;

    private RecipeCoursePersistenceModelItem(){}

    @Nonnull
    public Course getCourse() {
        return course;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCoursePersistenceModelItem)) return false;
        if (!super.equals(o)) return false;
        RecipeCoursePersistenceModelItem that = (RecipeCoursePersistenceModelItem) o;
        return isActive == that.isActive &&
                course == that.course;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), course, isActive);
    }

    @Override
    public String toString() {
        return "RecipeCoursePersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", course=" + course +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeCoursePersistenceModelItem> {

        public Builder() {
            domainModel = new RecipeCoursePersistenceModelItem();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.course = Course.COURSE_ZERO;
            domainModel.isActive = false;
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        public Builder basedOnModel (RecipeCoursePersistenceModelItem m) {
            domainModel.dataId = m.getDataId();
            domainModel.domainId = m.getDomainId();
            domainModel.course = m.getCourse();
            domainModel.isActive = m.isActive();
            domainModel.createDate = m.getCreateDate();
            domainModel.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setCourse(Course course) {
            domainModel.course = course;
            return self();
        }

        public Builder setIsActive(boolean isActive) {
            domainModel.isActive = isActive;
            return self();
        }

        @Override
        protected Builder self() {
            return super.self();
        }
    }
}
