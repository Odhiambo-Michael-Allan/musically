package com.odesa.musically.utils

fun Float.toSafeFinite() = if ( !isFinite() ) 0f else this