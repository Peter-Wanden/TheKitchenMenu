package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private List<Course> courses;

    private RecipeCourseUseCaseResponseModel() {}

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseUseCaseResponseModel)) return false;

        RecipeCourseUseCaseResponseModel that = (RecipeCourseUseCaseResponseModel) o;

        return Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return courses != null ? courses.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseUseCaseResponseModel{" +
                "courses=" + courses +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeCourseUseCaseResponseModel> {

        public Builder() {
            super(new RecipeCourseUseCaseResponseModel());
        }

        @Override
        public Builder basedOnModel(RecipeCourseUseCaseResponseModel model) {
            domainModel.courses = model.courses;
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.courses = new ArrayList<>();
            return self();
        }

        public Builder setCourses(List<Course> courses) {
            domainModel.courses = courses;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
