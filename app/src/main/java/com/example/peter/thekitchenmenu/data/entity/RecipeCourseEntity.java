package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import static com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity.*;

@Entity(tableName = TABLE_RECIPE_COURSES)
public final class RecipeCourseEntity implements TkmEntity {

    public static final String TABLE_RECIPE_COURSES = "recipeCourses";
    public static final String RECIPE_COURSE_ENTRY_ID = "recipeCourseEntryId";
    public static final String RECIPE_ID = "recipeId";
    public static final String RECIPE_COURSE_NO = "recipeCourseNo";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = RECIPE_COURSE_ENTRY_ID)
    private final String id;

    @ColumnInfo(name = RECIPE_COURSE_NO)
    private final int courseNo;

    @NonNull
    @ColumnInfo(name = RECIPE_ID)
    private final String recipeId;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lasUpdate;

    public RecipeCourseEntity(@NonNull String id, int courseNo, @NonNull String recipeId,
                              long createDate, long lasUpdate) {
        this.id = id;
        this.courseNo = courseNo;
        this.recipeId = recipeId;
        this.createDate = createDate;
        this.lasUpdate = lasUpdate;
    }

    @Override
    @NonNull
    public String getId() {
        return id;
    }

    public int getCourseNo() {
        return courseNo;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
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
                createDate == that.createDate &&
                lasUpdate == that.lasUpdate &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseNo, recipeId, createDate, lasUpdate);
    }

    @Override
    public String toString() {
        return "RecipeCourseEntity{" +
                "id='" + id + '\'' +
                ", courseNo=" + courseNo +
                ", recipeId='" + recipeId + '\'' +
                ", createDate=" + createDate +
                ", lasUpdate=" + lasUpdate +
                '}';
    }
}
