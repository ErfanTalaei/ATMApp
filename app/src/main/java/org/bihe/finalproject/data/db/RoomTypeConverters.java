package org.bihe.finalproject.data.db;

import androidx.room.TypeConverter;

import java.util.Date;

public class RoomTypeConverters {

    @TypeConverter
    public static Long dateToLong(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date longToDate(Long value) {
        return new Date(value);
    }
}
