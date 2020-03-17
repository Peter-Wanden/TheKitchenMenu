package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourse.*;

public final class RecipeCourseRequest extends RecipeComponentRequest<RecipeCourseRequest.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeRequestBuilder<Builder, RecipeCourseRequest, Model> {

        public Builder() {
            request = new RecipeCourseRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(new Model.Builder().
                            getDefault().
                            build());
        }

        public Builder basedOnResponse(RecipeCourseResponse response) {
            request.id = response.getId();
            request.model.courseList = new ArrayList<>(
                    response.getModel().getCourseList().keySet());
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends RecipeDataModel {

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

        public static class Builder extends RecipeDataModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Builder().setCourseList(getDefaultList());
            }

            public Builder setCourseList(List<Course> courseList) {
                model.courseList = courseList;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }

            private static List<Course> getDefaultList() {
                return new ArrayList<>();
            }
        }
    }
}
