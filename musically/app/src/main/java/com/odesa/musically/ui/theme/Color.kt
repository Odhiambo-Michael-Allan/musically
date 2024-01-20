package com.odesa.musically.ui.theme

import androidx.compose.ui.graphics.Color


enum class PrimaryThemeColors {
    Red,
    Orange,
    Amber,
    Yellow,
    Lime,
    Green,
    Emerald,
    Teal,
    Cyan,
    Sky,
    Blue,
    Indigo,
    Violet,
    Purple,
    Fuchsia,
    Pink,
    Rose;

    fun toHumanString() = name
        .split( "_" )
        .joinToString( " " ) {
            it[0].uppercase() + it.substring( 1 ).lowercase()
        }
}

// --------------------------------------------------------------

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)