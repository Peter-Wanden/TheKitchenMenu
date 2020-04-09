package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModelMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseResponse
        extends BaseDomainMessageModelMetadata<RecipeCourseResponse.Model>
        implements UseCase.Response {

    private RecipeCourseResponse() {}

    public static class Builder
            extends UseCaseMessageBuilderMetadata<Builder, RecipeCourseResponse, Model> {

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

    public static final class Model extends BaseDomainModel {

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
