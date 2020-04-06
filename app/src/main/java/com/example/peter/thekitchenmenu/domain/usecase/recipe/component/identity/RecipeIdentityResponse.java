package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBaseModelMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityResponse
        extends UseCaseDomainMessageBaseModelMetadata<RecipeIdentityResponse.Model>
        implements UseCase.Response {

    private RecipeIdentityResponse() {}

    public static class Builder extends UseCaseMessageBuilderMetadata
            <Builder, RecipeIdentityResponse, Model> {

        public Builder() {
            message = new RecipeIdentityResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            message.metadata = new UseCaseMetadata.Builder().getDefault().build();
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

        @Override
        public String toString() {
            return "Model{" +
                    "title='" + title + '\'' +
                    ", description='" + description +
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