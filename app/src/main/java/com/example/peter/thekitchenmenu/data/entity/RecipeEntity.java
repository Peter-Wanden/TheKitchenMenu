package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = RecipeEntity.TABLE_RECIPE)
public final class RecipeEntity implements TkmEntity {

    public static final String TABLE_RECIPE = "recipe";
    public static final String ID = "id";
    public static final String PARENT_ID = "parentId";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @NonNull
    @ColumnInfo(name = PARENT_ID)
    private final String parentId;

    @ColumnInfo(name = "createdBy")
    private final String createdBy;

    @ColumnInfo(name = "createDate")
    private final long createDate;

    @ColumnInfo(name = "lastUpdate")
    private final long lastUpdate;

    public RecipeEntity(@NonNull String id,
                        @NonNull String parentId,
                        String createdBy,
                        long createDate,
                        long lastUpdate) {
        this.id = id;
        this.parentId = parentId;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeEntity that = (RecipeEntity) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                id.equals(that.id) &&
                parentId.equals(that.parentId) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, createdBy, createDate, lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeEntity{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @NonNull
    public String getParentId() {
        return parentId;
    }

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
