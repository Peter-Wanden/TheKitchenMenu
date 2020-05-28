package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.MessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityRequest
        extends MessageModelDataId<RecipeIdentityRequest.DomainModel>
        implements UseCaseBase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipeIdentityRequest() {}

    public static class Builder
            extends
            MessageModelDataIdBuilder<Builder, RecipeIdentityRequest, DomainModel> {

        public Builder() {
            message = new RecipeIdentityRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new DomainModel.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeIdentityResponse response) {
            message.dataId = response.getDataId();
            message.domainId = response.getDomainId();
            message.model = new DomainModel.Builder().
                    basedOnResponseModel(response.getModel()).
                    build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class DomainModel extends BaseDomainModel {
        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DomainModel model = (DomainModel) o;
            return title.equals(model.title) &&
                    description.equals(model.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

        public static class Builder
                extends
                DomainModelBuilder<Builder, DomainModel> {

            public Builder() {
                domainModel = new DomainModel();
            }

            public Builder getDefault() {
                domainModel.title = "";
                domainModel.description = "";
                return self();
            }

            public Builder basedOnResponseModel(RecipeIdentityResponse.DomainModel domainModel) {
                this.domainModel.title = domainModel.getTitle();
                this.domainModel.description = domainModel.getDescription();
                return self();
            }

            public Builder setTitle(String title) {
                domainModel.title = title;
                return self();
            }

            public Builder setDescription(String description) {
                domainModel.description = description;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}