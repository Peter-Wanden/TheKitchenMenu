package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseResponse
        extends
        UseCaseMessageModelDataIdMetadata<RecipeCourseResponse.DomainModel>
        implements
        UseCaseBase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                "'}'";
    }

    private RecipeCourseResponse() {
    }

    public static class Builder
            extends MessageModelDataIdMetadataBuilder<Builder, RecipeCourseResponse, DomainModel> {

        public Builder() {
            message = new RecipeCourseResponse();
        }

        @Nonnull
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
            message.model = new DomainModel.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class DomainModel
            extends
            BaseDomainModel {

        private List<Course> courseList;

        private DomainModel() {
        }

        @Nonnull
        public List<Course> getCourses() {
            return courseList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DomainModel)) return false;
            DomainModel domainModel = (DomainModel) o;
            return Objects.equals(courseList, domainModel.courseList);
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

        public static class Builder
                extends
                BaseDomainModelBuilder<Builder, DomainModel> {

            public Builder() {
                domainModel = new DomainModel();
            }

            @Override
            public Builder basedOnModel(DomainModel model) {
                return null;
            }

            @Override
            public Builder getDefault() {
                domainModel.courseList = new ArrayList<>();
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
