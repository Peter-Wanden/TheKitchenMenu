package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.*;

public final class RecipeCourseRequest
        extends UseCaseMessageModelDataId<RecipeCourseRequest.Model>
        implements UseCaseBase.Request {

    @Override
    public String toString() {
        return "RecipeCourseRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipeCourseRequest() {
    }

    public static class Builder
            extends UseCaseMessageModelDataIdBuilder<Builder, RecipeCourseRequest, Model> {

        public Builder() {
            message = new RecipeCourseRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeCourseResponse response) {
            message.dataId = response.getDataId();
            message.domainId = response.getDomainId();
            message.model = new Model.Builder().
                    basedOnResponseModel(response.getModel()).
                    build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {

        private List<Course> courseList;

        private Model() {
        }

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
                domainModel = new Model();
            }

            public Builder getDefault() {
                domainModel.courseList = new ArrayList<>();
                return self();
            }

            public Builder basedOnResponseModel(RecipeCourseResponse.Model m) {
                domainModel.courseList = new ArrayList<>(m.getCourseList().keySet());
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
}
