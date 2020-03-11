package com.dwilliams.moviesphasetwo.persistence;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {

    //ROOM uses this method when reading from Database
    @TypeConverter
    public static Date toDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }

    //ROOM uses this method when wrting into Database
    @TypeConverter
    public static Long toTimestamp(Date date){ return date == null ? null : date.getTime(); }
}
