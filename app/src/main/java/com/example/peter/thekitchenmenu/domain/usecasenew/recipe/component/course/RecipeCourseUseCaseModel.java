package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseUseCaseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseModel {

    private List<Course> courses;

    private RecipeCourseUseCaseModel() {}

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseUseCaseModel)) return false;

        RecipeCourseUseCaseModel that = (RecipeCourseUseCaseModel) o;

        return Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return courses != null ? courses.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseUseCaseModel{" +
                "courses=" + courses +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeCourseUseCaseModel> {

        public Builder() {
            super(new RecipeCourseUseCaseModel());
        }

        public Builder setCourses(List<Course> courses) {
            domainModel.courses = courses;
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.courses = new ArrayList<>();
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeCourseUseCaseModel model) {
            domainModel.courses = model.courses;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
