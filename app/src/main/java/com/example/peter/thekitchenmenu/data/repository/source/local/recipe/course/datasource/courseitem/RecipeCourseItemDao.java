package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemEntity.*;

@Dao
public interface RecipeCourseItemDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM)
    List<RecipeCourseItemEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + DATA_ID + " = :dataId")
    RecipeCourseItemEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + PARENT_DATA_ID + " = :parentDataId")
    List<RecipeCourseItemEntity> getAllByParentDataId(String parentDataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + RECIPE_COURSE_ID + " = :courseNo")
    List<RecipeCourseItemEntity> getAllByCourseNo(int courseNo);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + DOMAIN_ID + " = :domainId")
    List<RecipeCourseItemEntity> getAllByDomainId(String domainId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseItemEntity recipeCourseItemEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseItemEntity... recipeCourseItemEntity);

    @Update
    void update(RecipeCourseItemEntity recipeCourseItemEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_ITEM + " WHERE " + PARENT_DATA_ID + " = :parentDataId")
    void deleteAllByParentDataId(String parentDataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_ITEM)
    void deleteAll();
}
