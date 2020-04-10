package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity.*;

@Dao
public interface RecipeComponentStateDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_COMPONENT_STATE)
    List<RecipeComponentStateEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + DATA_ID + " = :dataId")
    RecipeComponentStateEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + PARENT_DATA_ID + " = :parentDataId")
    List<RecipeComponentStateEntity> getAllByParentDataId(String parentDataId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeComponentStateEntity e);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeComponentStateEntity... entities);

    @Update
    void update(RecipeComponentStateEntity e);

    @Query("DELETE FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + PARENT_DATA_ID + " = :parentDataId")
    void deleteAllByParentDataId(String parentDataId);

    @Query("DELETE FROM " + TABLE_RECIPE_COMPONENT_STATE)
    void deleteAll();
}
