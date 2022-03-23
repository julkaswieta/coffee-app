package com.example.coffeeapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {RecipeDB.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RecipesDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();

    private static RecipesDatabase INSTANCE;

    public static RecipesDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecipesDatabase.class, "recipes_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
