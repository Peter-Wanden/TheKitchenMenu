package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON)
public final class RecipeFailReasonEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE_FAIL_REASON = "recipeFailReason";
    public static final String DATA_ID = "dataId";
    public static final String PARENT_DATA_ID = "parentDataId";
    public static final String FAIL_REASON_ID = "failReason";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = PARENT_DATA_ID)
    private final String parentDataId;

    @ColumnInfo(name = FAIL_REASON_ID)
    private final int failReasonId;

    public RecipeFailReasonEntity(@NonNull String id,
                                  @Nonnull String parentDataId,
                                  int failReasonId) {
        this.id = id;
        this.parentDataId = parentDataId;
        this.failReasonId = failReasonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeFailReasonEntity that = (RecipeFailReasonEntity) o;
        return failReasonId == that.failReasonId &&
                id.equals(that.id) &&
                parentDataId.equals(that.parentDataId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentDataId, failReasonId);
    }

    @Override
    public String toString() {
        return "RecipeFailReasonEntity{" +
                "id='" + id + '\'' +
                ", parentDataId='" + parentDataId + '\'' +
                ", failReason=" + failReasonId +
                '}';
    }

    @NonNull
    public String getDataId() {
        return id;
    }

    @Nonnull
    public String getParentDataId() {
        return parentDataId;
    }

    public int getFailReasonId() {
        return failReasonId;
    }
}
