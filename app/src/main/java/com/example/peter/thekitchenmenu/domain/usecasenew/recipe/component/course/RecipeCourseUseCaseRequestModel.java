package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeCourseUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private List<Course> courseList;

    private RecipeCourseUseCaseRequestModel() {}

    public List<Course> getCourseList() {
        return courseList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseUseCaseRequestModel)) return false;

        RecipeCourseUseCaseRequestModel that = (RecipeCourseUseCaseRequestModel) o;

        return Objects.equals(courseList, that.courseList);
    }

    @Override
    public int hashCode() {
        return courseList != null ? courseList.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseUseCaseRequestModel{" +
                "courseList=" + courseList +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeCourseUseCaseRequestModel> {

        public Builder() {
            domainModel = new RecipeCourseUseCaseRequestModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.courseList = new ArrayList<>();
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeCourseUseCaseRequestModel model) {
            domainModel.courseList = model.courseList;
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
