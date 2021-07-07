package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity.DATA_ID;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity.PARENT_DATA_ID;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity.RECIPE_COURSE_ID;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity.TABLE_RECIPE_COURSE_ITEM;

@Dao
public interface RecipeCourseItemDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM)
    List<RecipeCourseEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + DATA_ID + " = :dataId")
    RecipeCourseEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + PARENT_DATA_ID + " = :parentDataId")
    List<RecipeCourseEntity> getAllByParentDataId(String parentDataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + RECIPE_COURSE_ID + " = :courseNo")
    List<RecipeCourseEntity> getAllByCourseNo(int courseNo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseEntity recipeCourseEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseEntity... recipeCourseEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + PARENT_DATA_ID + " = :parentDataId")
    void deleteAllByParentDataId(String parentDataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_ITEM)
    void deleteAll();
}
