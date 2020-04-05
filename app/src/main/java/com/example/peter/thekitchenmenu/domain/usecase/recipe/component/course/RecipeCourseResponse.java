package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBasePlusModelMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseResponse
        extends UseCaseDomainMessageBasePlusModelMetadata<RecipeCourseResponse.Model>
        implements UseCase.Response {

    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "metadata=" + metadata +
                ", model=" + model +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                '}';
    }

    public static class Builder extends UseCaseMessageBuilderWithMetadata
            <Builder, RecipeCourseResponse, Model> {

        public Builder() {
            message = new RecipeCourseResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadata.Builder().getDefault().build();
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

    }

    public static final class Model extends UseCaseDomainModel {

        private HashMap<RecipeCourse.Course, RecipeCoursePersistenceModel> courseList;

        private Model() {}

        @Nonnull
        public HashMap<RecipeCourse.Course, RecipeCoursePersistenceModel> getCourseList() {
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
                model.courseList = new HashMap<>();
                return self();
            }

            public Builder setCourseList(
                    HashMap<RecipeCourse.Course, RecipeCoursePersistenceModel> courseList) {
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
