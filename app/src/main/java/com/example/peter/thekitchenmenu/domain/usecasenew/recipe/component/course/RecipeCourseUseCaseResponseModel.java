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

    private List<Course> courseList;

    private RecipeCourseUseCaseResponseModel() {}

    public List<Course> getCourseList() {
        return courseList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseUseCaseResponseModel)) return false;

        RecipeCourseUseCaseResponseModel that = (RecipeCourseUseCaseResponseModel) o;

        return Objects.equals(courseList, that.courseList);
    }

    @Override
    public int hashCode() {
        return courseList != null ? courseList.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseUseCaseResponseModel{" +
                "courseList=" + courseList +
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
            domainModel.courseList = model.courseList;
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.courseList = new ArrayList<>();
            return self();
        }

        public Builder setCourseList(List<Course> courseList) {
            domainModel.courseList = courseList;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
