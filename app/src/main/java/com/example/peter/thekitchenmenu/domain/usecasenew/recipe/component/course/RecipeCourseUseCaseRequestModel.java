package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private List<Course> courses;

    private RecipeCourseUseCaseRequestModel() {}

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseUseCaseRequestModel)) return false;

        RecipeCourseUseCaseRequestModel that = (RecipeCourseUseCaseRequestModel) o;

        return Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return courses != null ? courses.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseUseCaseRequestModel{" +
                "courses=" + courses +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeCourseUseCaseRequestModel> {

        public Builder() {
            super(new RecipeCourseUseCaseRequestModel());
        }

        @Override
        public Builder getDefault() {
            domainModel.courses = new ArrayList<>();
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeCourseUseCaseRequestModel model) {
            domainModel.courses = model.courses;
            return self();
        }

        public Builder basedOnResponseModel(RecipeCourseUseCaseResponseModel responseModel) {
            domainModel.courses =  responseModel.getCourses();
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
