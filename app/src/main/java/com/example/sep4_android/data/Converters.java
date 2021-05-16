package com.example.sep4_android.data;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class Converters {
    @TypeConverter
    public static LocalDate fromTimestamp(String value) {
        return LocalDate.parse(value);
    }

    @TypeConverter
    public static String dateToTimeStamp(LocalDate date) {
        return date.toString();
    }
}
