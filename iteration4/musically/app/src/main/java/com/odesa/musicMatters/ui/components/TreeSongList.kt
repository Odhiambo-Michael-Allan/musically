package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortPathsBy
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.ui.tree.TreeScreenUiState
import com.odesa.musicMatters.ui.tree.label
import com.odesa.musicMatters.ui.tree.testTreeScreenUiState


@Composable
fun TreeSongList(
    uiState: TreeScreenUiState,
    togglePath: ( String ) -> Unit,
    onPlaySong: ( Song ) -> Unit,
    @DrawableRes fallbackResourceId: Int
) {

    MediaSortBarScaffold(
        mediaSortBar = {
            TreeSongListMediaSortBar(
                songsCount = uiState.songsCount,
                sortPathsBy = SortPathsBy.NAME,
                pathsSortReverse = false,
                sortSongsBy = SortSongsBy.ALBUM,
                sortSongsReverse = false,
                language = uiState.language,
                onPathsSortByChange = {},
                onPathsSortReverseChange = {},
                onSongsSortByChange = {},
                onSortSongsReverseChange = {}
            )
        }
    ) {
        when {
            uiState.tree.isEmpty() -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        modifier = modifier,
                        imageVector =Icons.Filled.MusicNote,
                        contentDescription = null
                    )
                }
            ) {
                Text( text = uiState.language.damnThisIsSoEmpty )
            }
            else -> TreeSongListContent(
                uiState = uiState,
                togglePath = togglePath,
                onPlaySong = onPlaySong,
                fallbackResourceId = fallbackResourceId
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun TreeSongListPreview() {
    TreeSongList(
        uiState = testTreeScreenUiState,
        togglePath = {},
        onPlaySong = {},
        fallbackResourceId = R.drawable.placeholder_light
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TreeSongListContent(
    uiState: TreeScreenUiState,
    togglePath: ( String ) -> Unit,
    onPlaySong: ( Song ) -> Unit,
    @DrawableRes fallbackResourceId: Int,
) {

    val lazyListState = rememberLazyListState()

    LazyColumn (
        modifier = Modifier.drawScrollBar( lazyListState ),
        state = lazyListState
    ) {
        uiState.tree.forEach { ( directoryName, songsInDirectory ) ->
            val expanded = !uiState.disabledTreePaths.contains( directoryName )
            val bottomPadding = if ( expanded ) 4.dp else 0.dp

            stickyHeader {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                        .clickable { togglePath(directoryName) }
                        .padding(
                            start = 12.dp,
                            end = 8.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .size( 20.dp )
                        ,
                        imageVector = if ( expanded ) Icons.Filled.ExpandMore
                        else Icons.Filled.ChevronRight,
                        contentDescription = null
                    )
                    Spacer( modifier = Modifier.width( 4.dp ) )
                    Text(
                        modifier = Modifier.weight( 1f ),
                        text = directoryName,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    var showOptionsMenu by remember { mutableStateOf( false ) }
                    TreeSongListSongCardIconButton(
                        icon = { modifier ->
                            Icon(
                                modifier = modifier,
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            showOptionsMenu = !showOptionsMenu
                        }
                    )
                }
            }
            if ( expanded ) {
                items( songsInDirectory ) {
                    val isCurrentlyPlaying by remember( it, uiState.currentlyPlayingSongId ) {
                        derivedStateOf { it.id == uiState.currentlyPlayingSongId }
                    }
                    val isFavorite by remember( uiState.favoriteSongIds, it ) {
                        derivedStateOf { uiState.favoriteSongIds.contains( it.id ) }
                    }
                    Card (
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors( containerColor = Color.Transparent ),
                        onClick = { onPlaySong( it ) }
                    ) {
                        Row (
                            modifier = Modifier
                                .padding(
                                    start = 12.dp,
                                    end = 8.dp,
                                    top = 6.dp,
                                    bottom = 6.dp
                                ),
                            horizontalArrangement = Arrangement.spacedBy( 12.dp ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(5.dp)),
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
                            Column (
                                modifier = Modifier.weight( 1f )
                            ) {
                                Text(
                                    text = it.title,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = when {
                                            isCurrentlyPlaying -> MaterialTheme.colorScheme.primary
                                            else -> LocalTextStyle.current.color
                                        }
                                    )
                                )
                                if ( it.artists.isNotEmpty() ) {
                                    Text(
                                        text = it.artists.joinToString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                            Row {
                                if ( isFavorite ) {
                                    TreeSongListSongCardIconButton(
                                        icon = { modifier ->
                                            Icon(
                                                modifier = modifier,
                                                imageVector = Icons.Filled.Favorite,
                                                tint = MaterialTheme.colorScheme.primary,
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {}
                                    )
                                }
                                Spacer( modifier = Modifier.width( 4.dp ) )
                                var showOptionsMenu by remember { mutableStateOf( false ) }
                                TreeSongListSongCardIconButton(
                                    icon = { modifier ->
                                        Icon(
                                            modifier = modifier,
                                            imageVector = Icons.Filled.MoreVert,
                                            contentDescription = null
                                        )
                                        if ( showOptionsMenu ) {
                                            ModalBottomSheet(
                                                onDismissRequest = {
                                                    showOptionsMenu = false
                                                }
                                            ) {
                                                SongOptionsBottomSheetContent(
                                                    language = uiState.language,
                                                    song = it,
                                                    isFavorite = isFavorite,
                                                    isCurrentlyPlaying = uiState.currentlyPlayingSongId == it.id,
                                                    fallbackResourceId = fallbackResourceId,
                                                    onFavorite = {},
                                                    onAddToQueue = {},
                                                    onPlayNext = { /*TODO*/ },
                                                    onViewArtist = {},
                                                    onViewAlbum = {},
                                                    onShareSong = {}
                                                ) {
                                                    showOptionsMenu = false
                                                }
                                            }
                                        }
                                    },
                                    onClick = {
                                        showOptionsMenu = !showOptionsMenu
                                    }
                                )
                            }
                        }
                    }
                }
            }
            item {
                HorizontalDivider( modifier = Modifier.padding( top = bottomPadding ) )
            }
        }
    }

}

@Preview( showSystemUi = true )
@Composable
fun TreeSongListContentPreview() {
    TreeSongListContent(
        uiState = testTreeScreenUiState,
        fallbackResourceId = R.drawable.placeholder_light,
        togglePath = {},
        onPlaySong = {}
    )
}

@Composable
fun TreeSongListSongCardIconButton(
    icon: @Composable ( Modifier ) -> Unit,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        icon(
            Modifier
                .size(20.dp)
                .padding(start = 6.dp, top = 6.dp)
        )
    }
}

@Composable
private fun TreeSongListMediaSortBar(
    songsCount: Int,
    sortPathsBy: SortPathsBy,
    pathsSortReverse: Boolean,
    sortSongsBy: SortSongsBy,
    sortSongsReverse: Boolean,
    language: Language,
    onPathsSortByChange: (SortPathsBy ) -> Unit,
    onPathsSortReverseChange: ( Boolean ) -> Unit,
    onSongsSortByChange: ( SortSongsBy ) -> Unit,
    onSortSongsReverseChange: ( Boolean ) -> Unit,
) {
    val currentTextStyle = MaterialTheme.typography.bodySmall.run {
        copy( color = MaterialTheme.colorScheme.onSurface )
    }
    var showSortMenu by remember { mutableStateOf( false ) }

    Row (
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    showSortMenu = !showSortMenu
                }
        ) {
            ProvideTextStyle(
                value = currentTextStyle
            ) {
                Row (
                    modifier = Modifier.padding( end = 2.dp ),
                    horizontalArrangement = Arrangement.spacedBy( 8.dp ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size( 12.dp ),
                        imageVector = if ( pathsSortReverse ) Icons.Filled.ArrowDownward
                                else Icons.Filled.ArrowUpward,
                        contentDescription = null
                    )
                    Text( text = sortPathsBy.label( language ) )
                    VerticalDivider(
                        modifier = Modifier
                            .size(9.dp, 12.dp)
                            .padding(4.dp, 0.dp)
                    )
                    Icon(
                        modifier = Modifier.size( 12.dp ),
                        imageVector = if ( sortSongsReverse ) Icons.Filled.ArrowDownward
                                else Icons.Filled.ArrowUpward,
                        contentDescription = null
                    )
                    Text( text = sortSongsBy.label( language ) )
                }
            }
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = {
                    showSortMenu = false
                }
            ) {
                Row {
                    Column (
                        modifier = Modifier.weight( 1f )
                    ) {
                        Text(
                            modifier = Modifier.padding( 16.dp, 8.dp ),
                            text = language.folders,
                            style = currentTextStyle
                        )
                        SortPathsBy.entries.forEach { sortBy ->
                            TreeSongListMediaSortBarDropdownMenuItem(
                                selected = sortPathsBy == sortBy,
                                reversed = pathsSortReverse,
                                text = {
                                    Text( text = sortBy.label( language ) )
                                },
                                onClick = {
                                    when ( sortPathsBy ) {
                                        sortBy -> onPathsSortReverseChange( !pathsSortReverse )
                                        else -> onPathsSortByChange( sortBy )
                                    }
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                    Column (
                        modifier = Modifier.weight( 1f )
                    ) {
                        Text(
                            modifier = Modifier.padding( 16.dp, 8.dp ),
                            text = language.songs,
                            style = currentTextStyle
                        )
                        SortSongsBy.entries.forEach { sortBy ->
                            TreeSongListMediaSortBarDropdownMenuItem(
                                selected = sortSongsBy == sortBy,
                                reversed = sortSongsReverse,
                                text = {
                                    Text( text = sortBy.label( language ) )
                                },
                                onClick = {
                                    when ( sortSongsBy ) {
                                        sortBy -> onSortSongsReverseChange( !sortSongsReverse )
                                        else -> onSongsSortByChange( sortBy )
                                    }
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
        Text(
            modifier = Modifier.padding( 16.dp, 0.dp ),
            text = language.xSongs( songsCount.toString() ),
            style = currentTextStyle,
        )
    }
}

@Preview( showBackground = true )
@Composable
fun TreeSongListMediaSortBarPreview() {
    TreeSongListMediaSortBar(
        songsCount = 50,
        sortPathsBy = SortPathsBy.NAME,
        pathsSortReverse = false,
        sortSongsBy = SortSongsBy.CUSTOM,
        language = English,
        sortSongsReverse = false,
        onPathsSortByChange = {},
        onPathsSortReverseChange = {},
        onSongsSortByChange = {},
        onSortSongsReverseChange = {}
    )
}

@Composable
private fun TreeSongListMediaSortBarDropdownMenuItem(
    selected: Boolean,
    reversed: Boolean,
    text: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        contentPadding = MenuDefaults.DropdownMenuItemContentPadding.run {
            val horizontalPadding = calculateLeftPadding( LayoutDirection.Ltr )
            PaddingValues(
                start = horizontalPadding.div( 2 ),
                end = horizontalPadding.times( 4 )
            )
        },
        leadingIcon = {
            when {
                selected -> IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        imageVector = if ( reversed ) Icons.Filled.ArrowCircleDown
                                else Icons.Filled.ArrowCircleUp,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
                else -> RadioButton(
                    selected = false,
                    onClick = onClick
                )
            }
        },
        text = text,
        onClick = onClick
    )
}