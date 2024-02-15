package com.odesa.musically.services.media.extensions

import android.database.Cursor
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull

fun Cursor.getNullableLongFrom(columnName: String ): Long? {
    val columnIndex = getColumnIndex( columnName )
    return getLongOrNull( columnIndex )
}

fun Cursor.getNullableStringFrom( columnName: String ): String? {
    val columnIndex = getColumnIndex( columnName )
    return getStringOrNull( columnIndex )
}