package com.odesa.musicMatters.utils

import org.json.JSONArray
import org.json.JSONObject

fun <T> JSONArray.toList( fn: JSONArray.( Int ) -> T ) = List( length() ) {
    i -> fn.invoke( this, i )
}

fun JSONObject.getIntOrNull( key: String ) : Int? = if ( has( key ) ) getInt( key ) else null

fun JSONObject.getStringOrNull( key: String ) : String? = if ( has( key ) ) getString( key ) else null