package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityUseCaseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseModel {

    private String title;
    private String description;

    private RecipeIdentityUseCaseModel(){}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIdentityUseCaseModel)) return false;

        RecipeIdentityUseCaseModel that = (RecipeIdentityUseCaseModel) o;

        if (!Objects.equals(title, that.title)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityUseCaseModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeIdentityUseCaseModel> {

        public Builder() {
            super(new RecipeIdentityUseCaseModel());
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
        public Builder getDefault() {
            domainModel.title = RecipeIdentityUseCase.DEFAULT_TITLE;
            domainModel.description = RecipeIdentityUseCase.DEFAULT_DESCRIPTION;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeIdentityUseCaseModel model) {
            domainModel.title = model.title;
            domainModel.description = model.description;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
