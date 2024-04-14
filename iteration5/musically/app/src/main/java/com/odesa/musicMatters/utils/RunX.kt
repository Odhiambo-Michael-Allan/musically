package com.odesa.musicMatters.utils

fun <T> runFunctionIfTrueElseReturnDefaultValue(
    run: Boolean,
    defaultValue: T,
    fn: () -> T
) = when {
    run -> fn()
    else -> defaultValue
}

fun <T> T.runFunctionIfTrueElseReturnThisObject(
    run: Boolean,
    fn: T.() -> T
) = when {
    run -> fn.invoke( this )
    else -> this
}