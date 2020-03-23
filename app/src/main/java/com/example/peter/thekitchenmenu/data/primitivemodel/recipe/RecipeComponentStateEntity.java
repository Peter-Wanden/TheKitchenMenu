package com.example.peter.thekitchenmenu.data.primitivemodel.recipe;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeComponentStateEntity.TABLE_RECIPE_COMPONENT_STATE)
public final class RecipeComponentStateEntity {

    public static final String TABLE_RECIPE_COMPONENT_STATE = "recipeComponentState";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String COMPONENT_NAME = "componentName";
    public static final String COMPONENT_STATE = "componentState";

    @PrimaryKey
    @Nonnull
    @ColumnInfo(name = ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @ColumnInfo(name = COMPONENT_NAME)
    private final int componentName;

    @ColumnInfo(name = COMPONENT_STATE)
    private final int componentState;

    public RecipeComponentStateEntity(@Nonnull String id,
                                      @Nonnull String recipeId,
                                      int componentName,
                                      int componentState) {
        this.id = id;
        this.recipeId = recipeId;
        this.componentName = componentName;
        this.componentState = componentState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentStateEntity that = (RecipeComponentStateEntity) o;
        return componentName == that.componentName &&
                componentState == that.componentState &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, componentName, componentState);
    }

    @Override
    public String toString() {
        return "RecipeComponentStateEntity{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", componentName=" + componentName +
                ", componentState=" + componentState +
                '}';
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    public int getComponentName() {
        return componentName;
    }

    public int getComponentState() {
        return componentState;
    }
}
