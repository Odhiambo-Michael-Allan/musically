package com.odesa.musically.data.storage.database.impl

import android.content.Context
import com.odesa.musically.data.storage.database.MusicDatabase
import com.odesa.musically.services.audio.Song
import com.odesa.musically.services.audio.SongsRetriever

class MusicDatabaseImpl( context: Context ) : MusicDatabase {

    private val songsRetriever = SongsRetriever( context, setOf( "" ) )

    override val songs: List<Song> by lazy { songsRetriever.fetch() }

}