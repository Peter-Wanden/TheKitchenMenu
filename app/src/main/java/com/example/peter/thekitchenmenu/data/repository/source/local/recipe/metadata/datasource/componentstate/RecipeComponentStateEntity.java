package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate;

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
    public static final String COMPONENT_NAME_ID = "componentId";
    public static final String COMPONENT_STATE_ID = "componentStateId";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = PARENT_DATA_ID)
    private final String parentDataId;

    @ColumnInfo(name = COMPONENT_NAME_ID)
    private final int componentNameId;

    @ColumnInfo(name = COMPONENT_STATE_ID)
    private final int componentStateId;

    public RecipeComponentStateEntity(@Nonnull String id,
                                      @Nonnull String parentDataId,
                                      int componentNameId,
                                      int componentStateId) {
        this.id = id;
        this.parentDataId = parentDataId;
        this.componentNameId = componentNameId;
        this.componentStateId = componentStateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentStateEntity that = (RecipeComponentStateEntity) o;
        return id.equals(that.id) &&
                parentDataId.equals(that.parentDataId) &&
                componentNameId == that.componentNameId &&
                componentStateId == that.componentStateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentDataId, componentNameId, componentStateId);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeComponentStateEntity{" +
                "id='" + id + '\'' +
                ", parentDataId='" + parentDataId + '\'' +
                ", componentNameId=" + componentNameId +
                ", componentStateLevel=" + componentStateId +
                '}';
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getParentDataId() {
        return parentDataId;
    }

    public int getComponentNameId() {
        return componentNameId;
    }

    public int getComponentStateId() {
        return componentStateId;
    }
}