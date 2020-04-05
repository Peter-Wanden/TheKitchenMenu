package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipePersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecipeIdentityPersistenceModel
        extends RecipePersistenceModel {

    private String title;
    private String description;
    private long createDate;
    private long lastUpdate;

    private RecipeIdentityPersistenceModel(){}

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityPersistenceModel that = (RecipeIdentityPersistenceModel) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                title.equals(that.title) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, createDate, lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeIdentityPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends DomainModelBuilder<Builder, RecipeIdentityPersistenceModel> {

        public Builder() {
            model = new RecipeIdentityPersistenceModel();
        }

        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.title = "";
            model.description = "";
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return self();
        }

        public Builder basedOnPersistenceModel(
                @Nonnull RecipeIdentityPersistenceModel m) {
            model.dataId = m.getDataId();
            model.domainId = m.getDomainId();
            model.title = m.getTitle();
            model.description = m.getDescription();
            model.createDate = m.getCreateDate();
            model.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            model.domainId = domainId;
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

        public Builder setCreateDate(long createDate) {
            model.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            model.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
