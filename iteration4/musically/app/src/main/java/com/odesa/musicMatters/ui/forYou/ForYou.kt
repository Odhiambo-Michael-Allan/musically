package com.odesa.musicMatters.ui.forYou

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.services.media.testArtists
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun ForYouScreen(
    viewModel: ForYouScreenViewModel,
    onSettingsClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    ForYouScreenContent(
        uiState = uiState,
        onShuffleAndPlay = { viewModel.shuffleAndPlay() },
        onSettingsClicked = onSettingsClicked,
        onSongInRecentlyAddedSongsSelected = { viewModel.playRecentlyAddedSong( it ) },
        onSongInMostPlayedSongsSelected = { viewModel.playMostPlayedSong( it ) },
        onSongInPlayHistorySelected = { viewModel.playSongInPlayHistory( it ) }
    )
}

@Composable
fun ForYouScreenContent(
    uiState: ForYouScreenUiState,
    onShuffleAndPlay: () -> Unit,
    onSettingsClicked: () -> Unit,
    onSongInRecentlyAddedSongsSelected: ( MediaItem ) -> Unit,
    onSongInMostPlayedSongsSelected: ( MediaItem ) -> Unit,
    onSongInPlayHistorySelected: ( MediaItem ) -> Unit,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = stringResource( id = R.string.app_name ),
                settings = "Settings",
                onSettingsClicked = onSettingsClicked
            )
            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
            ) {
                RecentlyAddedSongs(
                    language = uiState.language,
                    isLoadingRecentSongs = uiState.isLoadingRecentSongs,
                    recentlyAddedSongs = uiState.recentlyAddedSongs,
                    fallbackResourceId = fallbackResourceId,
                    onSongInRecentlyAddedSongsSelected = onSongInRecentlyAddedSongsSelected
                )
                SuggestedAlbums(
                    language = uiState.language,
                    isLoading = uiState.isLoadingSuggestedAlbums,
                    albums = uiState.suggestedAlbums,
                    fallbackResourceId = fallbackResourceId
                )
                if ( uiState.mostPlayedSongs.isNotEmpty() ) {
                    MostPlayedSongs(
                        language = uiState.language,
                        isLoadingMostPlayedSongs = uiState.isLoadingMostPlayedSongs,
                        mostPlayedSongs = uiState.mostPlayedSongs,
                        fallbackResourceId = fallbackResourceId,
                        onSongInMostPlayedSongsSelected = onSongInMostPlayedSongsSelected
                    )
                }
                SuggestedArtists(
                    language = uiState.language,
                    isLoading = uiState.isLoadingSuggestedArtists,
                    suggestedArtists = uiState.suggestedArtists,
                    fallbackResourceId = fallbackResourceId
                )
                if ( uiState.songsInPlayHistory.isNotEmpty() ) {
                    PlayHistory(
                        language = uiState.language,
                        isLoadingPlayHistory = uiState.isLoadingPlayHistory,
                        songsInPlayHistory = uiState.songsInPlayHistory,
                        fallbackResourceId = fallbackResourceId,
                        onSongInPlayHistorySelected = onSongInPlayHistorySelected
                    )
                }
                Spacer( modifier = Modifier.height( 100.dp ) )
            }
        }
        LargeFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onClick = onShuffleAndPlay
        ) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = null
            )
        }
    }
}

@Composable
fun RecentlyAddedSongs(
    language: Language,
    isLoadingRecentSongs: Boolean,
    recentlyAddedSongs: List<Song>,
    @DrawableRes fallbackResourceId: Int,
    onSongInRecentlyAddedSongsSelected: ( MediaItem ) -> Unit
) {
    ForYouSongRow(
        heading = language.recentlyAddedSongs,
        isLoading = isLoadingRecentSongs,
        language = language,
        fallbackResourceId = fallbackResourceId,
        songs = recentlyAddedSongs,
        onSongSelected = onSongInRecentlyAddedSongsSelected
    )
}

@Composable
fun MostPlayedSongs(
    language: Language,
    isLoadingMostPlayedSongs: Boolean,
    mostPlayedSongs: List<Song>,
    @DrawableRes fallbackResourceId: Int,
    onSongInMostPlayedSongsSelected: ( MediaItem ) -> Unit,
) {
    ForYouSongRow(
        heading = "Most Played Songs",
        isLoading = isLoadingMostPlayedSongs,
        language = language,
        fallbackResourceId = fallbackResourceId,
        songs = mostPlayedSongs,
        onSongSelected = onSongInMostPlayedSongsSelected
    )
}

