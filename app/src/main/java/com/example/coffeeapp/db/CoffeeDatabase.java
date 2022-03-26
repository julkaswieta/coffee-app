package com.example.coffeeapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {RecipeDB.class, BeanDB.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CoffeeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract BeanDao beanDao();

    private static CoffeeDatabase INSTANCE;

    public static CoffeeDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CoffeeDatabase.class, "coffee_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
