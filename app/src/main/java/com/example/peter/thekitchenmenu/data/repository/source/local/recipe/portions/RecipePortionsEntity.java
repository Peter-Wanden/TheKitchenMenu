package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions;

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
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String SERVINGS = "servings";
    public static final String SITTINGS = "sittings";
    public static final String CREATE_DATE = "createDate";
    public static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @ColumnInfo(name = SERVINGS)
    private final int servings;

    @ColumnInfo(name = SITTINGS)
    private final int sittings;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipePortionsEntity(@Nonnull String id,
                                @Nonnull String recipeId,
                                int servings,
                                int sittings,
                                long createDate,
                                long lastUpdate) {
        this.id = id;
        this.recipeId = recipeId;
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
                id.equals(that.id) &&
                recipeId.equals(that.recipeId);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsEntity{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", servings=" + servings +
                ", sittings=" + sittings +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                recipeId,
                servings,
                sittings,
                createDate,
                lastUpdate);
    }

    @Nonnull
    @Override
    public String getDataId() {
        return id;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
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
