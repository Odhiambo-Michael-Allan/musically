package com.odesa.musically.services.media

enum class LoopMode {
    None,
    Queue,
    Song;

    companion object {
        val all = entries.toTypedArray()
    }
}

val allowedSpeedPitchValues = listOf( 0.5f, 1f, 1.5f, 2f, )