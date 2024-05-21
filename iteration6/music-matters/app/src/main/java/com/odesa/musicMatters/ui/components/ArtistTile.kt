package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testArtists


@Composable
fun ArtistTile(
    artist: Artist,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlaySongsByArtist: () -> Unit,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onViewArtist: () -> Unit,
    onGetSongsByArtist: ( Artist ) -> List<Song>,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: (Playlist) -> List<Song>,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
) {
    GenericTile(
        modifier = Modifier,
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( artist.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        title = language.artist,
        description = artist.name,
        headerDescription = artist.name,
        language = language,
        fallbackResourceId = fallbackResourceId,
        onPlay = onPlaySongsByArtist,
        onClick = onViewArtist,
        onShufflePlay = onShufflePlay,
        onAddToQueue = onAddToQueue,
        onPlayNext = onPlayNext,
        onGetSongs = { onGetSongsByArtist( artist ) },
        onGetPlaylists = onGetPlaylists,
        onGetSongsInPlaylist = onGetSongsInPlaylist,
        onAddSongsToPlaylist = { onAddSongsToPlaylist( it, onGetSongsByArtist( artist ) ) },
        onCreatePlaylist = onCreatePlaylist,
        onSearchSongsMatchingQuery = onSearchSongsMatchingQuery
    )
}

@Composable
fun ArtistOptionsBottomSheetMenu(
    artist: Artist,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    BottomSheetMenuContent(
        bottomSheetHeader = {
            BottomSheetMenuHeader(
                headerImage = ImageRequest.Builder( LocalContext.current ).apply {
                    data( artist.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                title = language.artist,
                description = artist.name
            )
        }
    ) {
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.shufflePlay
        ) {
            onDismissRequest()
            onShufflePlay()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.playNext
        ) {
            onDismissRequest()
            onPlayNext()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.addToQueue
        ) {
            onDismissRequest()
            onAddToQueue()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
            label = language.addToPlaylist
        ) {
            onDismissRequest()
            onAddToPlaylist()
        }
    }
}


@Preview( showBackground = true )
@Composable
fun ArtistTilePreview() {
    ArtistTile(
        artist = testArtists.first(),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onPlaySongsByArtist = { /*TODO*/ },
        onShufflePlay = { /*TODO*/ },
        onPlayNext = { /*TODO*/ },
        onAddToQueue = { /*TODO*/ },
        onViewArtist = {},
        onAddSongsToPlaylist = { _, _ -> },
        onCreatePlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
        onSearchSongsMatchingQuery = { emptyList() },
        onGetSongsByArtist = { emptyList() }
    )
}