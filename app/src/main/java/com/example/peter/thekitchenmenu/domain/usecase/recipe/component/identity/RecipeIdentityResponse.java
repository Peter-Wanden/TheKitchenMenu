package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.MessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadataModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityResponse
        extends MessageModelDataIdMetadata<RecipeIdentityResponse.Model>
        implements UseCaseBase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                "'}'";
    }

    private RecipeIdentityResponse() {}

    public static class Builder extends MessageModelDataIdMetadataBuilder
            <Builder, RecipeIdentityResponse, Model> {

        public Builder() {
            message = new RecipeIdentityResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
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

        private Model() {}

        @Nonnull
        public String getTitle() {
            return title;
        }

        @Nonnull
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
                domainModel = new Model();
            }

            public Builder getDefault() {
                domainModel.title = "";
                domainModel.description = "";
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