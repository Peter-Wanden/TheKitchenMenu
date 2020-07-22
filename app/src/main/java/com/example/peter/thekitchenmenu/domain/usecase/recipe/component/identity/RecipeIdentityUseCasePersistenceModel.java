package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.model.BasePersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityUseCasePersistenceModel
        extends BasePersistenceModel {

    private String title;
    private String description;

    private RecipeIdentityUseCasePersistenceModel(){}

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
        if (!(o instanceof RecipeIdentityUseCasePersistenceModel)) return false;
        if (!super.equals(o)) return false;
        RecipeIdentityUseCasePersistenceModel that = (RecipeIdentityUseCasePersistenceModel) o;
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
            PersistenceModelBuilder<Builder, RecipeIdentityUseCasePersistenceModel> {

        public Builder() {
            persistenceModel = new RecipeIdentityUseCasePersistenceModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.title = "";
            persistenceModel.description = "";
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull RecipeIdentityUseCasePersistenceModel model) {
            persistenceModel.dataId = model.getDataId();
            persistenceModel.domainId = model.getDomainId();
            persistenceModel.title = model.getTitle();
            persistenceModel.description = model.getDescription();
            persistenceModel.createDate = model.getCreateDate();
            persistenceModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setTitle(String title) {
            persistenceModel.title = title;
            return self();
        }

        public Builder setDescription(String description) {
            persistenceModel.description = description;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
