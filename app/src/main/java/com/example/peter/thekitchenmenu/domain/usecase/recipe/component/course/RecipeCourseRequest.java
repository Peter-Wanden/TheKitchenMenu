package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.*;

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

        private Set<Course> courseList;

        private DomainModel() {
        }

        public Set<Course> getCourseList() {
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

        @Nullable
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
                domainModel.courseList = new HashSet<>();
                return self();
            }

            public Builder basedOnResponseModel(RecipeCourseResponse.Model m) {
                domainModel.courseList = new HashSet<>(m.getCourseList());
                return self();
            }

            public Builder setCourseList(Set<Course> courseList) {
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
