package com.odesa.musicMatters.ui.genre

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song

import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.SongList

import com.odesa.musicMatters.ui.utils.displayToastWithMessage

@Composable
fun GenreScreen(
    genreName: String,
    viewModel: GenreScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    GenreScreenContent(
        uiState = uiState,
        genreName = genreName,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {
            viewModel.shuffleAndPlay( songs = uiState.songsInGenre )
        },
        playSong = {
            viewModel.playSongs(
                selectedSong = it,
                songsInPlaylist = uiState.songsInGenre
            )
        },
        onFavorite = { viewModel.addToFavorites( it ) },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onNavigateBack = onNavigateBack,
        onPlayNext = {
            viewModel.playSongNext( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} will play next"
            )
        },
        onAddToQueue = {
            viewModel.addSongToQueue( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} added to queue"
            )
        },
        onAddSongsToPlaylist = { playlist, songs ->
            viewModel.addSongsToPlaylist( playlist, songs )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onCreatePlaylist = { title, songs ->
            viewModel.createPlaylist( title, songs )
        },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onGetPlaylists = { uiState.playlists },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) }
    )
}

@Composable
fun GenreScreenContent(
    uiState: GenreScreenUiState,
    genreName: String,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onShufflePlay: () -> Unit,
    playSong: (Song) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( Song ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onNavigateBack: () -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        MinimalAppBar(
            onNavigationIconClicked = onNavigateBack,
            title = "${uiState.language.genre} - $genreName"
        )
        LoaderScaffold(
            isLoading = uiState.isLoading,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = true,
                onSortReverseChange = onSortReverseChange,
                sortSongsBy = SortSongsBy.TITLE,
                onSortTypeChange = onSortTypeChange,
                language = uiState.language,
                songs = uiState.songsInGenre,
                playlists = onGetPlaylists(),
                onShufflePlay = onShufflePlay,
                fallbackResourceId = fallbackResourceId,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onGetSongsInPlaylist = onGetSongsInPlaylist,
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist,
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun GenreScreenContentPreview() {
    GenreScreenContent(
        uiState = testGenreScreenUiState,
        genreName = "Hip Hop",
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {},
        playSong = {},
        onFavorite = {},
        onNavigateBack = {},
        onViewAlbum = {},
        onViewArtist = {},
        onShareSong = {},
        onPlayNext = {},
        onAddToQueue = {},
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInPlaylist = { emptyList() }
    )
}

