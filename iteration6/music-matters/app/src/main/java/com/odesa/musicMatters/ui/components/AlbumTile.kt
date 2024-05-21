package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testAlbums

@Composable
fun AlbumTile(
    modifier: Modifier,
    album: Album,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlayAlbum: () -> Unit,
    onAddToQueue: () -> Unit,
    onPlayNext: () -> Unit,
    onShufflePlay: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onClick: () -> Unit,
    onGetSongsInAlbum: ( Album ) -> List<Song>,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
) {

    GenericTile(
        modifier = modifier,
        imageRequest = ImageRequest.Builder(LocalContext.current).apply {
            data( album.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        title = album.name,
        description = album.artists.joinToString(),
        headerDescription = album.artists.joinToString(),
        language = language,
        fallbackResourceId = fallbackResourceId,
        onPlay = onPlayAlbum,
        onClick = onClick,
        onShufflePlay = onShufflePlay,
        onAddToQueue = onAddToQueue,
        onPlayNext = onPlayNext,
        onGetSongs = { onGetSongsInAlbum( album ) },
        onGetPlaylists = onGetPlaylists,
        onGetSongsInPlaylist = onGetSongsInPlaylist,
        onAddSongsToPlaylist = { onAddSongsToPlaylist( it, onGetSongsInAlbum( album ) ) },
        onCreatePlaylist = onCreatePlaylist,
        onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
        additionalBottomSheetMenuItems = { onDismissRequest ->
            album.artists.forEach { artist ->
                BottomSheetMenuItem(
                    imageVector = Icons.Filled.Person,
                    label = "${language.viewArtist}: $artist"
                ) {
                    onDismissRequest()
                    onViewArtist( artist )
                }
            }
        }
    )
}

@Composable
fun AlbumOptionsBottomSheetMenu(
    album: Album,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onAddToPlaylist: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BottomSheetMenuContent(
        bottomSheetHeader = {
            BottomSheetMenuHeader(
                headerImage = ImageRequest.Builder( LocalContext.current ).apply {
                    data( album.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                title = album.name,
                description = album.artists.joinToString()
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
        album.artists.forEach {
            BottomSheetMenuItem(
                imageVector = Icons.Filled.Person,
                label = "${language.viewArtist}: $it"
            ) {
                onDismissRequest()
                onViewArtist( it )
            }
        }
    }
}


@Preview( showSystemUi = true )
@Composable
fun AlbumTilePreview() {
    AlbumTile(
        modifier = Modifier.fillMaxWidth(),
        album = testAlbums.first(),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onPlayAlbum = { /*TODO*/ },
        onAddToQueue = { /*TODO*/ },
        onPlayNext = { /*TODO*/ },
        onShufflePlay = { /*TODO*/ },
        onViewArtist = {},
        onAddSongsToPlaylist = { _, _ -> },
        onGetSongsInAlbum = { emptyList() },
        onClick = {},
        onCreatePlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
        onSearchSongsMatchingQuery = { emptyList() }
    )
}