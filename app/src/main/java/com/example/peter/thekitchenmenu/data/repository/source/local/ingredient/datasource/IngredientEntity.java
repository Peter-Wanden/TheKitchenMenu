package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = IngredientEntity.TABLE_INGREDIENTS)
public final class IngredientEntity implements PrimitiveModel {

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String DATA_ID = "dataId";
    public static final String DOMAIN_ID = "domainId";
    public static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String CONVERSION_FACTOR = "conversionFactor";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @Nonnull
    @ColumnInfo(name = DOMAIN_ID)
    private final String domainId;

    @Nonnull
    @ColumnInfo(name = NAME)
    private final String name;

    @Nullable
    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @ColumnInfo(name = CONVERSION_FACTOR)
    private final double conversionFactor;

    @Nonnull
    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public IngredientEntity(@Nonnull String dataId,
                            @Nonnull String domainId,
                            @Nonnull String name,
                            @Nullable String description,
                            double conversionFactor,
                            @Nonnull String createdBy,
                            long createDate,
                            long lastUpdate) {
        this.dataId = dataId;
        this.domainId = domainId;
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
        IngredientEntity that = (IngredientEntity) o;
        return Double.compare(that.conversionFactor, conversionFactor) == 0 &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, name, description, conversionFactor, createdBy,
                createDate, lastUpdate);
    }

    @Override
    @Nonnull
    public String getDataId() {
        return dataId;
    }

    @Nonnull
    public String getDomainId() {
        return domainId;
    }

    @Nonnull
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
