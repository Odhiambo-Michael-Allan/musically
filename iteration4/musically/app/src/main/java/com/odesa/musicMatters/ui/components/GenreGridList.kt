package com.odesa.musicMatters.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.google.common.collect.ImmutableList
import com.odesa.musicMatters.data.preferences.GenreSortBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import java.util.UUID

private object GenreTile {
    val colors = mutableListOf(
        0xFFEF4444,
        0xFFF97316,
        0xFFF59E0B,
        0xFF16A34A,
        0xFF06B6B4,
        0xFF8B5CF6,
        0xFFD946EF,
        0xFFF43F5E,
        0xFF6366F1,
        0xFFA855F7,
    ).map { Color( it ) }

    fun colorAt( index: Int ) = colors[ index % colors.size ]

    @Composable
    fun cardColors( index: Int ) = CardDefaults.cardColors(
        containerColor = colorAt( index ),
        contentColor = Color.White,
    )
}

@Composable
fun GenreGridList(
    genres: List<MediaItem>,
    language: Language,
    sortType: GenreSortBy,
    sortReverse: Boolean,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( GenreSortBy ) -> Unit,
) {
    MediaSortBarScaffold(
        mediaSortBar = {
            Box(
                modifier = Modifier.padding( bottom = 4.dp )
            ) {
                MediaSortBar(
                    sortReverse = sortReverse,
                    onSortReverseChange = onSortReverseChange,
                    sortType = sortType,
                    sortTypes = GenreSortBy.entries.associateBy( { it }, { it.label( language ) } ),
                    onSortTypeChange = onSortTypeChange,
                    label = {
                        Text(
                            text = language.xGenres(
                                genres.size.toString()
                            )
                        )
                    }
                )
            }
        }
    ) {
        when {
            genres.isEmpty() -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.Filled.MusicNote,
                        contentDescription = null
                    )
                },
                content = {
                    Text( language.damnThisIsSoEmpty )
                }
            )
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive( 150.dp ),
                    horizontalArrangement = Arrangement.spacedBy( 4.dp ),
                    verticalArrangement = Arrangement.spacedBy( 4.dp ),
                    contentPadding = PaddingValues( 8.dp )
                ) {
                    itemsIndexed( genres ) { index, genre ->
                        GenreCard(
                            genre = genre.mediaMetadata.title!!.toString(),
                            numberOfSongsString = language.xSongs( "10" ),
                            position = index,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun GenreCard(
    genre: String,
    numberOfSongsString: String,
    position: Int,
    onClick: () -> Unit,
) {
    Card (
        modifier = Modifier
            .padding( 2.dp ),
        colors = GenreTile.cardColors( index = position ),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .defaultMinSize(minHeight = 88.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .matchParentSize()
                    .fillMaxWidth()
                    .alpha(0.25f)
                    .absoluteOffset(8.dp, 12.dp)
            ) {
                Text(
                    text = genre,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.displaySmall
                        .copy( fontWeight = FontWeight.Bold ),
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )

            }
            Column(
                modifier = Modifier.padding( 20.dp ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = genre,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                        .copy( fontWeight = FontWeight.Bold )
                )
                Text(
                    text = numberOfSongsString,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview( showBackground = true )
@Composable
fun GenreCardPreview() {
    GenreCard(
        genre = "Hip Hop",
        numberOfSongsString = English.xSongs( "10" ),
        position = 0,
        onClick = {}
    )
}



@Preview( showSystemUi = true )
@Composable
fun GenreGridPreview() {
    GenreGridList(
        genres = testGenreList,
        language = SettingsDefaults.language,
        sortType = GenreSortBy.GENRE,
        sortReverse = false,
        onSortReverseChange = {},
        onSortTypeChange = {},
    )
}

fun GenreSortBy.label( language: Language ) = when ( this ) {
    GenreSortBy.GENRE -> language.genre
    GenreSortBy.CUSTOM -> language.custom
    GenreSortBy.TRACKS_COUNT -> language.trackCount
}

val testGenreList: ImmutableList<MediaItem> = ImmutableList.of(
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Hip Hop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Pop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "RnB" )
            }.build()
        )
    }.build()
)