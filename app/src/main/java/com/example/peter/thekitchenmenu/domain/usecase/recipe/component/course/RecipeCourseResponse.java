package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseResponse extends UseCaseResponse<RecipeCourseResponse.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder extends UseCaseResponseBuilder<
                    Builder,
                    RecipeCourseResponse,
                    Model> {

        public Builder() {
            response = new RecipeCourseResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(new UseCaseMetadata.Builder().
                                    getDefault().
                                    build()).
                    setModel(new Model.Builder().
                                    getDefault().
                                    build());
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

        public static class Builder extends DomainModelBuilder<
                                Builder,
                                Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Builder().
                        setCourseList(getDefaultCourseList());
            }

            public Builder setCourseList(
                    HashMap<RecipeCourse.Course, RecipeCoursePersistenceModel> courseList) {
                model.courseList = courseList;
                return self();
            }

            private static HashMap<RecipeCourse.Course, RecipeCoursePersistenceModel> getDefaultCourseList() {
                return new HashMap<>();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
