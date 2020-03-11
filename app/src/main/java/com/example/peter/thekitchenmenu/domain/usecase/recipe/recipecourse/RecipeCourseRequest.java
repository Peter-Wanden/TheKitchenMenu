package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse.*;

public final class RecipeCourseRequest extends RecipeRequestBase<RecipeCourseRequest.Model> {

    private RecipeCourseRequest(@Nonnull String id, @Nonnull Model model) {
        super(id, model);
    }

    @Nonnull
    @Override
    public Model getModel() {
        return model;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String id;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeCourseRequest build() {
            return new RecipeCourseRequest(
                    id,
                    model
            );
        }
    }

    public static final class Model implements RecipeRequestBase.RecipeRequestModel {
        @Nonnull
        private final List<Course> courseList;

        private Model(@Nonnull List<Course> courseList) {
            this.courseList = courseList;
        }

        @Nonnull
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

        @Override
        public String toString() {
            return "Model{" +
                    "courseList=" + courseList +
                    '}';
        }

        public static final class Builder {
            private List<Course> courseList;

            public static Builder getDefault() {
                return new Builder().
                        setCourseList(getDefaultList());
            }

            public Builder setCourseList(List<Course> courseList) {
                this.courseList = courseList;
                return this;
            }

            public Model build() {
                return new Model(
                        courseList
                );
            }

            private static List<Course> getDefaultList() {
                return new ArrayList<>();
            }
        }
    }
}
