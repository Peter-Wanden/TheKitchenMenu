package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;

import java.util.Objects;

import javax.annotation.Nonnull;


@Entity(tableName = RecipeCourseItemEntity.TABLE_RECIPE_COURSE_ITEM)
public final class RecipeCourseItemEntity implements EntityModel {

    public static final String TABLE_RECIPE_COURSE_ITEM = "recipeCourseItem";
    public static final String DATA_ID = "dataId";
    public static final String PARENT_DATA_ID = "parentDataId";
    public static final String DOMAIN_ID = "recipeId";
    public static final String RECIPE_COURSE_ID = "recipeCourseId";
    public static final String IS_ACTIVE = "isActive";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @NonNull
    @ColumnInfo(name = PARENT_DATA_ID)
    private final String parentDataId;

    @Nonnull
    @ColumnInfo(name = DOMAIN_ID)
    private final String domainId;

    @ColumnInfo(name = RECIPE_COURSE_ID)
    private final int courseId;

    @ColumnInfo(name = IS_ACTIVE)
    private final boolean isActive;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lasUpdate;

    public RecipeCourseItemEntity(@Nonnull String dataId,
                                  @NonNull String parentDataId,
                                  @Nonnull String domainId,
                                  int courseId,
                                  boolean isActive,
                                  long createDate,
                                  long lasUpdate) {
        this.dataId = dataId;
        this.parentDataId = parentDataId;
        this.domainId = domainId;
        this.courseId = courseId;
        this.isActive = isActive;
        this.createDate = createDate;
        this.lasUpdate = lasUpdate;
    }

    @Override
    @Nonnull
    public String getDataId() {
        return dataId;
    }

    @Nonnull
    public String getDomainId() {
        return domainId;
    }

    @NonNull
    public String getParentDataId() {
        return parentDataId;
    }

    public int getCourseId() {
        return courseId;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLasUpdate() {
        return lasUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCourseItemEntity that = (RecipeCourseItemEntity) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                parentDataId.equals(that.parentDataId) &&
                courseId == that.courseId &&
                isActive == that.isActive &&
                createDate == that.createDate &&
                lasUpdate == that.lasUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                dataId, domainId, parentDataId, courseId,
                isActive, createDate, lasUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseItemEntity{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", parentDataId=" + parentDataId + '\'' +
                ", courseId=" + courseId +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                ", lasUpdate=" + lasUpdate +
                '}';
    }
}
