package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private String title;
    private String description;

    private RecipeIdentityUseCaseResponseModel() {}

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
        RecipeIdentityUseCaseResponseModel domainModel = (RecipeIdentityUseCaseResponseModel) o;
        return title.equals(domainModel.title) &&
                description.equals(domainModel.description);
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
            DomainModelBuilder<Builder, RecipeIdentityUseCaseResponseModel> {

        public Builder() {
            domainModel = new RecipeIdentityUseCaseResponseModel();
        }

        @Override
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
