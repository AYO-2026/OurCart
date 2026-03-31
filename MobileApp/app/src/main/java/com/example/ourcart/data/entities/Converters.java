package com.example.ourcart.data.entities;
import androidx.room.TypeConverter;
public class Converters {

    @TypeConverter
    public static Unit fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Unit.valueOf(value);
        } catch (IllegalArgumentException e) {
            return Unit.PIECE;
        }
    }

    @TypeConverter
    public static String unitToString(Unit unit) {
        if (unit == null) {
            return null;
        }
        return unit.name();
    }
}