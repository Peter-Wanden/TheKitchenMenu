package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity.*;

@Dao
public interface RecipeIdentityEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_IDENTITY + " WHERE " + DATA_ID + " = :dataId")
    RecipeIdentityEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE_IDENTITY + " WHERE " + DOMAIN_ID + " = :domainId")
    List<RecipeIdentityEntity> getAllByDomainId(String domainId);

    @Query("SELECT * FROM " + TABLE_RECIPE_IDENTITY + " ORDER BY " + TITLE)
    List<RecipeIdentityEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeIdentityEntity recipeIdentityEntity);

    @Update
    void update(RecipeIdentityEntity recipeIdentityEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_IDENTITY + " WHERE " + DATA_ID + " = :dataId")
    void deleteById(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE_IDENTITY + " WHERE " + DOMAIN_ID + " = :domainId")
    void deleteAllByDomainId(String domainId);

    @Query("DELETE FROM " + TABLE_RECIPE_IDENTITY)
    void deleteAll();
}
