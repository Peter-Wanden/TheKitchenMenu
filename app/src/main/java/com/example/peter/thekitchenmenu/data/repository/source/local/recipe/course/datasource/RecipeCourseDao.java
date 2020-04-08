package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity.*;

@Dao
public interface RecipeCourseDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE)
    List<RecipeCourseEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE + " WHERE " + ID + " = :dataId")
    RecipeCourseEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE + " WHERE " + RECIPE_COURSE_NO + " = :courseNo")
    List<RecipeCourseEntity> getAllByCourseNo(int courseNo);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE + " WHERE " + RECIPE_ID + " = :recipeId")
    List<RecipeCourseEntity> getAllByRecipeId(String recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseEntity recipeCourseEntity);

    @Update
    void update(RecipeCourseEntity recipeCourseEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE + " WHERE " + ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE + " WHERE " + RECIPE_ID + " = :domainId")
    void deleteAllByDomainId(String domainId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE)
    void deleteAll();
}
