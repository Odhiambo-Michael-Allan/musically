package com.odesa.musically.ui.songs

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.extensions.METADATA_KEY_DATE_MODIFIED
import com.odesa.musically.services.media.extensions.METADATA_KEY_PATH
import com.odesa.musically.services.media.extensions.METADATA_KEY_SIZE
import com.odesa.musically.services.media.library.MUSICALLY_TRACKS_ROOT
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SongsViewModel(
    private val settingsRepository: SettingsRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SongsScreenUiState(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songs = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            Timber.tag( songsViewModelTag ).d( "SUBSCRIPTION CALLBACK INVOKED. UPDATING MEDIA ITEMS" )
            val itemList = children.map { child ->
                val mediaId = child.description.mediaId.toString()
                val title = child.description.title.toString()
                val description = child.description.description
                val iconUri = child.description.iconUri
                val mediaUri = child.description.mediaUri
                val extras = child.description.extras
                val album = extras?.getString( MediaMetadataCompat.METADATA_KEY_ALBUM )
                val albumArtist = extras?.getString( MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST )
                val artist = extras?.getString( MediaMetadataCompat.METADATA_KEY_ARTIST )
                val genre = extras?.getString( MediaMetadataCompat.METADATA_KEY_GENRE )
                val duration = extras?.getLong( MediaMetadataCompat.METADATA_KEY_DURATION )
                val trackNumber = extras?.getLong( MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER )
                val year = extras?.getLong( MediaMetadataCompat.METADATA_KEY_YEAR )
                val composer = extras?.getString( MediaMetadataCompat.METADATA_KEY_COMPOSER )
                val dateModified = extras?.getLong( METADATA_KEY_DATE_MODIFIED )
                val path = extras?.getString( METADATA_KEY_PATH )
                val size = extras?.getLong( METADATA_KEY_SIZE )
                Song(
                    id = mediaId,
                    mediaUri = mediaUri!!,
                    title = title,
                    trackNumber = trackNumber,
                    year = year,
                    duration = duration!!,
                    album = album,
                    artists = artist,
                    composers = composer,
                    dateModified = dateModified!!,
                    size = size!!,
                    path = path!!,
                    artworkUri = iconUri

                )
            }
            _uiState.value = _uiState.value.copy(
                songs = itemList
            )
        }
    }

    init {
        musicServiceConnection.subscribe( MUSICALLY_TRACKS_ROOT, subscriptionCallback )
        viewModelScope.launch { observeLanguageChange() }
//        viewModelScope.launch { observeIsLoadingSongsChange() }
//        viewModelScope.launch { observeSortSongsInReverse() }
//        viewModelScope.launch { observeSortSongsBy() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeConnectedState() }
    }

    private suspend fun observeLanguageChange() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

//    private suspend fun observeIsLoadingSongsChange() {
//        songsRepository.isLoadingSongs.collect {
//            _uiState.value = _uiState.value.copy(
//                isLoadingSongs = it
//            )
//        }
//    }

//    private suspend fun observeSortSongsInReverse() {
//        songsRepository.sortSongsInReverse.collect {
//            _uiState.value = _uiState.value.copy(
//                sortSongsInReverse =  it
//            )
//        }
//    }

//    private suspend fun observeSortSongsBy() {
//        songsRepository.sortSongsBy.collect {
//            _uiState.value = _uiState.value.copy(
//                sortSongsBy = it
//            )
//        }
//    }

    private suspend fun observeThemeMode() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    private suspend fun observeConnectedState() {
        musicServiceConnection.isConnected.collect { isConnected ->
            if ( isConnected )
                Timber.tag(songsViewModelTag).d("MUSIC SERVICE CONNECTION CONNECTED" )
            else
                Timber.tag( songsViewModelTag ).d( "MUSIC SERVICE CONNECTION FAILED" )
        }
    }



//    fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
//        viewModelScope.launch { songsRepository.setSortSongsInReverse( sortSongsInReverse ) }
//    }
//
//    fun setSortSongsBy( sortSongsBy: SortSongsBy ) {
//        viewModelScope.launch { songsRepository.setSortSongsBy( sortSongsBy ) }
//    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe( MUSICALLY_TRACKS_ROOT, subscriptionCallback )
    }

}

data class SongsScreenUiState(
    val language: Language,
//    val isLoadingSongs: Boolean,
//    val sortSongsInReverse: Boolean,
//    val sortSongsBy: SortSongsBy,
    val themeMode: ThemeMode,
    val songs: List<Song>,
)

@Suppress( "UNCHECKED_CAST" )
class SongsViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SongsViewModel(
            settingsRepository,
            musicServiceConnection,
        ) as T )
}

const val songsViewModelTag = "SONGS-VIEW-MODEL"