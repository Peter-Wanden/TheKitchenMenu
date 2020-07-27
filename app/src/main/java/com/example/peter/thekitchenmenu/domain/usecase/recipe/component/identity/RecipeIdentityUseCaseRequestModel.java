package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private String title;
    private String description;

    private RecipeIdentityUseCaseRequestModel(){}

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
        RecipeIdentityUseCaseRequestModel model = (RecipeIdentityUseCaseRequestModel) o;
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
            DomainModelBuilder<Builder, RecipeIdentityUseCaseRequestModel> {

        public Builder() {
            domainModel = new RecipeIdentityUseCaseRequestModel();
        }

        public Builder getDefault() {
            domainModel.title = "";
            domainModel.description = "";
            return self();
        }

        public Builder basedOnResponseModel(RecipeIdentityUseCaseResponseModel domainModel) {
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
