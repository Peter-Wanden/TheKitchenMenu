package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModel;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityRequest
        extends BaseDomainMessageModel<RecipeIdentityRequest.Model>
        implements UseCase.Request {

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
            extends UseCaseMessageBuilderModel<Builder, RecipeIdentityRequest, Model> {

        public Builder() {
            message = new RecipeIdentityRequest();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeIdentityResponse response) {
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
            Model model = (Model) o;
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

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.title = "";
                model.description = "";
                return self();
            }

            public Builder basedOnResponseModel(RecipeIdentityResponse.Model model) {
                this.model.title = model.getTitle();
                this.model.description = model.getDescription();
                return self();
            }

            public Builder setTitle(String title) {
                model.title = title;
                return self();
            }

            public Builder setDescription(String description) {
                model.description = description;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}