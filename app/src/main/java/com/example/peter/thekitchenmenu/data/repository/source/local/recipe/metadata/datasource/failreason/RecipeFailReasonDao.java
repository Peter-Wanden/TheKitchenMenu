package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface RecipeFailReasonDao {

    @Query("SELECT * FROM " + RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON)
    List<RecipeFailReasonEntity> getAll();

    @Query("SELECT * FROM " + RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON + " WHERE " + RecipeFailReasonEntity.DATA_ID + " = :dataId")
    RecipeFailReasonEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON + " WHERE " + RecipeFailReasonEntity.PARENT_DATA_ID + " = :parentDataId")
    List<RecipeFailReasonEntity> getAllByParentDataId(String parentDataId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeFailReasonEntity e);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RecipeFailReasonEntity... entities);

    @Update
    void update(RecipeFailReasonEntity e);

    @Query("DELETE FROM " + RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON + " WHERE " + RecipeFailReasonEntity.DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON + " WHERE " + RecipeFailReasonEntity.PARENT_DATA_ID + " = :parentId")
    void deleteAllByParentDataId(String parentId);

    @Query("DELETE FROM " + RecipeFailReasonEntity.TABLE_RECIPE_FAIL_REASON)
    void deleteAll();
}
