package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeDataModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseResponse extends RecipeComponentResponse<RecipeCourseResponse.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeComponentResponseBuilder<
                Builder,
                RecipeCourseResponse,
                Model> {

        public Builder() {
            response = new RecipeCourseResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(new RecipeComponentMetadata.Builder().
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

    public static final class Model extends RecipeDataModel {

        private HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

        private Model() {}

        @Nonnull
        public HashMap<RecipeCourse.Course, RecipeCourseModel> getCourseList() {
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

        public static class Builder extends RecipeDataModelBuilder<
                Builder,
                Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Builder().
                        setCourseList(getDefaultCourseList());
            }

            public Builder setCourseList(HashMap<RecipeCourse.Course, RecipeCourseModel> courseList) {
                model.courseList = courseList;
                return self();
            }

            private static HashMap<RecipeCourse.Course, RecipeCourseModel> getDefaultCourseList() {
                return new HashMap<>();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
