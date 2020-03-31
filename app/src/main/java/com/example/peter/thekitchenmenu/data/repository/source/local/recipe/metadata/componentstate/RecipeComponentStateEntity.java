package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeComponentStateEntity.TABLE_RECIPE_COMPONENT_STATE)
public final class RecipeComponentStateEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE_COMPONENT_STATE = "recipeComponentState";
    public static final String DATA_ID = "dataId";
    public static final String PARENT_DATA_ID = "parentDataId";
    public static final String COMPONENT_ID = "componentId";
    public static final String COMPONENT_STATE_ID = "componentStateId";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @Nonnull
    @ColumnInfo(name = PARENT_DATA_ID)
    private final String parentDataId;

    @ColumnInfo(name = COMPONENT_ID)
    private final int componentId;

    @ColumnInfo(name = COMPONENT_STATE_ID)
    private final int componentStateId;

    public RecipeComponentStateEntity(@Nonnull String dataId,
                                      @Nonnull String parentDataId,
                                      int componentId,
                                      int componentStateId) {
        this.dataId = dataId;
        this.parentDataId = parentDataId;
        this.componentId = componentId;
        this.componentStateId = componentStateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentStateEntity that = (RecipeComponentStateEntity) o;
        return dataId.equals(that.dataId) &&
                parentDataId.equals(that.parentDataId) &&
                componentId == that.componentId &&
                componentStateId == that.componentStateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, parentDataId, componentId, componentStateId);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeComponentStateEntity{" +
                "id='" + dataId + '\'' +
                ", parentDataId='" + parentDataId + '\'' +
                ", componentId=" + componentId +
                ", componentStateLevel=" + componentStateId +
                '}';
    }

    @Override
    @Nonnull
    public String getDataId() {
        return dataId;
    }

    @Nonnull
    public String getParentDataId() {
        return parentDataId;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getComponentStateId() {
        return componentStateId;
    }
}
