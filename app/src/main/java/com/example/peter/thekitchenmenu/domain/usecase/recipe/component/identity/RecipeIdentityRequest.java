package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseRequestWithDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityRequest
        extends UseCaseRequestWithDomainModel<RecipeIdentityRequest.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityRequest{" +
                "id='" + dataId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder
            extends UseCaseRequestBuilder<Builder, RecipeIdentityRequest, Model> {

        public Builder() {
            request = new RecipeIdentityRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setDataId("").
                    setModel(new Model.Builder().
                            getDefault().
                            build());
        }

        public Builder basedOnResponse(RecipeIdentityResponse response) {
            request.dataId = response.getId();
            request.model.title = response.getModel().getTitle();
            request.model.description = response.getModel().getDescription();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends UseCaseDomainModel {
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
                return new Builder().
                        setTitle("").
                        setDescription("");
            }

            public static Builder basedOnResponseModel(RecipeIdentityResponse.Model model) {
                return new Builder().
                        setTitle(model.getTitle()).
                        setDescription(model.getDescription());
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