package com.example.coffeeapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BeanDao {
    @Insert
    void insertBean(BeanDB... beans);

    @Delete
    void deleteBean(BeanDB beanDB);

    @Update
    void updateBean(BeanDB... beans);

    @Query("SELECT COUNT(*) FROM beans")
    int getBeansCount();

    @Query("SELECT * FROM beans")
    List<BeanDB> getAllBeans();

    @Query("SELECT * FROM beans WHERE beansId = :id")
    BeanDB getBeansById(int id);

    @Query("SELECT MAX(beansId) FROM beans")
    int getBiggestBeansId();
}
