package com.odesa.musicMatters.ui.search

import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.search.SearchHistoryItem
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Genre
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.services.media.testArtists
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.components.GenericCard
import com.odesa.musicMatters.ui.components.IconTextBody
import com.odesa.musicMatters.ui.components.testGenres
import com.odesa.musicMatters.ui.theme.MusicMattersTheme
import com.odesa.musicMatters.ui.theme.isLight
import com.odesa.musicMatters.utils.subListNonStrict
import java.util.UUID

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel,
    onArtistClick: ( Artist ) -> Unit,
    onAlbumClick: ( Album ) -> Unit,
    onGenreClick: ( Genre ) -> Unit,
    onPlaylistClick: (Playlist ) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchScreenContent(
        uiState = uiState,
        onGetSong = { viewModel.getSongWithId( it ) },
        onGetAlbum = { viewModel.getAlbumWithName( it ) },
        onGetArtist = { viewModel.getArtistWithName( it ) },
        onGetGenre = { viewModel.getGenreWithName( it ) },
        onGetPlaylist = { viewModel.getPlaylistWithId( it ) },
        onGetPlaylistArtworkUri = { viewModel.getPlaylistArtworkUri( it ) },
        onSearch = { query, filter -> viewModel.search( query, filter ) },
        onSongClick = {
            viewModel.addSongToSearchHistory( it )
            viewModel.playMedia( it.mediaItem )
        },
        onAlbumClick = {
            viewModel.addAlbumToSearchHistory( it )
            onAlbumClick( it )
        },
        onArtistClick = {
            viewModel.addArtistToSearchHistory( it )
            onArtistClick( it )
        },
        onGenreClick = {
            viewModel.addGenreToSearchHistory( it )
            onGenreClick( it )
        },
        onPlaylistClick = {
            viewModel.addPlaylistToSearchHistory( it )
            onPlaylistClick( it )
        },
        onNavigateBack = onNavigateBack
    )

}

