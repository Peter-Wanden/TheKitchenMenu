package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;

public final class RecipeCourseRequest
        extends
        UseCaseMessageModelDataId<RecipeCourseRequest.DomainModel>
        implements
        UseCaseBase.Request {

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
            extends
            UseCaseMessageModelDataIdBuilder<Builder, RecipeCourseRequest, DomainModel> {

        public Builder() {
            message = new RecipeCourseRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new DomainModel.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeCourseResponse response) {
            message.dataId = response.getDataId();
            message.domainId = response.getDomainId();
            message.model = new DomainModel.Builder().
                    basedOnResponseModel(response.getDomainModel()).
                    build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class DomainModel extends BaseDomainModel {

        private List<Course> courseList;

        private DomainModel() {
        }

        public List<Course> getCourses() {
            return courseList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DomainModel)) return false;
            DomainModel that = (DomainModel) o;
            return Objects.equals(courseList, that.courseList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(courseList);
        }

        @Nonnull
        @Override
        public String toString() {
            return "DomainModel{" +
                    "courseList=" + courseList +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, DomainModel> {

            public Builder() {
                domainModel = new DomainModel();
            }

            public Builder getDefault() {
                domainModel.courseList = new ArrayList<>();
                return self();
            }

            public Builder basedOnResponseModel(RecipeCourseResponse.Model m) {
                domainModel.courseList = new ArrayList<>(m.getCourses());
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
