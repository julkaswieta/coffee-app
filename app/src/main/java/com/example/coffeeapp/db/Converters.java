package com.example.coffeeapp.db;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalTime;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalTime fromTimestamp(String value) {
        return value == null ? null : LocalTime.parse(value);
    }

    @TypeConverter
    public static String dateToTimestamp(LocalTime date) {
        return date == null ? null : date.toString();
    }
}