@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    onGetSong: ( String ) -> Song?,
    onGetAlbum: ( String ) -> Album?,
    onGetArtist: ( String ) -> Artist?,
    onGetGenre: ( String ) -> Genre?,
    onGetPlaylist: ( String ) -> Playlist?,
    onGetPlaylistArtworkUri: ( Playlist ) -> Uri?,
    onSearch: ( String, SearchFilter? ) -> Unit,
    onSongClick: ( Song ) -> Unit,
    onArtistClick: ( Artist ) -> Unit,
    onAlbumClick: ( Album ) -> Unit,
    onGenreClick: ( Genre ) -> Unit,
    onPlaylistClick: (Playlist ) -> Unit,
    onNavigateBack: () -> Unit,
) {

    var currentSearchQuery by remember { mutableStateOf( uiState.currentSearchQuery ) }
    var currentSearchFilter by rememberSaveable { mutableStateOf<SearchFilter?>( null ) }

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark
    
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            searchQuery = currentSearchQuery,
            language = uiState.language,
            currentSearchFilter = currentSearchFilter,
            onSearchQueryChange = {
                currentSearchQuery = it
                onSearch( currentSearchQuery, currentSearchFilter )
            },
            onFilterChange = {
                currentSearchFilter = it
                onSearch( currentSearchQuery, currentSearchFilter )
            },
            onNavigateBack = onNavigateBack
        )
        if ( currentSearchQuery.isNotEmpty() ) {
            SearchResultsList(
                isSearching = uiState.isSearching,
                currentSearchFilter = uiState.currentSearchFilter,
                searchResults = uiState.currentSearchResults,
                language = uiState.language,
                fallbackResourceId = fallbackResourceId,
                onSongClick = onSongClick,
                onArtistClick = onArtistClick,
                onAlbumClick = onAlbumClick,
                onGenreClick = onGenreClick,
                onPlaylistClick = onPlaylistClick,
                onGetPlaylistArtworkUri = onGetPlaylistArtworkUri,
            )
        }
        else {
            SearchHistoryList(
                searchHistoryItems = uiState.searchHistoryItems,
                language = uiState.language,
                fallbackResourceId = fallbackResourceId,
                onGetSong = onGetSong,
                onGetAlbum = onGetAlbum,
                onGetArtist = onGetArtist,
                onGetGenre = onGetGenre,
                onGetPlaylist = onGetPlaylist,
                onGetPlaylistArtworkUri = onGetPlaylistArtworkUri,
                onSongClick = onSongClick,
                onAlbumClick = onAlbumClick,
                onArtistClick = onArtistClick,
                onGenreClick = onGenreClick,
                onPlaylistClick = onPlaylistClick,
            )
        }
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
private fun TopAppBar(
    searchQuery: String,
    language: Language,
    currentSearchFilter: SearchFilter?,
    onSearchQueryChange: ( String ) -> Unit,
    onFilterChange: ( SearchFilter? ) -> Unit,
    onNavigateBack: () -> Unit,
) {

    Column (
        Modifier
            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
            .clipToBounds()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions( imeAction = ImeAction.Search ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            singleLine = true,
            value = searchQuery,
            onValueChange = { onSearchQueryChange( it ) },
            placeholder = {
                Text( text = language.searchYourMusic )
            },
            leadingIcon = {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                if ( searchQuery.isNotEmpty() ) {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
        Spacer( modifier = Modifier.height( 4.dp ) )
        Row (
            modifier = Modifier.horizontalScroll( rememberScrollState() ),
            horizontalArrangement = Arrangement.spacedBy( 8.dp )
        ) {
            Spacer( modifier = Modifier.width( 4.dp ) )
            FilterChip(
                selected = currentSearchFilter == null,
                onClick = {
                    onFilterChange( null )
                },
                label = {
                    Text( text = language.all )
                }
            )
            SearchFilter.entries.forEach {
                FilterChip(
                    selected = currentSearchFilter == it,
                    onClick = {
                        onFilterChange( it )
                    },
                    label = {
                        Text( text = it.label( language ) )
                    }
                )
            }
            Spacer( modifier = Modifier.width( 4.dp ) )
        }
        Spacer( modifier = Modifier.height( 4.dp ) )
    }
}

@Composable
fun SearchHistoryList(
    searchHistoryItems: List<SearchHistoryItem>,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onGetSong: ( String ) -> Song?,
    onGetAlbum: ( String ) -> Album?,
    onGetArtist: ( String ) -> Artist?,
    onGetGenre: ( String ) -> Genre?,
    onGetPlaylist: ( String ) -> Playlist?,
    onGetPlaylistArtworkUri: ( Playlist ) -> Uri?,
    onSongClick: (Song) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onArtistClick: (Artist) -> Unit,
    onGenreClick: ( Genre ) -> Unit,
    onPlaylistClick: (Playlist ) -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            searchHistoryItems.isEmpty() -> {
                Text(
                    modifier = Modifier.align( Alignment.Center ),
                    text = "No recent searches"
                )
            }
            else -> {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    searchHistoryItems.forEach {
                        when ( it.category ) {
                            SearchFilter.SONG -> {
                                onGetSong( it.id )?.let { song ->
                                    SearchHistoryListItem(
                                        artworkUri = song.artworkUri,
                                        fallbackResourceId = fallbackResourceId,
                                        title = song.title,
                                        subtitle = "Song - ${song.artists.joinToString()}",
                                        onClick = { onSongClick( song ) }
                                    )
                                }

                            }
                            SearchFilter.ALBUM -> {
                                onGetAlbum( it.id )?.let { album ->
                                    SearchHistoryListItem(
                                        artworkUri = album.artworkUri,
                                        fallbackResourceId = fallbackResourceId,
                                        title = album.name,
                                        subtitle = "Album - ${album.artists.joinToString()}",
                                        onClick = { onAlbumClick( album ) }
                                    )
                                }

                            }
                            SearchFilter.ARTIST -> {
                                onGetArtist( it.id )?.let { artist ->
                                    SearchHistoryListItem(
                                        artworkUri = artist.artworkUri,
                                        fallbackResourceId = fallbackResourceId,
                                        title = artist.name,
                                        subtitle = language.artist,
                                        onClick = { onArtistClick( artist ) }
                                    )
                                }

                            }
                            SearchFilter.GENRE -> {
                                onGetGenre( it.id )?.let { genre ->
                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        GenericCard(
                                            modifier = Modifier.weight( 1f ),
                                            imageRequest = null,
                                            title = { Text( text = genre.name ) },
                                            subtitle = { Text( text = genre.numberOfTracks.toString() ) },
                                            onClick = { onGenreClick( genre ) }
                                        )
                                        IconButton(
                                            onClick = { /*TODO*/ }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }

                            }
                            SearchFilter.PLAYLIST -> {
                                onGetPlaylist( it.id )?.let { playlist ->
                                    SearchHistoryListItem(
                                        artworkUri = onGetPlaylistArtworkUri( playlist ),
                                        fallbackResourceId = fallbackResourceId,
                                        title = playlist.title,
                                        subtitle = language.playlist,
                                        onClick = { onPlaylistClick( playlist ) }
                                    )
                                }
                            }
                        }
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Text(
                                text = "Clear Search History"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHistoryListItem(
    artworkUri: Uri?,
    @DrawableRes fallbackResourceId: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    val context = LocalContext.current

    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GenericCard(
            modifier = Modifier.weight( 1f ),
            imageRequest = createImageRequest(
                context = context,
                artworkUri = artworkUri,
                fallbackResourceId = fallbackResourceId
            ),
            title = {
                Text( text = title )
            },
            subtitle = {
                Text(
                    text = subtitle
                )
            },
            onClick = onClick
        )
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun SearchHistoryListPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.PRIMARY_COLOR_NAME,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.FONT_SCALE,
        useMaterialYou = true
    ) {
        SearchHistoryList(
            searchHistoryItems = testSearchHistoryItems,
            language = English,
            fallbackResourceId = R.drawable.placeholder_light,
            onGetSong = { testSongs.first() },
            onGetAlbum = { testAlbums.first() },
            onGetArtist = { testArtists.first() },
            onGetGenre = { testGenres.first() },
            onGetPlaylist = { testPlaylists.first() },
            onGetPlaylistArtworkUri = { null },
            onArtistClick = {},
            onAlbumClick = {},
            onGenreClick = {},
            onSongClick = {},
            onPlaylistClick = {}
        )
    }
}


@Composable
fun SearchResultsList(
    isSearching: Boolean,
    currentSearchFilter: SearchFilter?,
    searchResults: SearchResults,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onSongClick: ( Song ) -> Unit,
    onArtistClick: ( Artist ) -> Unit,
    onAlbumClick: ( Album ) -> Unit,
    onGenreClick: ( Genre ) -> Unit,
    onGetPlaylistArtworkUri: ( Playlist ) -> Uri?,
    onPlaylistClick: ( Playlist ) -> Unit,
) {
    val context = LocalContext.current

    searchResults.run {
        val hasSongs = isFilterSelected( SearchFilter.SONG, currentSearchFilter ) && searchResults.matchingSongs.isNotEmpty()
        val hasArtists = isFilterSelected( SearchFilter.ARTIST, currentSearchFilter ) && searchResults.matchingArtists.isNotEmpty()
        val hasAlbums = isFilterSelected( SearchFilter.ALBUM, currentSearchFilter ) && searchResults.matchingAlbums.isNotEmpty()
        val hasGenres = isFilterSelected( SearchFilter.GENRE, currentSearchFilter ) && searchResults.matchingGenres.isNotEmpty()
        val hasPlaylists = isFilterSelected( SearchFilter.PLAYLIST, currentSearchFilter ) && searchResults.matchingPlaylists.isNotEmpty()
        val hasNoResults = !hasSongs && !hasArtists && !hasAlbums && !hasPlaylists && !hasGenres

        Box( modifier = Modifier.fillMaxSize() ) {
            when {
                isSearching -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align( Alignment.Center ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                hasNoResults -> {
                    Box(
                        modifier = Modifier.align( Alignment.Center )
                    ) {
                        IconTextBody(
                            icon = { modifier ->
                                Icon(
                                    modifier = modifier,
                                    imageVector = Icons.Filled.PriorityHigh,
                                    contentDescription = null
                                )
                            }
                        ) {
                            Text(text = language.noResultsFound)
                        }
                    }
                }
                else -> {
                    Column (
                        modifier = Modifier.verticalScroll( rememberScrollState() )
                    ) {
                        if ( hasSongs ) {
                            SideHeading( searchFilter = SearchFilter.SONG, language = language )
                            matchingSongs.forEach {
                                GenericCard(
                                    imageRequest = createImageRequest(
                                        context = context,
                                        artworkUri = it.artworkUri,
                                        fallbackResourceId = fallbackResourceId,
                                    ),
                                    title = { 
                                        Text( text = it.title )
                                    },
                                    subtitle = {
                                        Text( text = it.artists.joinToString() )
                                    },
                                    onClick = { onSongClick( it ) }
                                )
                            }
                        }
                        if ( hasArtists ) {
                            SideHeading( searchFilter = SearchFilter.ARTIST, language = language )
                            matchingArtists.forEach {
                                GenericCard(
                                    imageRequest = createImageRequest(
                                        context = context,
                                        artworkUri = it.artworkUri,
                                        fallbackResourceId = fallbackResourceId
                                    ),
                                    title = {
                                        Text( text = it.name )
                                    },
                                    onClick = { onArtistClick( it ) }
                                )
                            }
                        }
                        if ( hasAlbums ) {
                            SideHeading( searchFilter = SearchFilter.ALBUM, language = language )
                            matchingAlbums.forEach {
                                GenericCard(
                                    imageRequest = createImageRequest(
                                        context = context,
                                        artworkUri = it.artworkUri,
                                        fallbackResourceId = fallbackResourceId
                                    ),
                                    title = { 
                                        Text( text = it.name )
                                    },
                                    subtitle = {
                                        Text( text = it.artists.joinToString() )
                                    },
                                    onClick = { onAlbumClick( it ) }
                                )
                            }
                        }
                        if ( hasGenres ) {
                            SideHeading( searchFilter = SearchFilter.GENRE, language = language )
                            matchingGenres.forEach {
                                GenericCard(
                                    imageRequest = null,
                                    title = { Text( text = it.name ) },
                                    subtitle = { Text( text = it.numberOfTracks.toString() ) }
                                ) {
                                    onGenreClick( it )
                                }
                            }
                        }
                        if ( hasPlaylists ) {
                            SideHeading( searchFilter = SearchFilter.PLAYLIST, language = language )
                            matchingPlaylists.forEach {
                                GenericCard(
                                    imageRequest = createImageRequest(
                                        context = context,
                                        artworkUri = onGetPlaylistArtworkUri( it ),
                                        fallbackResourceId = fallbackResourceId,
                                    ),
                                    title = {
                                        Text( text = it.title )
                                    }
                                ) {
                                    onPlaylistClick( it )
                                }
                            }
                        }
                        Spacer( modifier = Modifier.height( 12.dp ) )
                    }
                }
            }
        }
    }
}

@Composable
private fun SideHeading(
    searchFilter: SearchFilter,
    language: Language,
) {
    Text(
        modifier = Modifier.padding( 12.dp, 12.dp, 12.dp, 4.dp ),
        text = searchFilter.label( language ),
        style = MaterialTheme.typography.bodyMedium.copy( fontWeight = FontWeight.Bold )
    )
}

private fun isFilterSelected( searchFilter: SearchFilter, currentFilter: SearchFilter? ) = currentFilter == null || currentFilter == searchFilter

private fun SearchFilter.label( language: Language ) = when ( this ) {
    SearchFilter.SONG -> language.songs
    SearchFilter.ALBUM -> language.albums
    SearchFilter.ARTIST -> language.artists
    SearchFilter.GENRE -> language.genres
    SearchFilter.PLAYLIST -> language.playlists
}

private fun createImageRequest( context: Context, artworkUri: Uri?, @DrawableRes fallbackResourceId: Int ) =
    ImageRequest.Builder( context ).apply{
        data( artworkUri )
        placeholder( fallbackResourceId )
        fallback( fallbackResourceId )
        error( fallbackResourceId )
        crossfade( true )
        build()
    }.build()

@Preview( showSystemUi = true )
@Composable
fun SearchScreenContentPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.PRIMARY_COLOR_NAME,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.FONT_SCALE,
        useMaterialYou = true
    ) {
        SearchScreenContent(
            uiState = SearchScreenUiState(
                currentSearchQuery = "",
                language = English,
                currentSearchFilter = null,
                isSearching = false,
                currentSearchResults = SearchResults(
                    matchingSongs = testSongs.subListNonStrict( 2 ),
                    matchingAlbums = testAlbums.subListNonStrict( 2 ),
                    matchingGenres = testGenres.subListNonStrict( 2 ),
                    matchingArtists = testArtists.subListNonStrict( 2 ),
                    matchingPlaylists = testPlaylists.subListNonStrict( 2 )
                ),
                themeMode = SettingsDefaults.themeMode,
                searchHistoryItems = testSearchHistoryItems
            ),
            onSearch = { _, _ -> },
            onGetSong = { testSongs.first() },
            onGetAlbum = { testAlbums.first() },
            onGetArtist = { testArtists.first() },
            onGetGenre = { testGenres.first() },
            onGetPlaylist = { testPlaylists.first() },
            onGetPlaylistArtworkUri = { null },
            onGenreClick = {},
            onArtistClick = {},
            onPlaylistClick = {},
            onAlbumClick = {},
            onSongClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview( showBackground = true )
@Composable
private fun TopAppBarPreview() {
    TopAppBar(
        searchQuery = "",
        currentSearchFilter = null,
        language = English,
        onSearchQueryChange = {},
        onFilterChange = {},
        onNavigateBack = {}
    )
}

@Preview( showSystemUi = true )
@Composable
fun SearchResultsListPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.PRIMARY_COLOR_NAME,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.FONT_SCALE,
        useMaterialYou = true
    ) {
        SearchResultsList(
            isSearching = false,
            currentSearchFilter = null,
            searchResults = SearchResults(
                matchingSongs = testSongs.subListNonStrict( 2 ),
                matchingAlbums = testAlbums.subListNonStrict( 2 ),
                matchingGenres = testGenres.subListNonStrict( 2 ),
                matchingArtists = testArtists.subListNonStrict( 2 ),
                matchingPlaylists = testPlaylists.subListNonStrict( 2 )
            ),
            language = English,
            fallbackResourceId = R.drawable.placeholder_light,
            onArtistClick = {},
            onAlbumClick = {},
            onPlaylistClick = {},
            onGenreClick = {},
            onSongClick = {},
            onGetPlaylistArtworkUri = { null }
        )
    }
}

val testSearchHistoryItems = listOf(
    SearchHistoryItem(
        id = UUID.randomUUID().toString(),
        category = SearchFilter.SONG
    ),
    SearchHistoryItem(
        id = testAlbums.first().name,
        category = SearchFilter.ALBUM
    ),
    SearchHistoryItem(
        id = testArtists.first().name,
        category = SearchFilter.ARTIST
    ),
    SearchHistoryItem(
        id = testGenres.first().name,
        category = SearchFilter.GENRE
    ),
    SearchHistoryItem(
        id = testPlaylists.first().id,
        category = SearchFilter.PLAYLIST
    )
)
