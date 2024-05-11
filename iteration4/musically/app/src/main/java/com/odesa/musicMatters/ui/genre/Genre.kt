package com.odesa.musicMatters.ui.genre

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun GenreScreen(
    genreName: String,
    genreScreenViewModel: GenreScreenViewModel,
    onViewAlbum: (String ) -> Unit,
    onViewArtist: (String ) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val uiState by genreScreenViewModel.uiState.collectAsState()
    val context = LocalContext.current

    GenreScreenContent(
        uiState = uiState,
        genreName = genreName,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {},
        playSong = { genreScreenViewModel.playMedia( it.mediaItem ) },
        onFavorite = { genreScreenViewModel.addToFavorites( it ) },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onNavigateBack = onNavigateBack,
        onShareSong = {
            try {
                val intent = Intent( Intent.ACTION_SEND ).apply {
                    addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION )
                    putExtra( Intent.EXTRA_STREAM, it )
                    type = context.contentResolver.getType( it )
                }
                context.startActivity( intent )
            }
            catch ( exception: Exception ) {
                Toast.makeText(
                    context,
                    com.odesa.musicMatters.ui.songs.uiState.language.shareFailedX( exception.localizedMessage ?: exception.toString() ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}

@Composable
fun GenreScreenContent(
    uiState: GenreScreenUistate,
    genreName: String,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onShufflePlay: () -> Unit,
    playSong: ( Song ) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onNavigateBack: () -> Unit,
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
                songs = uiState.songs,
                onShufflePlay = onShufflePlay,
                fallbackResourceId = fallbackResourceId,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong,
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
        onShareSong = {}
    )
}

