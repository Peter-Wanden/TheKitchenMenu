package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipePortionsEntity.TABLE_RECIPE_PORTIONS)
public final class RecipePortionsEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE_PORTIONS = "recipePortions";
    public static final String DATA_ID = "dataId";
    public static final String DOMAIN_ID = "domainId";
    public static final String SERVINGS = "servings";
    public static final String SITTINGS = "sittings";
    public static final String CREATE_DATE = "createDate";
    public static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @Nonnull
    @ColumnInfo(name = DOMAIN_ID)
    private final String domainId;

    @ColumnInfo(name = SERVINGS)
    private final int servings;

    @ColumnInfo(name = SITTINGS)
    private final int sittings;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipePortionsEntity(@Nonnull String dataId,
                                @Nonnull String domainId,
                                int servings,
                                int sittings,
                                long createDate,
                                long lastUpdate) {
        this.dataId = dataId;
        this.domainId = domainId;
        this.servings = servings;
        this.sittings = sittings;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePortionsEntity that = (RecipePortionsEntity) o;
        return servings == that.servings &&
                sittings == that.sittings &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                dataId.equals(that.dataId) &&
                domainId.equals(that.domainId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, servings, sittings, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsEntity{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", servings=" + servings +
                ", sittings=" + sittings +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
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

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
