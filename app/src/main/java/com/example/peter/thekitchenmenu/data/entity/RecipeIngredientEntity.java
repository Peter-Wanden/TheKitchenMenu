package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = RecipeIngredientEntity.TABLE_RECIPE_INGREDIENT)
public final class RecipeIngredientEntity implements TkmEntity {

    public static final String TABLE_RECIPE_INGREDIENT = "recipeIngredient";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String INGREDIENT_ID = "ingredientId";
    public static final String PRODUCT_ID = "productId";
    private static final String BASE_UNITS = "baseUnits";
    private static final String UNIT_OF_MEASURE_SUBTYPE = "UnitOfMeasureSubType";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @NonNull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @NonNull
    @ColumnInfo(name = INGREDIENT_ID)
    private final String ingredientId;

    @Nullable
    @ColumnInfo(name = PRODUCT_ID)
    private final String productId;

    @ColumnInfo(name = BASE_UNITS)
    private final double baseUnits;

    @ColumnInfo(name = UNIT_OF_MEASURE_SUBTYPE)
    private final int unitOfMeasureSubtype;

    public RecipeIngredientEntity(@NonNull String id,
                                  @NonNull String recipeId,
                                  @NonNull String ingredientId,
                                  @Nullable String productId,
                                  double baseUnits,
                                  int unitOfMeasureSubtype) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.productId = productId;
        this.baseUnits = baseUnits;
        this.unitOfMeasureSubtype = unitOfMeasureSubtype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientEntity that = (RecipeIngredientEntity) o;
        return Double.compare(that.baseUnits, baseUnits) == 0 &&
                unitOfMeasureSubtype == that.unitOfMeasureSubtype &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId) &&
                ingredientId.equals(that.ingredientId) &&
                productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, ingredientId, productId, baseUnits, unitOfMeasureSubtype);
    }

    @Override
    public String toString() {
        return "RecipeIngredientEntity{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", productId='" + productId + '\'' +
                ", baseUnits=" + baseUnits +
                ", unitOfMeasureSubtype=" + unitOfMeasureSubtype +
                '}';
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    @NonNull
    public String getIngredientId() {
        return ingredientId;
    }

    @Nullable
    public String getProductId() {
        return productId;
    }

    public double getBaseUnits() {
        return baseUnits;
    }

    public int getUnitOfMeasureSubtype() {
        return unitOfMeasureSubtype;
    }
}
