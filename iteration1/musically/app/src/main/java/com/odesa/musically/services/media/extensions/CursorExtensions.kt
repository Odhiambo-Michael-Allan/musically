package com.odesa.musically.services.media.extensions

import android.database.Cursor
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull

fun Cursor.getLongFrom( columnName: String ): Long {
    val columnIndex = getColumnIndex( columnName )
    return getLong( columnIndex )
}

fun Cursor.getLongNullableFrom( columnName: String ): Long? {
    val columnIndex = getColumnIndex( columnName )
    return getLongOrNull( columnIndex )
}

fun Cursor.getStringFrom( columnName: String ): String {
    val columnIndex = getColumnIndex( columnName )
    return getString( columnIndex )
}

fun Cursor.getStringNullableFrom( columnName: String ): String? {
    val columnIndex = getColumnIndex( columnName )
    return getStringOrNull( columnIndex )
}