package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity.DATA_ID;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity.RECIPE_ID;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity.TABLE_RECIPE;

@Dao
public interface RecipeMetadataParentEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE)
    List<RecipeMetadataParentEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE + " WHERE " + DATA_ID + " = :dataId")
    RecipeMetadataParentEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE_RECIPE + " WHERE " + RECIPE_ID + " = :recipeId")
    List<RecipeMetadataParentEntity> getAllByDomainId(String recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeMetadataParentEntity e);

    @Update
    void update(RecipeMetadataParentEntity e);

    @Query("DELETE FROM " + TABLE_RECIPE + " WHERE " + RECIPE_ID + " = :domainId")
    void deleteAllByDomainId(String domainId);

    @Query("DELETE FROM " + TABLE_RECIPE + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_RECIPE)
    void deleteAll();
}
