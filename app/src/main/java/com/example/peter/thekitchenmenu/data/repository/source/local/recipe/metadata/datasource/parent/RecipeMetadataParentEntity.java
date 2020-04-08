package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeMetadataParentEntity.TABLE_RECIPE)
public final class RecipeMetadataParentEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE = "recipe";
    public static final String DATA_ID = "id";
    public static final String DOMAIN_ID = "domainId";
    private static final String RECIPE_PARENT_ID = "recipeParentId";
    private static final String RECIPE_STATE_ID = "recipeStateId";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = DOMAIN_ID)
    private final String domainId;

    @Nonnull
    @ColumnInfo(name = RECIPE_PARENT_ID)
    private final String recipeParentId;

    @ColumnInfo(name = RECIPE_STATE_ID)
    private final int recipeStateId;

    @Nonnull
    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeMetadataParentEntity(@Nonnull String id,
                                      @Nonnull String domainId,
                                      @Nonnull String recipeParentId,
                                      int recipeStateId,
                                      @Nonnull String createdBy,
                                      long createDate,
                                      long lastUpdate) {
        this.id = id;
        this.domainId = domainId;
        this.recipeParentId = recipeParentId;
        this.recipeStateId = recipeStateId;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeMetadataParentEntity that = (RecipeMetadataParentEntity) o;
        return recipeStateId == that.recipeStateId &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                id.equals(that.id) &&
                domainId.equals(that.domainId) &&
                recipeParentId.equals(that.recipeParentId) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, domainId, recipeParentId, recipeStateId,
                createdBy, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataEntity{" +
                "dataId='" + id + '\'' +
                ", domainId='" + domainId + '\'' +
                ", recipeParentId='" + recipeParentId + '\'' +
                ", recipeStateId=" + recipeStateId +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Nonnull
    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    public String getDomainId() {
        return domainId;
    }

    @Nonnull
    public String getRecipeParentId() {
        return recipeParentId;
    }

    public int getRecipeStateId() {
        return recipeStateId;
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

    public static class Builder {
        private String dataId;
        private String domainId;
        private String recipeParentId;
        private int recipeStateId;
        private String createdBy;
        private long createDate;
        private long lastUpdate;

        public Builder getDefault() {
            dataId = "";
            domainId = "";
            recipeParentId = "";
            recipeStateId = 0;
            createdBy = Constants.getUserId();
            createDate = 0L;
            lastUpdate = 0L;
            return this;
        }

        public Builder setDataId(String dataId) {
            this.dataId = dataId;
            return this;
        }

        public Builder setDomainId(String domainId) {
            this.domainId = domainId;
            return this;
        }

        public Builder setRecipeParentId(String recipeParentId) {
            this.recipeParentId = recipeParentId;
            return this;
        }

        public Builder setRecipeStateId(int recipeStateId) {
            this.recipeStateId = recipeStateId;
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

        public RecipeMetadataParentEntity build() {
            return new RecipeMetadataParentEntity(
                    dataId,
                    domainId,
                    recipeParentId,
                    recipeStateId,
                    createdBy,
                    createDate,
                    lastUpdate
            );
        }
    }
}
