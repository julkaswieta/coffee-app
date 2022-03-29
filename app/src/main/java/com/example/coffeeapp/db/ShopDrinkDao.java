package com.example.coffeeapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShopDrinkDao {
    @Insert
    void insertShopDrink(ShopDrinkDB... shop_drinks);

    @Delete
    void deleteShopDrink(ShopDrinkDB shop_drinks);

    @Update
    void updateShopDrink(ShopDrinkDB... shop_drinks);

    @Query("SELECT COUNT(*) FROM shop_drinks")
    int getShopDrinksCount();

    @Query("SELECT * FROM shop_drinks")
    List<ShopDrinkDB> getAllShopDrinks();

    @Query("SELECT * FROM shop_drinks WHERE drinkId = :id")
    ShopDrinkDB getShopDrinkById(int id);

    @Query("SELECT MAX(drinkId) FROM shop_drinks")
    int getBiggestShopDrinkId();
}
