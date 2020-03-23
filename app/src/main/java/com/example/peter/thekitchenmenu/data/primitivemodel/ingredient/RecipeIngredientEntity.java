package com.example.peter.thekitchenmenu.data.primitivemodel.ingredient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeIngredientEntity.TABLE_RECIPE_INGREDIENT)
public final class RecipeIngredientEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE_INGREDIENT = "recipeIngredient";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String INGREDIENT_ID = "ingredientId";
    public static final String PRODUCT_ID = "productId";
    private static final String BASE_UNITS = "itemBaseUnits";
    private static final String UNIT_OF_MEASURE_SUBTYPE = "UnitOfMeasureSubType";
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
    @ColumnInfo(name = INGREDIENT_ID)
    private final String ingredientId;

    @Nullable
    @ColumnInfo(name = PRODUCT_ID)
    private final String productId;

    @ColumnInfo(name = BASE_UNITS)
    private final double itemBaseUnits;

    @ColumnInfo(name = UNIT_OF_MEASURE_SUBTYPE)
    private final int measurementSubtype;

    @Nonnull
    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeIngredientEntity(@Nonnull String id,
                                  @Nonnull String recipeId,
                                  @Nonnull String ingredientId,
                                  @Nullable String productId,
                                  double itemBaseUnits,
                                  int measurementSubtype,
                                  @Nonnull String createdBy,
                                  long createDate,
                                  long lastUpdate) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.productId = productId;
        this.itemBaseUnits = itemBaseUnits;
        this.measurementSubtype = measurementSubtype;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientEntity entity = (RecipeIngredientEntity) o;
        return Double.compare(entity.itemBaseUnits, itemBaseUnits) == 0 &&
                measurementSubtype == entity.measurementSubtype &&
                createDate == entity.createDate &&
                lastUpdate == entity.lastUpdate &&
                id.equals(entity.id) &&
                recipeId.equals(entity.recipeId) &&
                ingredientId.equals(entity.ingredientId) &&
                productId.equals(entity.productId) &&
                createdBy.equals(entity.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                recipeId,
                ingredientId,
                productId,
                itemBaseUnits,
                measurementSubtype,
                createdBy,
                createDate,
                lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeIngredientEntity{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", productId='" + productId + '\'' +
                ", itemBaseUnits=" + itemBaseUnits +
                ", measurementSubtype=" + measurementSubtype +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public String getIngredientId() {
        return ingredientId;
    }

    @Nullable
    public String getProductId() {
        return productId;
    }

    public double getItemBaseUnits() {
        return itemBaseUnits;
    }

    public int getMeasurementSubtype() {
        return measurementSubtype;
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
