package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = RecipeIdentityEntity.TABLE_RECIPE_IDENTITY)
public final class RecipeIdentityEntity implements TkmEntity {

    public static final String TABLE_RECIPE_IDENTITY = "recipeIdentity";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_BY = "createdBy";
    public static final String CREATE_DATE = "createDate";
    public static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @NonNull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @NonNull
    @ColumnInfo(name = TITLE)
    private final String title;

    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @NonNull
    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeIdentityEntity(@NonNull String id,
                                @NonNull String recipeId,
                                @NonNull String title,
                                String description,
                                @NonNull String createdBy,
                                long createDate,
                                long lastUpdate) {
        this.id = id;
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityEntity that = (RecipeIdentityEntity) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId) &&
                title.equals(that.title) &&
                description.equals(that.description) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                recipeId,
                title,
                description,
                createdBy,
                createDate,
                lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeIdentityEntity{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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
}
