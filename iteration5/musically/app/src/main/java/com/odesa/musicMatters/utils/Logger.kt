package com.odesa.musicMatters.utils

import android.util.Log

object Logger {
    private const val tag = "MUSICALLY"

    fun warn( mod: String, message: String ) = Log.w( tag, "$mod: $message" )
    fun warn( mod: String, message: String, throwable: Throwable ) =
        warn( mod, joinTextThrowable( message, throwable ) )
    fun error( mod: String, message: String ) = Log.e( tag, "$mod: $message" )
    fun error( mod: String, message: String, throwable: Throwable ) =
        error( mod, joinTextThrowable( message, throwable ) )

    private fun joinTextThrowable( text: String, throwable: Throwable ) = StringBuilder().apply {
        append( text )
        append( "\nError: ${throwable.message}" )
        append( "\nStack trace: ${throwable.stackTraceToString()}" )
    }.toString()
}