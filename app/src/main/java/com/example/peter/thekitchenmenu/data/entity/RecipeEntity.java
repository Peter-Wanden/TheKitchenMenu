package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.app.Constants;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = RecipeEntity.TABLE_RECIPE)
public final class RecipeEntity implements TkmEntity {

    public static final String TAG = "RecipeEntity";

    public static final String TABLE_RECIPE = "recipe";
    public static final String ID = "id";
    public static final String TITLE = "titleObservable";
    private static final String DESCRIPTION = "description";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @Nullable
    @ColumnInfo(name = TITLE)
    private final String title;

    @NonNull
    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @ColumnInfo(name = "preparationTime")
    private final int preparationTime;

    @ColumnInfo(name = "cookingTime")
    private final int cookingTime;

    @ColumnInfo(name = "createdBy")
    private final String createdBy;

    @ColumnInfo(name = "createDate")
    private final long createDate;

    @ColumnInfo(name = "lastUpdate")
    private final long lastUpdate;


    public RecipeEntity(@NonNull String id,
                        @Nullable String title,
                        @NonNull String description,
                        int preparationTime,
                        int cookingTime,
                        String createdBy,
                        long createDate,
                        long lastUpdate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeEntity that = (RecipeEntity) o;
        return preparationTime == that.preparationTime &&
                cookingTime == that.cookingTime &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                id.equals(that.id) &&
                title.equals(that.title) &&
                description.equals(that.description) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                title,
                description,
                preparationTime,
                cookingTime,
                createdBy,
                createDate,
                lastUpdate);
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipeEntity{" +
                "id='" + id + '\'' +
                ", titleObservable='" + title + '\'' +
                ", description='" + description + '\'' +
                ", preparationTime=" + preparationTime +
                ", cookingTime=" + cookingTime +
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

    @Nullable
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public int getCookingTime() {
        return cookingTime;
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
