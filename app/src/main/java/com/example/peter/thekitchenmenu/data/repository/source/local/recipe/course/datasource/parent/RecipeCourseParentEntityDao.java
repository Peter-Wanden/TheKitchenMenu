package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity.DATA_ID;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity.DOMAIN_ID;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity.TABLE_RECIPE_COURSE_PARENT;

@Dao
public interface RecipeCourseParentEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_PARENT)
    List<RecipeCourseParentEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_PARENT + " WHERE " + DATA_ID + " = :dataId")
    RecipeCourseParentEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_COURSE_PARENT + " WHERE " + DOMAIN_ID + " = :domainId")
    List<RecipeCourseParentEntity> getAllByDomainId(String domainId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseParentEntity e);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeCourseParentEntity... entities);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_PARENT + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_PARENT + " WHERE " + DOMAIN_ID + " = :domainId")
    void deleteAllByDomainId(String domainId);

    @Query("DELETE FROM " + TABLE_RECIPE_COURSE_PARENT)
    void deleteAll();
}
