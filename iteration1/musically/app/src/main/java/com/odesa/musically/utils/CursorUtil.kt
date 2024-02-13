package com.odesa.musically.utils

import android.database.Cursor

data class CursorUtil(
    val cursor: Cursor,
    val indices: Map<String, Int>
) {
    fun getIntFrom( column: String ) = cursor.getInt( indices[ column ]!! )
    fun getLongFrom( column: String ) = cursor.getLong( indices[ column ]!! )
    fun getStringFrom(column: String ) = cursor.getString( indices[ column ]!! )

    fun getIntNullableFrom( column: String ): Int? {
        val index = indices[ column ]!!
        return if ( index > -1 ) cursor.getInt( index ) else null
    }

    fun getStringNullableFrom(column: String ): String? {
        val index = indices[ column ]!!
        return if ( index > -1 ) cursor.getString( index ) else null
    }
}

fun Cursor.getColumnIndices( columns: List<String> ) = columns.associateWith {
    getColumnIndex( it )
}