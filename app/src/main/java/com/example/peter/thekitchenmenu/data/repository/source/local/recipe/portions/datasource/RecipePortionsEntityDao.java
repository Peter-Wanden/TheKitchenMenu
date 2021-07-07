package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity.*;

@Dao
public interface RecipePortionsEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_PORTIONS + " WHERE " + DATA_ID + " = :dataId")
    RecipePortionsEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_PORTIONS + " WHERE " + DOMAIN_ID + " = :domainId")
    List<RecipePortionsEntity> getByDomainId(String domainId);

    @Query("SELECT * FROM " + TABLE_RECIPE_PORTIONS)
    List<RecipePortionsEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipePortionsEntity portionsEntity);

    @Update
    void update(RecipePortionsEntity portionsEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_PORTIONS + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_PORTIONS)
    void deleteAll();
}
