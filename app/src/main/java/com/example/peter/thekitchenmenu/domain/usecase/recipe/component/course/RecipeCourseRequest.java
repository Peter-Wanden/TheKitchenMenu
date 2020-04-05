package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBasePlusModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.*;

public final class RecipeCourseRequest
        extends UseCaseDomainMessageBasePlusModel<RecipeCourseRequest.Model>
        implements UseCase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseRequest{" +
                "dataId='" + dataId + '\'' +
                "domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder
            extends UseCaseMessageBuilderWithModel
            <Builder, RecipeCourseRequest, Model> {

        public Builder() {
            message = new RecipeCourseRequest();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeCourseResponse response) {
            message.dataId = response.getDataId();
            message.model.courseList = new ArrayList<>(response.
                    getModel().
                    getCourseList().
                    keySet()
            );
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends UseCaseDomainModel {

        private List<Course> courseList;

        public List<Course> getCourseList() {
            return courseList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return courseList.equals(model.courseList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(courseList);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "courseList=" + courseList +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.courseList = new ArrayList<>();
                return self();
            }

            public Builder setCourseList(List<Course> courseList) {
                model.courseList = courseList;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
