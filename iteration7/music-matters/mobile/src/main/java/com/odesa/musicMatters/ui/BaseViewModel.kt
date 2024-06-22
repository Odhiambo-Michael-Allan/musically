package com.odesa.musicMatters.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.model.Album
import com.odesa.musicMatters.core.model.Artist
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.utils.FuzzySearchOption
import com.odesa.musicMatters.utils.FuzzySearcher
import kotlinx.coroutines.launch
import java.util.UUID

open class BaseViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val songsFuzzySearcher: FuzzySearcher<String> = FuzzySearcher(
        options = listOf(
            FuzzySearchOption( { v -> getSongWithId( v )?.title?.let { compareString( it ) } }, 3 ),
            FuzzySearchOption( { v -> getSongWithId( v )?.path?.let { compareString( it ) } }, 2 ),
            FuzzySearchOption( { v -> getSongWithId( v )?.artists?.let { compareCollection( it ) } } ),
            FuzzySearchOption( { v -> getSongWithId( v )?.albumTitle?.let { compareString( it ) } } )
        )
    )

    private val playlistsChangeListeners: MutableList<( List<Playlist> )->Unit> = mutableListOf()
    private val sortSongsByChangeListeners: MutableList<( SortSongsBy, Boolean )->Unit> = mutableListOf()

    init {
        viewModelScope.launch { observePlaylists() }
        viewModelScope.launch { observeSortSongsBy() }
        viewModelScope.launch { observeSortSongsInReverse() }
    }

    private suspend fun observePlaylists() {
        playlistRepository.playlists.collect {
            playlistsChangeListeners.forEach { listener ->
                listener.invoke( getEditablePlaylists() )
            }
        }
    }

    private suspend fun observeSortSongsBy() {
        settingsRepository.sortSongsBy.collect { sortSongsBy ->
            sortSongsByChangeListeners.forEach { listener ->
                listener.invoke( sortSongsBy, settingsRepository.sortSongsInReverse.value )
            }
        }
    }

    private suspend fun observeSortSongsInReverse() {
        settingsRepository.sortSongsInReverse.collect { sortSongsInReverse ->
            sortSongsByChangeListeners.forEach { listener ->
                listener.invoke( settingsRepository.sortSongsBy.value, sortSongsInReverse )
            }
        }
    }

    fun addOnPlaylistsChangeListener( listener: ( List<Playlist>) -> Unit ) {
        playlistsChangeListeners.add( listener )
        // Supply the newly added listener with the currently editable playlists..
        listener.invoke( getEditablePlaylists() )
    }

    fun addOnSortSongsByChangeListener( listener: ( SortSongsBy, Boolean ) -> Unit ) {
        sortSongsByChangeListeners.add( listener )
    }

    fun setSortSongsBy( sortSongsBy: SortSongsBy ) {
        viewModelScope.launch { settingsRepository.setSortSongsBy( sortSongsBy ) }
    }

    fun setSortSongsInReverse( reverse: Boolean ) {
        viewModelScope.launch { settingsRepository.setSortSongsInReverse( reverse ) }
    }

    fun isPlaylistDeletable( playlist: Playlist ) = getDeletablePlaylists().contains( playlist )

    private fun getDeletablePlaylists(): List<Playlist> =
        playlistRepository.playlists.value.filter {
            it.id != playlistRepository.mostPlayedSongsPlaylist.value.id &&
                    it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id &&
                    it.id != playlistRepository.favoritesPlaylist.value.id
        }

    fun renamePlaylist( playlist: Playlist, newName: String ) {
        viewModelScope.launch {
            playlistRepository.renamePlaylist( playlist, newName )
        }
    }

    fun deletePlaylist( playlist: Playlist ) {
        viewModelScope.launch { playlistRepository.deletePlaylist( playlist ) }
    }

    fun addToFavorites( songId: String ) {
        viewModelScope.launch { playlistRepository.addToFavorites( songId ) }
    }

    fun addSongsToPlaylist(
        playlist: Playlist,
        songs: List<Song>
    ) {
        viewModelScope.launch {
            songs.forEach {
                playlistRepository.addSongIdToPlaylist( it.id, playlist.id )
            }
        }
    }

    fun playSongs(
        selectedSong: Song,
        songsInPlaylist: List<Song>
    ) {
        musicServiceConnection.playMediaItem(
            mediaItem = selectedSong.mediaItem,
            mediaItems = songsInPlaylist.map { it.mediaItem },
            shuffle = settingsRepository.shuffle.value
        )
    }

    fun shufflePlaySongsInAlbum( album: Album ) {
        shuffleAndPlay(
            songs = getSongsInAlbum( album )
        )
    }

    fun shufflePlaySongsByArtist( artist: Artist ) {
        shuffleAndPlay(
            songs = getSongsByArtist( artist )
        )
    }

    fun shuffleAndPlay(
        songs: List<Song>,
    ) {
        if ( songs.isEmpty() ) return
        musicServiceConnection.shuffleAndPlay(
            songs.map { it.mediaItem }
        )
    }

    fun playSong( song: Song ) {
        musicServiceConnection.playMediaItem(
            mediaItem = song.mediaItem,
            mediaItems = listOf( song.mediaItem ),
            shuffle = false // Its only one, no need to shuffle..
        )
    }

    fun playSongsNext( songs: List<Song> ) {
        songs.forEach { playSongNext( it ) }
    }

    fun playSongNext(
        song: Song
    ) {
        musicServiceConnection.playNext( song.mediaItem )
    }

    fun addSongsToQueue( songs: List<Song> ) {
        songs.forEach { addSongToQueue( it ) }
    }

    fun addSongToQueue( song: Song ) {
        musicServiceConnection.addToQueue( song.mediaItem )
    }

    fun searchSongsMatching( query: String ) =
        songsFuzzySearcher.search(
            terms = query,
            entities = musicServiceConnection.cachedSongs.value.map { it.id }
        ).mapNotNull { getSongWithId( it.entity ) }

    private fun getEditablePlaylists() = playlistRepository.playlists.value.filter {
        it.id != playlistRepository.mostPlayedSongsPlaylist.value.id &&
                it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id
    }

    fun createPlaylist(
        playlistTitle: String,
        songsToAddToPlaylist: List<Song>
    ) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(
                Playlist(
                    id = UUID.randomUUID().toString(),
                    title = playlistTitle,
                    songIds = songsToAddToPlaylist.map { it.id }
                )
            )
        }
    }

    private fun getSongWithId( id: String ) =
        musicServiceConnection.cachedSongs.value.find { it.id == id }

    fun playSongsInAlbum( album: Album ) {
        playSongs(
            songs = getSongsInAlbumAsMediaItems( album ),
            shuffle = settingsRepository.shuffle.value
        )
    }

    fun playSongsInAlbumNext( album: Album ) {
        playMediaItemsNext( getSongsInAlbumAsMediaItems( album ) )
    }


    fun addSongsInAlbumToQueue( album: Album ) {
        addMediaItemsToQueue( getSongsInAlbumAsMediaItems( album ) )
    }

    fun playSongsInPlaylist( playlist: Playlist ) {
        playSongs(
            songs = getSongsInPlaylist( playlist ).map { it.mediaItem },
            shuffle = settingsRepository.shuffle.value
        )
    }

    fun getSongsInPlaylist( playlist: Playlist ) =
        musicServiceConnection.cachedSongs.value.filter {
            playlist.songIds.contains( it.id )
        }

    fun playSongsByArtist(
        artist: Artist
    ) {
        playSongsByArtist(
            artist = artist,
            shuffle = settingsRepository.shuffle.value
        )
    }

    private fun playSongsByArtist(
        artist: Artist,
        shuffle: Boolean = false
    ) {
        playSongs(
            songs = getSongsByArtistAsMediaItems( artist ),
            shuffle = shuffle
        )
    }

    fun addSongsByArtistToQueue( artist: Artist) {
        addMediaItemsToQueue( getSongsByArtistAsMediaItems( artist ) )
    }

    private fun playSongs(
        songs: List<MediaItem>,
        shuffle: Boolean
    ) {
        if ( songs.isEmpty() ) return
        musicServiceConnection.playMediaItem(
            mediaItem = if( shuffle ) songs.random() else songs.first(),
            mediaItems = songs,
            shuffle = shuffle
        )
    }

    private fun playMediaItemsNext(
        songs: List<MediaItem>
    ) {
        songs.forEach {
            musicServiceConnection.playNext( it )
        }
    }

    fun playSongsByArtistNext( artist: Artist ) {
        playMediaItemsNext(
            songs = getSongsByArtistAsMediaItems( artist )
        )
    }

    private fun addMediaItemsToQueue(
        songs: List<MediaItem>
    ) {
        songs.forEach {
            musicServiceConnection.addToQueue( it )
        }
    }

    private fun getSongsByArtistAsMediaItems( artist: Artist ) =
        getSongsByArtist( artist )
            .map { it.mediaItem }

    fun getSongsByArtist( artist: Artist ) =
        musicServiceConnection.cachedSongs.value
            .filter { it.artists.contains( artist.name ) }

    private fun getSongsInAlbumAsMediaItems( album: Album ) =
        getSongsInAlbum( album ).map { it.mediaItem }

    fun getSongsInAlbum( album: Album ) =
        musicServiceConnection.cachedSongs.value
            .filter{ it.albumTitle == album.title }
}