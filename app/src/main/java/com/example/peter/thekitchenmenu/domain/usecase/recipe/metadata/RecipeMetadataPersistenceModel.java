package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public final class RecipeMetadataPersistenceModel implements PersistenceModel {
    @Nonnull
    private final String id;
    @Nonnull
    private final String parentId;
    @Nonnull
    private HashMap<ComponentName, ComponentState> componentStates;
    @Nonnull
    private final String createdBy;
    private final long createDate;
    private final long lastUpdate;

    private RecipeMetadataPersistenceModel(
            @Nonnull String id,
            @Nonnull String parentId,
            @Nonnull HashMap<ComponentName, ComponentState> componentStates,
            @Nonnull String createdBy,
            long createDate,
            long lastUpdate) {
        this.id = id;
        this.parentId = parentId;
        this.componentStates = componentStates;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getParentId() {
        return parentId;
    }

    @Nonnull
    public HashMap<ComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Nonnull
    public String getCreatedBy() {
        return createdBy;
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
        RecipeMetadataPersistenceModel that = (RecipeMetadataPersistenceModel) o;
        return id.equals(that.id) &&
                parentId.equals(that.parentId) &&
                componentStates.equals(that.componentStates) &&
                createdBy.equals(that.createdBy) &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, createdBy, componentStates, createDate, lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeMetadataPersistenceModel{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", componentStates=" + componentStates + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {
        private String id;
        private String parentId;
        private HashMap<ComponentName, ComponentState> componentStates;
        private String createdBy;
        private long createDate;
        private long lastUpdate;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setParentId("").
                    setComponentStates(new HashMap<>()).
                    setCreatedBy(Constants.getUserId()).
                    setCreateDate(0L).
                    setLastUpdate(0L);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder setComponentStates(HashMap<ComponentName, ComponentState> componentStates) {
            this.componentStates = componentStates;
            return this;
        }

        public Builder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder setCreateDate(long createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public RecipeMetadataPersistenceModel build() {
            return new RecipeMetadataPersistenceModel(
                    id,
                    parentId,
                    componentStates,
                    createdBy,
                    createDate,
                    lastUpdate
            );
        }
    }
}
