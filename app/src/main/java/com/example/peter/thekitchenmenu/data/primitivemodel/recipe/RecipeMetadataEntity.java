package com.example.peter.thekitchenmenu.data.primitivemodel.recipe;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeMetadataEntity.TABLE_RECIPE)
public final class RecipeMetadataEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE = "recipe";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    private static final String PARENT_ID = "parentId";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @Nonnull
    @ColumnInfo(name = PARENT_ID)
    private final String parentId;

    @Nonnull
    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeMetadataEntity(@Nonnull String id,
                                @Nonnull String recipeId,
                                @Nonnull String parentId,
                                @Nonnull String createdBy,
                                long createDate,
                                long lastUpdate) {
        this.id = id;
        this.recipeId  = recipeId;
        this.parentId = parentId;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeMetadataEntity that = (RecipeMetadataEntity) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId) &&
                parentId.equals(that.parentId) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, parentId, createdBy, createDate, lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeMetadata{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", parentId='" + parentId + '\'' +
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
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public String getParentId() {
        return parentId;
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
}
