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
    public static final String CREATED_BY = "createdBy";
    public static final String CREATE_DATE = "createDate";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String IS_DRAFT = "isDraft";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @NonNull
    @ColumnInfo(name = PARENT_ID)
    private final String parentId;

    @NonNull
    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    @ColumnInfo(name = IS_DRAFT)
    private final boolean isDraft;

    public RecipeEntity(@NonNull String id,
                        @NonNull String parentId,
                        @NonNull String createdBy,
                        long createDate,
                        long lastUpdate,
                        boolean isDraft) {
        this.id = id;
        this.parentId = parentId;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.isDraft = isDraft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeEntity that = (RecipeEntity) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                isDraft == that.isDraft &&
                id.equals(that.id) &&
                parentId.equals(that.parentId) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, createdBy, createDate, lastUpdate, isDraft);
    }

    @Override
    public String toString() {
        return "RecipeEntity{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                ", isDraft=" + isDraft +
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

    @NonNull
    public String getCreatedBy() {
        return createdBy;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public boolean isDraft() {
        return isDraft;
    }
}
