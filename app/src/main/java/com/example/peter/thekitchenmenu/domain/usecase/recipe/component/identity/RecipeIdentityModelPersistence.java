package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecipeIdentityModelPersistence
        extends BasePersistence {

    private String title;
    private String description;
    private long createDate;
    private long lastUpdate;

    private RecipeIdentityModelPersistence(){}

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
        RecipeIdentityModelPersistence that = (RecipeIdentityModelPersistence) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, title, description, createDate, lastUpdate);
    }

    @Nonnull
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
            extends DomainModelBuilder<Builder, RecipeIdentityModelPersistence> {

        public Builder() {
            model = new RecipeIdentityModelPersistence();
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
                @Nonnull RecipeIdentityModelPersistence m) {
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
