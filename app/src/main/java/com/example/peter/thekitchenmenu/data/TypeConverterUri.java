package com.example.peter.thekitchenmenu.data;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

public class TypeConverterUri {

    @TypeConverter
    public static Uri toUri(String stringUri) {
        return Uri.parse(stringUri);
    }

    @TypeConverter
    public static String toString(Uri uri) {
        return String.valueOf(uri);
    }
}
