package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeIngredientEntity.TABLE_RECIPE_INGREDIENT)
public final class RecipeIngredientEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE_INGREDIENT = "recipeIngredient";
    public static final String DATA_ID = "dataId";
    public static final String RECIPE_INGREDIENT_ID = "recipeIngredientId";
    public static final String RECIPE_DATA_ID = "recipeDataId";
    public static final String RECIPE_DOMAIN_ID = "recipeDomainId";
    public static final String INGREDIENT_DATA_ID = "ingredientDataId";
    public static final String INGREDIENT_DOMAIN_ID = "ingredientDomainId";
    public static final String PRODUCT_DATA_ID = "productId";
    private static final String BASE_UNITS = "itemBaseUnits";
    private static final String UNIT_OF_MEASURE_SUBTYPE = "UnitOfMeasureSubType";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @Nonnull
    @ColumnInfo(name = RECIPE_INGREDIENT_ID)
    private final String recipeIngredientId;

    @Nonnull
    @ColumnInfo(name= RECIPE_DATA_ID)
    private final String recipeDataId;

    @Nonnull
    @ColumnInfo(name = RECIPE_DOMAIN_ID)
    private final String recipeDomainId;

    @Nonnull
    @ColumnInfo(name= INGREDIENT_DATA_ID)
    private final String ingredientDataId;

    @Nonnull
    @ColumnInfo(name = INGREDIENT_DOMAIN_ID)
    private final String ingredientDomainId;

    @Nonnull
    @ColumnInfo(name = PRODUCT_DATA_ID)
    private final String productDataId;

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

    public RecipeIngredientEntity(@Nonnull String dataId,
                                  @Nonnull String recipeIngredientId,
                                  @Nonnull String recipeDataId,
                                  @Nonnull String recipeDomainId,
                                  @Nonnull String ingredientDataId,
                                  @Nonnull String ingredientDomainId,
                                  @Nonnull String productDataId,
                                  double itemBaseUnits,
                                  int measurementSubtype,
                                  @Nonnull String createdBy,
                                  long createDate,
                                  long lastUpdate) {
        this.dataId = dataId;
        this.recipeIngredientId = recipeIngredientId;
        this.recipeDataId = recipeDataId;
        this.recipeDomainId = recipeDomainId;
        this.ingredientDataId = ingredientDataId;
        this.ingredientDomainId = ingredientDomainId;
        this.productDataId = productDataId;
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
        RecipeIngredientEntity that = (RecipeIngredientEntity) o;
        return Double.compare(that.itemBaseUnits, itemBaseUnits) == 0 &&
                measurementSubtype == that.measurementSubtype &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                dataId.equals(that.dataId) &&
                recipeIngredientId.equals(that.recipeIngredientId) &&
                recipeDataId.equals(that.recipeDataId) &&
                recipeDomainId.equals(that.recipeDomainId) &&
                ingredientDataId.equals(that.ingredientDataId) &&
                ingredientDomainId.equals(that.ingredientDomainId) &&
                productDataId.equals(that.productDataId) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, recipeIngredientId, recipeDataId, recipeDomainId,
                ingredientDataId, ingredientDomainId, productDataId, itemBaseUnits,
                measurementSubtype, createdBy, createDate, lastUpdate);
    }

    @Override
    @NonNull
    public String getDataId() {
        return dataId;
    }

    @Nonnull
    public String getRecipeIngredientId() {
        return recipeIngredientId;
    }

    @Nonnull
    public String getRecipeDataId() {
        return recipeDataId;
    }

    @Nonnull
    public String getRecipeDomainId() {
        return recipeDomainId;
    }

    @Nonnull
    public String getIngredientDataId() {
        return ingredientDataId;
    }

    @Nonnull
    public String getIngredientDomainId() {
        return ingredientDomainId;
    }

    @Nonnull
    public String getProductDataId() {
        return productDataId;
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
