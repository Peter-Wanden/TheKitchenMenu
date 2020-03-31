package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity.*;


@Dao
public interface RecipeFailReasonEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_FAIL_REASON)
    List<RecipeFailReasonEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + DATA_ID + " = :dataId")
    RecipeFailReasonEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + PARENT_DATA_ID + " = :parentDataId")
    List<RecipeFailReasonEntity> getAllByParentDataId(String parentDataId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeFailReasonEntity e);

    @Update
    void update(RecipeFailReasonEntity e);

    @Query("DELETE FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + PARENT_DATA_ID + " = :parentId")
    void deleteAllByParentDataId(String parentId);

    @Query("DELETE FROM " + TABLE_RECIPE_FAIL_REASON)
    void deleteAll();
}
