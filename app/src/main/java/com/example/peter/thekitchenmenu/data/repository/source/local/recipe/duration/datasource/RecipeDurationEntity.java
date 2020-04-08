package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeDurationEntity.TABLE_RECIPE_DURATION)
public final class RecipeDurationEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE_DURATION = "recipeDuration";
    public static final String ID = "id";
    public static final String PREP_TIME = "prepTime";
    public static final String COOK_TIME = "cookTime";
    public static final String CREATE_DATE = "createDate";
    public static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @ColumnInfo(name = PREP_TIME)
    private final int prepTime;

    @ColumnInfo(name = COOK_TIME)
    private final int cookTime;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeDurationEntity(@Nonnull String id,
                                int prepTime,
                                int cookTime,
                                long createDate,
                                long lastUpdate) {
        this.id = id;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDurationEntity that = (RecipeDurationEntity) o;
        return prepTime == that.prepTime &&
                cookTime == that.cookTime &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prepTime, cookTime, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationEntity{" +
                "id='" + id + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
    }

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
}
