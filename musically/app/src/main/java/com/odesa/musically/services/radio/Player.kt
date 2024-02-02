package com.odesa.musically.services.radio

data class PlaybackPosition(
    val played: Long,
    val total: Long,
) {
    val ratio: Float
        get() = ( played.toFloat() / total ).takeIf { it.isFinite() } ?: 0f

    companion object {
        val zero = PlaybackPosition( 0L, 0L )
    }
}