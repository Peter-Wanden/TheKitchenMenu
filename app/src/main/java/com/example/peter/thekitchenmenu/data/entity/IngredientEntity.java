package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = IngredientEntity.TABLE_INGREDIENTS)
public class IngredientEntity implements TkmEntity {

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String ID = "id";
    public static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String CONVERSION_FACTOR = "conversionFactor";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @NonNull
    @ColumnInfo(name = NAME)
    private final String name;

    @Nullable
    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @ColumnInfo(name = CONVERSION_FACTOR)
    private final double conversionFactor;

    @NonNull
    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public IngredientEntity(@NonNull String id,
                            @NonNull String name,
                            @Nullable String description,
                            double conversionFactor,
                            @NonNull String createdBy,
                            long createDate,
                            long lastUpdate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.conversionFactor = conversionFactor;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientEntity entity = (IngredientEntity) o;
        return Double.compare(entity.conversionFactor, conversionFactor) == 0 &&
                createDate == entity.createDate &&
                lastUpdate == entity.lastUpdate &&
                id.equals(entity.id) &&
                name.equals(entity.name) &&
                description.equals(entity.description) &&
                createdBy.equals(entity.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                description,
                conversionFactor,
                createdBy,
                createDate,
                lastUpdate);
    }

    @NonNull
    @Override
    public String toString() {
        return "IngredientEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conversionFactor=" + conversionFactor +
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
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public double getConversionFactor() {
        return conversionFactor;
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
