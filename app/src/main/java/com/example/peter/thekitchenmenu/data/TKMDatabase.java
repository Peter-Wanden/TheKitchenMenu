package com.example.peter.thekitchenmenu.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.model.Ingredient;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.model.Recipe;

/* Creates the database and keeps a singleton instance available
 * Add any POJO classes here to add more database tables
 */
@Database(entities = {
        Product.class,
        Recipe.class,
        Ingredient.class},
        version = 1,
        exportSchema = false)
public abstract class TKMDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "TKMDatabase";
    private static TKMDatabase sInstance;

    public static TKMDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder
                        (context.getApplicationContext(),
                                TKMDatabase.class,
                                TKMDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    /* Method that returns the product DAO */
    public abstract ProductDAO getProductDao();

    /* Method that returns the recipe DAO */
    public abstract RecipeDAO getRecipeDao();

    /* Method that returns the ingredient DAO */
    public abstract IngredientDAO getIngredientDao();

    /* Method that returns the ingredients and their associated product information */
    public abstract IngredientAndProductDAO getIngredientsAndProducts();

}