@Composable
fun PlayHistory(
    language: Language,
    isLoadingPlayHistory: Boolean,
    songsInPlayHistory: List<Song>,
    @DrawableRes fallbackResourceId: Int,
    onSongInPlayHistorySelected: ( MediaItem ) -> Unit
) {
    ForYouSongRow(
        heading = "Play History",
        isLoading = isLoadingPlayHistory,
        language = language,
        fallbackResourceId = fallbackResourceId,
        songs = songsInPlayHistory,
        onSongSelected = onSongInPlayHistorySelected
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForYouSongRow(
    heading: String,
    isLoading: Boolean,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    songs: List<Song>,
    onSongSelected: ( MediaItem ) -> Unit,
) {
    SideHeading {
        Text( text = heading )
    }
    when {
        isLoading -> Loading()
        songs.isEmpty() -> Empty( language = language )
        else -> {
            BoxWithConstraints {
                val tileWidth = maxWidth.times( 0.7f )
                val tileHeight = 96.dp

                LazyRow (
                    contentPadding = PaddingValues( 20.dp, 4.dp ),
                    horizontalArrangement = Arrangement.spacedBy( 8.dp )
                ) {
                    items( songs ) { song ->
                        ForYouSongCard(
                            modifier = Modifier
                                .width( tileWidth )
                                .height( tileHeight ),
                            song = song,
                            fallbackResourceId = fallbackResourceId,
                            onClick = { onSongSelected( song.mediaItem ) }
                        )
                    }
                }
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun ForYouScreenContentPreview() {
    ForYouScreenContent(
        uiState = testForYouScreenUiState,
        onShuffleAndPlay = {},
        onSettingsClicked = {},
        onSongInPlayHistorySelected = {},
        onSongInMostPlayedSongsSelected = {},
        onSongInRecentlyAddedSongsSelected = {}
    )
}

@Composable
private fun SideHeading( text: @Composable () -> Unit ) {
    Box(
        modifier = Modifier.padding( 20.dp, 12.dp )
    ) {
        ProvideTextStyle( value = MaterialTheme.typography.titleLarge ) {
            text()
        }
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp * 0.2f).dp)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Empty(
    language: Language
) {
    val height = ( LocalConfiguration.current.screenHeightDp * 0.15f ).dp
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = language.damnThisIsSoEmpty,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy( alpha = 0.7f )
            )
        )
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun ForYouSongCard(
    modifier: Modifier = Modifier,
    song: Song,
    @DrawableRes fallbackResourceId: Int,
    onClick: () -> Unit
) {

    val backgroundColor = MaterialTheme.colorScheme.surface

    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = ImageRequest.Builder( LocalContext.current ).apply {
                    data( song.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                backgroundColor.copy(alpha = 0.2f),
                                backgroundColor.copy(alpha = 0.7f),
                                backgroundColor.copy(alpha = 0.8f),
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier.padding( 8.dp )
            ) {
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp)),
                        model = ImageRequest.Builder( LocalContext.current ).apply {
                            data( song.artworkUri )
                            placeholder( fallbackResourceId )
                            fallback( fallbackResourceId )
                            error( fallbackResourceId )
                            crossfade( true )
                        }.build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    backgroundColor.copy(alpha = 0.25f),
                                    CircleShape
                                )
                                .padding(1.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size( 20.dp ),
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if ( song.artists.isNotEmpty() ) {
                        Text(
                            text = song.artists.joinToString(),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
private fun SuggestedAlbums(
    language: Language,
    isLoading: Boolean,
    albums: List<Album>,
    @DrawableRes fallbackResourceId: Int,
) {
    SideHeading {
        Text( text = language.suggestedAlbums )
    }
    Spacer( modifier = Modifier.padding( 0.dp, 8.dp ) )
    StatedSixGrid(
        language = language,
        isLoading = isLoading,
        items = albums
    ) {
        Card(
            onClick = {}
        ) {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                model = ImageRequest.Builder( LocalContext.current ).apply {
                    data( it.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
private fun SuggestedArtists(
    language: Language,
    isLoading: Boolean,
    suggestedArtists: List<Artist>,
    @DrawableRes fallbackResourceId: Int,
) {

    SideHeading {
        Text( text = language.suggestedArtists )
    }
    Spacer( modifier = Modifier.padding( 0.dp, 8.dp ) )
    StatedSixGrid(
        language = language,
        isLoading = isLoading,
        items = suggestedArtists
    ) {
        Card(
            onClick = {}
        ) {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                model = ImageRequest.Builder( LocalContext.current ).apply {
                    data( it.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun <T> StatedSixGrid(
    language: Language,
    isLoading: Boolean,
    items: List<T>,
    content: @Composable ( T ) -> Unit
) {
    when {
        isLoading -> SixGridLoading()
        items.isEmpty() -> SixGridEmpty( language )
        else -> SixGrid( items ) {
            content( it )
        }
    }
}

@Composable
private fun SixGridLoading() {
    Box(
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp * 0.2f).dp)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SixGridEmpty(
    language: Language
) {
    val height = ( LocalConfiguration.current.screenHeightDp * 0.15f ).dp
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = language.damnThisIsSoEmpty,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy( alpha = 0.7f )
            )
        )
    }
}

@Composable
private fun <T> SixGrid(
    items: List<T>,
    content: @Composable ( T ) -> Unit,
) {
    val gap = 12.dp

    Row (
        modifier = Modifier.padding( 20.dp, 0.dp ),
        horizontalArrangement = Arrangement.spacedBy( gap )
    ) {
        ( 0..2 ).forEach { i ->
            val item = items.getOrNull( i )
            Box( modifier = Modifier.weight( 1f ) ) {
                item?.let { content( it ) }
            }
        }
    }
    if ( items.size > 3 ) {
        Spacer( modifier = Modifier.height( gap ) )
        Row (
            modifier = Modifier.padding( 20.dp, 0.dp ),
            horizontalArrangement = Arrangement.spacedBy( gap )
        ) {
            ( 3..5 ).forEach { i ->
                val item = items.getOrNull( i )
                Box( modifier = Modifier.weight( 1f ) ) {
                    item?.let { content( it ) }
                }
            }
        }
    }
}

val testForYouScreenUiState = ForYouScreenUiState(
    language = English,
    themeMode = SettingsDefaults.themeMode,
    isLoadingRecentSongs = false,
    recentlyAddedSongs = testSongs,
    isLoadingSuggestedAlbums = false,
    suggestedAlbums = testAlbums,
    isLoadingMostPlayedSongs = false,
    mostPlayedSongs = testSongs,
    isLoadingSuggestedArtists = false,
    suggestedArtists = testArtists,
    isLoadingPlayHistory = false,
    songsInPlayHistory = testSongs,
)