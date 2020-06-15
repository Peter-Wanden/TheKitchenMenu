package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;

import java.util.Objects;

import javax.annotation.Nonnull;


@Entity(tableName = RecipeCourseEntity.TABLE_RECIPE_COURSE_ITEM)
public final class RecipeCourseEntity implements EntityModel {

    public static final String TABLE_RECIPE_COURSE_ITEM = "recipeCourseItem";
    public static final String DATA_ID = "dataId";
    public static final String PARENT_DATA_ID = "parentDataId";
    public static final String RECIPE_COURSE_ID = "recipeCourseId";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @NonNull
    @ColumnInfo(name = PARENT_DATA_ID)
    private final String parentDataId;

    @ColumnInfo(name = RECIPE_COURSE_ID)
    private final int courseId;

    public RecipeCourseEntity(@Nonnull String dataId,
                              @NonNull String parentDataId,
                              int courseId) {
        this.dataId = dataId;
        this.parentDataId = parentDataId;
        this.courseId = courseId;
    }

    @Override
    @Nonnull
    public String getDataId() {
        return dataId;
    }

    @NonNull
    public String getParentDataId() {
        return parentDataId;
    }

    public int getCourseId() {
        return courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCourseEntity that = (RecipeCourseEntity) o;
        return dataId.equals(that.dataId) &&
                parentDataId.equals(that.parentDataId) &&
                courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                dataId, parentDataId, courseId);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseItemEntity{" +
                "dataId='" + dataId + '\'' +
                ", parentDataId=" + parentDataId + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}
