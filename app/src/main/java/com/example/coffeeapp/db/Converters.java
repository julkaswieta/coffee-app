package com.example.coffeeapp.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
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

    @TypeConverter
    public static byte[] bitmapToByteArray(Bitmap photo) {
        if(photo != null) {
            Bitmap bmp = photo;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
        else {
            return null;
        }
    }

    @TypeConverter
    public static Bitmap byteArrayToBitmap(byte[] array) {
        if(array != null) {
            return BitmapFactory.decodeByteArray(array, 0, array.length);
        }
        else {
            return null;
        }
    }
}
