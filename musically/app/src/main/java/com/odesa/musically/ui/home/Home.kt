package com.odesa.musically.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.components.TopAppBarMinimalTitle
import com.odesa.musically.ui.components.swipeable
import com.odesa.musically.ui.helpers.ScaleTransition
import com.odesa.musically.ui.helpers.SlideTransition
import com.odesa.musically.ui.home.page.AlbumArtistsPage
import com.odesa.musically.ui.home.page.AlbumsPage
import com.odesa.musically.ui.home.page.ArtistsPage
import com.odesa.musically.ui.home.page.FoldersPage
import com.odesa.musically.ui.home.page.ForYouPage
import com.odesa.musically.ui.home.page.GenresPage
import com.odesa.musically.ui.home.page.PlaylistsPage
import com.odesa.musically.ui.home.page.SongsPage
import com.odesa.musically.ui.home.page.TreePage

@Composable
fun HomeScreen(
    onSettingsClicked: () -> Unit
) {
    HomeScreenContent(
        uiState = uiState,
        onSearchClicked = { /*TODO*/ },
        onRefreshClicked = { /*TODO*/ },
        onSettingsClicked = onSettingsClicked
    )
}
@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    onSearchClicked: () -> Unit,
    onRefreshClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    var currentTab by remember { mutableStateOf( uiState.homeTabs.first() ) }
    var showOptionsDropdownMenu by remember { mutableStateOf( false ) }
    var showTabsSheet by remember { mutableStateOf( false ) }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onSearchClicked
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Crossfade(
                        targetState = currentTab.label( uiState.language ),
                        label = "home-title"
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TopAppBarMinimalTitle {
                                Text( text = it )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showOptionsDropdownMenu = !showOptionsDropdownMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                        DropdownMenu(
                            expanded = showOptionsDropdownMenu,
                            onDismissRequest = { showOptionsDropdownMenu = false }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = uiState.language.rescan
                                    )
                                },
                                text = { Text( text = uiState.language.rescan ) },
                                onClick = {
                                    onRefreshClicked()
                                    showOptionsDropdownMenu = false
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = uiState.language.settings
                                    )
                                },
                                text = { Text( text = uiState.language.settings ) },
                                onClick = {
                                    onSettingsClicked()
                                    showOptionsDropdownMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Column {
//                NowPlayingBottomBar( false )
                NavigationBar(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                showTabsSheet = true
                            }
                        }
                        .swipeable(
                            onSwipeUp = {
                                showTabsSheet = true
                            }
                        )
                ) {
                    Spacer( modifier = Modifier.width( 2.dp ) )
                    uiState.homeTabs.map { homeTab ->
                        val isSelected = currentTab == homeTab
                        val label = homeTab.label( uiState.language )
                        NavigationBarItem(
                            selected = isSelected,
                            alwaysShowLabel = uiState.labelVisibility == HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
                            onClick = {
                                showTabsSheet = isSelected
                                currentTab = homeTab
                            },
                            icon = {
                                Crossfade(
                                    targetState = isSelected,
                                    label = "home-bottom-bar"
                                ) {
                                    Icon(
                                        imageVector = if ( it ) homeTab.selectedIcon() else homeTab.unselectedIcon(),
                                        contentDescription = label
                                    )
                                }
                            },
                            label = when ( uiState.labelVisibility ) {
                                HomePageBottomBarLabelVisibility.INVISIBLE -> null
                                else -> ( {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        softWrap = false,
                                    )
                                } )
                            }
                        )
                    }
                    Spacer( modifier = Modifier.width( 2.dp ) )
                }
            }
        }
    ) { contentPadding ->
        AnimatedContent(
            label = "home-content",
            targetState = currentTab,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            transitionSpec = {
                SlideTransition.slideUp.enterTransition()
                    .togetherWith( ScaleTransition.scaleDown.exitTransition() )
            }
        ) { homeUiPage ->
            when ( homeUiPage ) {
                HomeUiPage.ForYou -> ForYouPage()
                HomeUiPage.Songs -> SongsPage()
                HomeUiPage.Albums -> AlbumsPage()
                HomeUiPage.Artists -> ArtistsPage()
                HomeUiPage.AlbumArtists -> AlbumArtistsPage()
                HomeUiPage.Genres -> GenresPage()
                HomeUiPage.Folders -> FoldersPage()
                HomeUiPage.Playlists -> PlaylistsPage()
                HomeUiPage.Tree -> TreePage()
            }
        }
    }
    if ( showTabsSheet ) {
        val sheetState = rememberModalBottomSheetState()
        val orderedTabs = remember {
            setOf( *uiState.homeTabs.toTypedArray(), *HomeUiPage.entries.toTypedArray() )
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showTabsSheet = false }
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding( 6.dp ),
                columns = GridCells.Fixed( uiState.homeTabs.size ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy( 8.dp ),
            ) {
                items( orderedTabs.toList(), key = { it.ordinal } ) {
                    val isSelected = it == currentTab
                    val label = it.label( uiState.language )
                    val containerColor = when {
                        isSelected -> MaterialTheme.colorScheme.secondaryContainer
                        else -> Color.Unspecified
                    }
                    val contentColor = when {
                        isSelected -> MaterialTheme.colorScheme.onSecondaryContainer
                        else -> Color.Unspecified
                    }
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp, 0.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                currentTab = it
                                showTabsSheet = false
                            }
                            .background(containerColor)
                            .padding(0.dp, 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when {
                            isSelected -> Icon(
                                imageVector = it.selectedIcon(),
                                contentDescription = label,
                                tint = contentColor
                            )
                            else -> Icon(
                                imageVector = it.unselectedIcon(),
                                contentDescription = label
                            )
                        }
                        Spacer( modifier = Modifier.height( 8.dp ) )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall.copy( color = contentColor ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Spacer( modifier = Modifier.height( 48.dp ) )
        }
    }
}

fun HomeTab.toHomeUiPage() = when( this ) {
    HomeTab.ForYou -> HomeUiPage.ForYou
    HomeTab.Songs -> HomeUiPage.Songs
    HomeTab.Tree -> HomeUiPage.Tree
    HomeTab.Playlists -> HomeUiPage.Playlists
    HomeTab.Folders -> HomeUiPage.Folders
    HomeTab.Artists -> HomeUiPage.Artists
    HomeTab.AlbumArtists -> HomeUiPage.AlbumArtists
    HomeTab.Albums -> HomeUiPage.Albums
    HomeTab.Genres -> HomeUiPage.Genres
}

data class HomeScreenUiState(
    val language: Language,
    val homeTabs: Set<HomeUiPage>,
    val labelVisibility: HomePageBottomBarLabelVisibility,
)

val uiState = HomeScreenUiState(
    language = English,
    homeTabs = SettingsDefaults.homeTabs.map { it.toHomeUiPage() }.toSet(),
    labelVisibility = SettingsDefaults.homePageBottomBarLabelVisibility,
)

enum class HomeUiPage(
    val label: ( Language ) -> String,
    val selectedIcon: @Composable () -> ImageVector,
    val unselectedIcon: @Composable () -> ImageVector
) {
    ForYou(
        label = { it.forYou },
        selectedIcon = { Icons.Filled.Face },
        unselectedIcon = { Icons.Outlined.Face }
    ),
    Songs(
        label = { it.songs },
        selectedIcon = { Icons.Filled.MusicNote },
        unselectedIcon = { Icons.Outlined.MusicNote }
    ),
    Artists(
        label = { it.artists },
        selectedIcon = { Icons.Filled.Group },
        unselectedIcon = { Icons.Outlined.Group }
    ),
    Albums(
        label = { it.albums },
        selectedIcon = { Icons.Filled.Album },
        unselectedIcon = { Icons.Outlined.Album }
    ),
    AlbumArtists(
        label = { it.albumArtists },
        selectedIcon = { Icons.Filled.SupervisorAccount },
        unselectedIcon = { Icons.Outlined.SupervisorAccount }
    ),
    Genres(
        label = { it.genres },
        selectedIcon = { Icons.Filled.Tune },
        unselectedIcon = { Icons.Outlined.Tune }
    ),
    Folders(
        label = { it.folders },
        selectedIcon = { Icons.Filled.Folder },
        unselectedIcon = { Icons.Outlined.Folder }
    ),
    Playlists(
        label = { it.playlists },
        selectedIcon = { Icons.Filled.QueueMusic },
        unselectedIcon = { Icons.Outlined.QueueMusic }
    ),
    Tree(
        label = { it.tree },
        selectedIcon = { Icons.Filled.AccountTree },
        unselectedIcon = { Icons.Outlined.AccountTree }
    )
}

@Preview( showSystemUi = true )
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        uiState = uiState,
        onRefreshClicked = {},
        onSearchClicked = {},
        onSettingsClicked = {}
    )
}