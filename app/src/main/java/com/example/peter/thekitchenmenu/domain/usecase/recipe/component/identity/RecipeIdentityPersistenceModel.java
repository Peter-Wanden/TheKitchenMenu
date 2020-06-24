package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityPersistenceModel
        extends BaseDomainPersistenceModel {

    private String title;
    private String description;

    private RecipeIdentityPersistenceModel(){}

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
        if (!(o instanceof RecipeIdentityPersistenceModel)) return false;
        if (!super.equals(o)) return false;
        RecipeIdentityPersistenceModel that = (RecipeIdentityPersistenceModel) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityPersistenceModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeIdentityPersistenceModel> {

        public Builder() {
            domainModel = new RecipeIdentityPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.title = "";
            domainModel.description = "";
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull RecipeIdentityPersistenceModel model) {
            domainModel.dataId = model.getDataId();
            domainModel.domainId = model.getDomainId();
            domainModel.title = model.getTitle();
            domainModel.description = model.getDescription();
            domainModel.createDate = model.getCreateDate();
            domainModel.lastUpdate = model.getLastUpdate();
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
