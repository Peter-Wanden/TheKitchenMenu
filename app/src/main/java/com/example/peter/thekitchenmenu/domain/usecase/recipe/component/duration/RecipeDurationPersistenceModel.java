package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationPersistenceModel
        extends BasePersistence {

    private int prepTime;
    private int cookTime;
    private long createDate;
    private long lastUpdate;

    private RecipeDurationPersistenceModel() {}

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
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
        RecipeDurationPersistenceModel that = (RecipeDurationPersistenceModel) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                prepTime == that.prepTime &&
                cookTime == that.cookTime &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, prepTime, cookTime, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId=" + domainId + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends DomainModelBuilder<Builder, RecipeDurationPersistenceModel> {

        public Builder() {
            model = new RecipeDurationPersistenceModel();
        }

        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.prepTime = 0;
            model.cookTime = 0;
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return self();
        }

        public Builder basedOnPersistenceModel(
                @Nonnull RecipeDurationPersistenceModel m) {
            model.dataId = m.getDataId();
            model.domainId = m.getDomainId();
            model.prepTime = m.getPrepTime();
            model.cookTime = m.getCookTime();
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

        public Builder setPrepTime(int prepTime) {
            model.prepTime = prepTime;
            return self();
        }

        public Builder setCookTime(int cookTime) {
            model.cookTime = cookTime;
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
