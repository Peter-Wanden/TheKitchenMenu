package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.entitymodel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON)
public final class RecipeFailReasonEntity {

    public static final String TABLE_RECIPE_FAIL_REASON = "recipeFailReason";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String FAIL_REASON = "failReason";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @ColumnInfo(name = FAIL_REASON)
    private final int failReason;

    public RecipeFailReasonEntity(@NonNull String id,
                                  @Nonnull String recipeId,
                                  int failReason) {
        this.id = id;
        this.recipeId = recipeId;
        this.failReason = failReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeFailReasonEntity that = (RecipeFailReasonEntity) o;
        return failReason == that.failReason &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, failReason);
    }

    @Override
    public String toString() {
        return "RecipeFailReasonEntity{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", failReason=" + failReason +
                '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    public int getFailReason() {
        return failReason;
    }
}
