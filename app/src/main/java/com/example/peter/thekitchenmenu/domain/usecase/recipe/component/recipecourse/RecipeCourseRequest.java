package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseRequestWithDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourse.*;

public final class RecipeCourseRequest extends UseCaseRequestWithDomainModel<RecipeCourseRequest.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseRequest{" +
                "id='" + dataId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends UseCaseRequestBuilder<Builder, RecipeCourseRequest, Model> {

        public Builder() {
            request = new RecipeCourseRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setDataId("").
                    setModel(new Model.Builder().
                            getDefault().
                            build()
                    );
        }

        public Builder basedOnResponse(RecipeCourseResponse response) {
            request.dataId = response.getId();
            request.model.courseList = new ArrayList<>(
                    response.getModel().getCourseList().keySet()
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
