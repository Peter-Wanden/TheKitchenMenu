package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity.*;

@Entity(tableName = TABLE_RECIPE_COURSE)
public final class RecipeCourseEntity implements PrimitiveModel {

    public static final String TABLE_RECIPE_COURSE = "recipeCourse";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String RECIPE_COURSE_NO = "recipeCourseNo";
    public static final String IS_ACTIVE = "isActive";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @Nonnull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @ColumnInfo(name = RECIPE_COURSE_NO)
    private final int courseNo;

    @ColumnInfo(name = IS_ACTIVE)
    private final boolean isActive;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lasUpdate;

    public RecipeCourseEntity(@Nonnull String id,
                              @Nonnull String recipeId,
                              int courseNo,
                              boolean isActive,
                              long createDate,
                              long lasUpdate) {
        this.id = id;
        this.recipeId = recipeId;
        this.courseNo = courseNo;
        this.isActive = isActive;
        this.createDate = createDate;
        this.lasUpdate = lasUpdate;
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

    public int getCourseNo() {
        return courseNo;
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
        RecipeCourseEntity that = (RecipeCourseEntity) o;
        return courseNo == that.courseNo &&
                isActive == that.isActive &&
                createDate == that.createDate &&
                lasUpdate == that.lasUpdate &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, courseNo, isActive, createDate, lasUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseEntity{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", courseNo=" + courseNo +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                ", lasUpdate=" + lasUpdate +
                '}';
    }
}
