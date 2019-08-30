package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity.*;

@Dao
public interface RecipeCourseEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSES)
    List<RecipeCourseEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSES + " WHERE " + RECIPE_COURSE_ENTRY_ID + " = :id")
    RecipeCourseEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSES + " WHERE " + RECIPE_COURSE_NO + " = :courseNo")
    List<RecipeCourseEntity> getAllRecipesForCourseNo(int courseNo);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSES + " WHERE " + RECIPE_ID + " = :recipeId")
    List<RecipeCourseEntity> getCoursesForRecipe(String recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseEntity recipeCourseEntity);

    @Update
    void update(RecipeCourseEntity recipeCourseEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSES + " WHERE " + RECIPE_COURSE_ENTRY_ID + " = :courseId")
    void deleteByCourseId(String courseId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSES)
    void deleteAll();
}
