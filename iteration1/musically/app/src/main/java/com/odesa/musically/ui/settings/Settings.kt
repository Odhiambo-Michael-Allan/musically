package com.odesa.musically.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.data.storage.preferences.ForYou
import com.odesa.musically.data.storage.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.storage.preferences.HomeTab
import com.odesa.musically.data.storage.preferences.NowPlayingControlsLayout
import com.odesa.musically.data.storage.preferences.NowPlayingLyricsLayout
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.components.MinimalAppBar
import com.odesa.musically.ui.settings.Interface.BottomBarLabelVisibility
import com.odesa.musically.ui.settings.Interface.ForYou
import com.odesa.musically.ui.settings.Interface.HomeTabs
import com.odesa.musically.ui.settings.appearance.Font
import com.odesa.musically.ui.settings.appearance.FontScale
import com.odesa.musically.ui.settings.appearance.Language
import com.odesa.musically.ui.settings.appearance.MaterialYou
import com.odesa.musically.ui.settings.appearance.PrimaryColor
import com.odesa.musically.ui.settings.appearance.Theme
import com.odesa.musically.ui.settings.components.SettingsSideHeading
import com.odesa.musically.ui.settings.miniPlayer.ShowSeekControls
import com.odesa.musically.ui.settings.miniPlayer.ShowTrackControls
import com.odesa.musically.ui.settings.miniPlayer.TextMarquee
import com.odesa.musically.ui.settings.nowPlaying.ControlsLayout
import com.odesa.musically.ui.settings.nowPlaying.LyricsLayout
import com.odesa.musically.ui.settings.nowPlaying.ShowAudioInformation
import com.odesa.musically.ui.settings.player.FadePlayback
import com.odesa.musically.ui.settings.player.FadePlaybackDuration
import com.odesa.musically.ui.settings.player.FastForwardDuration
import com.odesa.musically.ui.settings.player.FastRewindDuration
import com.odesa.musically.ui.settings.player.IgnoreAudioFocusLoss
import com.odesa.musically.ui.settings.player.PauseOnHeadphonesDisconnect
import com.odesa.musically.ui.settings.player.PlayOnHeadphonesConnect
import com.odesa.musically.ui.settings.player.RequireAudioFocus
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.MusicallyTheme
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBackPressed: () -> Unit
) {
    val settingsScreenUiState by settingsViewModel.uiState.collectAsState()

    SettingsScreenContent(
        uiState = settingsScreenUiState,
        onBackPressed = onBackPressed,
        onLanguageChange = {
                newLanguage -> settingsViewModel.setLanguage( newLanguage.locale )
        },
        onFontChange = {
                newFont -> settingsViewModel.setFont( newFont.name )
        },
        onFontScaleChange = {
                newFontScale -> settingsViewModel.setFontScale( newFontScale )
        },
        onThemeChange = {
                newTheme -> settingsViewModel.setThemeMode( newTheme )
        },
        onUseMaterialYouChange = {
                useMaterialYou -> settingsViewModel.setUseMaterialYou( useMaterialYou )
        },
        onPrimaryColorChange = {
                primaryColorName -> settingsViewModel.setPrimaryColorName( primaryColorName )
        },
        onHomeTabsChange = {
                homeTabs -> settingsViewModel.setHomeTabs( homeTabs )
        },
        onForYouContentChange = {
                forYouContent -> settingsViewModel.setForYouContent( forYouContent )
        },
        onHomePageBottomBarLabelVisibilityChange = {
                value -> settingsViewModel.setHomePageBottomBarLabelVisibility( value )
        },
        onFadePlaybackChange = {
                fadePlayback -> settingsViewModel.setFadePlayback( fadePlayback )
        },
        onFadePlaybackDurationChange = { fadePlaybackDuration ->
            settingsViewModel.setFadePlaybackDuration( fadePlaybackDuration )
        },
        onRequireAudioFocusChange = { requireAudioFocusChange ->
            settingsViewModel.setRequireAudioFocus( requireAudioFocusChange )
        },
        onIgnoreAudioFocusLossChange = { ignoreAudioFocusChange ->
            settingsViewModel.setIgnoreAudioFocusLoss( ignoreAudioFocusChange )
        },
        onPlayOnHeadphonesConnectChange = { playOnHeadphonesChange ->
            settingsViewModel.setPlayOnHeadphonesConnect( playOnHeadphonesChange )
        },
        onPauseOnHeadphonesDisconnectChange = { pauseOnHeadphonesDisconnect ->
            settingsViewModel.setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect )
        },
        onFastRewindDurationChange = { fastRewindDuration ->
            settingsViewModel.setFastRewindDuration( fastRewindDuration.toInt() )
        },
        onFastForwardDurationChange = { fastForwardDuration ->
            settingsViewModel.setFastForwardDuration( fastForwardDuration.toInt() )
        },
        onMiniPlayerShowTrackControlsChange = { showTrackControls ->
            settingsViewModel.setMiniPlayerShowTrackControls( showTrackControls )
        },
        onMiniPlayerShowSeekControlsChange = { showSeekControls ->
            settingsViewModel.setMiniPlayerShowSeekControls( showSeekControls )
        },
        onMiniPlayerTextMarqueeChange = { textMarquee ->
            settingsViewModel.setMiniPlayerTextMarquee( textMarquee )
        },
        onNowPlayingControlsLayoutChange = { nowPlayingControlsLayout ->
            settingsViewModel.setNowPlayingControlsLayout( nowPlayingControlsLayout )
        },
        onNowPlayingLyricsLayoutChange = { nowPlayingLyricsLayout ->
            settingsViewModel.setNowPlayingLyricsLayout( nowPlayingLyricsLayout )
        },
        onShowNowPlayingAudioInformationChange = { showNowPlayingAudioInformation ->
            settingsViewModel.setShowNowPlayingAudioInformation(
                showNowPlayingAudioInformation
            )
        },
        onShowNowPlayingSeekControlsChange = { showNowPlayingSeekControls ->
            settingsViewModel.setShowNowPlayingSeekControls( showNowPlayingSeekControls )
        }
    )
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun SettingsScreenContent(
    uiState: SettingsScreenUiState,
    onBackPressed: () -> Unit,
    onLanguageChange: ( Language ) -> Unit,
    onFontChange: ( MusicallyFont ) -> Unit,
    onFontScaleChange: ( String ) -> Unit,
    onThemeChange: ( ThemeMode ) -> Unit,
    onUseMaterialYouChange: ( Boolean ) -> Unit,
    onPrimaryColorChange: ( String ) -> Unit,
    onHomeTabsChange: ( Set<HomeTab> ) -> Unit,
    onForYouContentChange: ( Set<ForYou> ) -> Unit,
    onHomePageBottomBarLabelVisibilityChange: (HomePageBottomBarLabelVisibility) -> Unit,
    onFadePlaybackChange: ( Boolean ) -> Unit,
    onFadePlaybackDurationChange: ( Float ) -> Unit,
    onRequireAudioFocusChange: ( Boolean ) -> Unit,
    onIgnoreAudioFocusLossChange: ( Boolean ) -> Unit,
    onPlayOnHeadphonesConnectChange: ( Boolean ) -> Unit,
    onPauseOnHeadphonesDisconnectChange: ( Boolean ) -> Unit,
    onFastRewindDurationChange: ( Float ) -> Unit,
    onFastForwardDurationChange: ( Float ) -> Unit,
    onMiniPlayerShowTrackControlsChange: ( Boolean ) -> Unit,
    onMiniPlayerShowSeekControlsChange: ( Boolean ) -> Unit,
    onMiniPlayerTextMarqueeChange: ( Boolean ) -> Unit,
    onNowPlayingControlsLayoutChange: (NowPlayingControlsLayout) -> Unit,
    onNowPlayingLyricsLayoutChange: (NowPlayingLyricsLayout) -> Unit,
    onShowNowPlayingAudioInformationChange: ( Boolean ) -> Unit,
    onShowNowPlayingSeekControlsChange: ( Boolean ) -> Unit,

    ) {
    val snackBarHostState = remember { SnackbarHostState() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MinimalAppBar(
            modifier = Modifier.fillMaxWidth(),
            onNavigationIconClicked = onBackPressed,
            title = uiState.language.settings
        )
        LazyColumn(
            modifier = Modifier.weight( 1f )
        ) {
            item {
                val contentColor = MaterialTheme.colorScheme.onPrimary

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp, 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size( 12.dp )
                        )
                        Spacer( modifier = Modifier.width( 4.dp ) )
                        Text(
                            text = uiState.language.considerContributing,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = contentColor
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(8.dp, 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.East,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size( 20.dp )
                        )
                    }
                }
                SettingsSideHeading( text = uiState.language.appearance )
                Language(
                    language = uiState.language,
                    onLanguageChange = onLanguageChange
                )
                Font(
                    font = uiState.font,
                    language = uiState.language,
                    onFontChange = onFontChange
                )
                FontScale(
                    language = uiState.language,
                    fontScale = uiState.fontScale,
                    onFontScaleChange = onFontScaleChange
                )
                Theme(
                    language = uiState.language,
                    themeMode = uiState.themeMode,
                    onThemeChange = onThemeChange
                )
                MaterialYou(
                    language = uiState.language,
                    useMaterialYou = uiState.useMaterialYou,
                    onUseMaterialYouChange = onUseMaterialYouChange
                )
                PrimaryColor(
                    primaryColor = uiState.primaryColorName,
                    language = uiState.language,
                    onPrimaryColorChange = onPrimaryColorChange,
                    useMaterialYou = uiState.useMaterialYou
                )
                Divider( thickness = 0.5.dp )
                SettingsSideHeading( text = uiState.language.Interface )
                HomeTabs(
                    language = uiState.language,
                    homeTabs = uiState.homeTabs,
                    onHomeTabsChange = onHomeTabsChange
                )
                ForYou(
                    language = uiState.language,
                    forYouContent = uiState.forYouContent,
                    onForYouContentChange = onForYouContentChange
                )
                BottomBarLabelVisibility(
                    value = uiState.homePageBottomBarLabelVisibility,
                    language = uiState.language,
                    onValueChange = onHomePageBottomBarLabelVisibilityChange
                )
                Divider( thickness = 0.5.dp )
                SettingsSideHeading( text = uiState.language.player )
                FadePlayback(
                    language = uiState.language,
                    fadePlayback = uiState.fadePlayback,
                    onFadePlaybackChange = onFadePlaybackChange
                )
                FadePlaybackDuration(
                    language = uiState.language,
                    value = uiState.fadePlaybackDuration,
                    onFadePlaybackDurationChange = onFadePlaybackDurationChange
                )
                RequireAudioFocus(
                    language = uiState.language,
                    requireAudioFocus = uiState.requireAudioFocus,
                    onRequireAudioFocusChange = onRequireAudioFocusChange
                )
                IgnoreAudioFocusLoss(
                    language = uiState.language,
                    ignoreAudioFocusLoss = uiState.ignoreAudioFocusLoss,
                    onIgnoreAudioFocusLossChange = onIgnoreAudioFocusLossChange
                )
                PlayOnHeadphonesConnect(
                    language = uiState.language,
                    playOnHeadphonesConnect = uiState.playOnHeadphonesConnect,
                    onPlayOnHeadphonesConnectChange = onPlayOnHeadphonesConnectChange
                )
                PauseOnHeadphonesDisconnect(
                    language = uiState.language,
                    pauseOnHeadphonesDisconnect = uiState.pauseOnHeadphonesDisconnect,
                    onPauseOnHeadphonesDisconnectChange = onPauseOnHeadphonesDisconnectChange
                )
                FastRewindDuration(
                    language = uiState.language,
                    value = uiState.fastRewindDuration.toFloat(),
                    onFastRewindDurationChange = onFastRewindDurationChange
                )
                FastForwardDuration(
                    language = uiState.language,
                    value = uiState.fastForwardDuration.toFloat(),
                    onFastForwardDurationChange = onFastForwardDurationChange
                )
                Divider( thickness = 0.5.dp )
                SettingsSideHeading( text = uiState.language.miniPlayer )
                ShowTrackControls(
                    value = uiState.miniPlayerShowTrackControls,
                    language = uiState.language,
                    onValueChange = onMiniPlayerShowTrackControlsChange
                )
                ShowSeekControls(
                    language = uiState.language,
                    value = uiState.miniPlayerShowSeekControls,
                    onValueChange = onMiniPlayerShowSeekControlsChange
                )
                TextMarquee(
                    language = uiState.language,
                    value = uiState.miniPlayerTextMarquee,
                    onValueChange = onMiniPlayerTextMarqueeChange
                )
                Divider( thickness = 0.5.dp )
                SettingsSideHeading( text = uiState.language.nowPlaying )
                ControlsLayout(
                    nowPlayingControlsLayout = uiState.nowPlayingControlsLayout,
                    language = uiState.language,
                    onNowPlayingControlsLayoutChange = onNowPlayingControlsLayoutChange
                )
                LyricsLayout(
                    nowPlayingLyricsLayout = uiState.nowPlayingLyricsLayout,
                    language = uiState.language,
                    onNowPlayingLyricsLayoutChange = onNowPlayingLyricsLayoutChange
                )
                ShowAudioInformation(
                    language = uiState.language,
                    value = uiState.showNowPlayingAudioInformation,
                    onValueChange = onShowNowPlayingAudioInformationChange
                )
                ShowSeekControls(
                    language = uiState.language,
                    value = uiState.showNowPlayingSeekControls,
                    onValueChange = onShowNowPlayingSeekControlsChange
                )
                Divider( thickness = 0.5.dp )
                SettingsSideHeading( text = uiState.language.groove )
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun SettingsScreenContentPreview() {
    val uiState = SettingsScreenUiState(
        language = English,
        font = SupportedFonts.ProductSans,
        fontScale = SettingsDefaults.fontScale,
        themeMode = ThemeMode.LIGHT,
        useMaterialYou = SettingsDefaults.useMaterialYou,
        primaryColorName = "Blue",
        homeTabs = SettingsDefaults.homeTabs,
        forYouContent = SettingsDefaults.forYouContents,
        homePageBottomBarLabelVisibility = SettingsDefaults.homePageBottomBarLabelVisibility,
        fadePlayback = SettingsDefaults.fadePlayback,
        fadePlaybackDuration = SettingsDefaults.fadePlaybackDuration,
        requireAudioFocus = SettingsDefaults.requireAudioFocus,
        ignoreAudioFocusLoss = SettingsDefaults.ignoreAudioFocusLoss,
        playOnHeadphonesConnect = SettingsDefaults.playOnHeadphonesConnect,
        pauseOnHeadphonesDisconnect = SettingsDefaults.pauseOnHeadphonesDisconnect,
        fastRewindDuration = SettingsDefaults.fastRewindDuration,
        fastForwardDuration = SettingsDefaults.fastForwardDuration,
        miniPlayerShowTrackControls = SettingsDefaults.miniPlayerShowTrackControls,
        miniPlayerShowSeekControls = SettingsDefaults.miniPlayerShowSeekControls,
        miniPlayerTextMarquee = SettingsDefaults.miniPlayerTextMarquee,
        nowPlayingControlsLayout = SettingsDefaults.nowPlayingControlsLayout,
        nowPlayingLyricsLayout = SettingsDefaults.nowPlayingLyricsLayout,
        showNowPlayingAudioInformation = SettingsDefaults.showNowPlayingAudioInformation,
        showNowPlayingSeekControls = SettingsDefaults.showNowPlayingSeekControls,
    )
    MusicallyTheme(
        themeMode = ThemeMode.LIGHT,
        primaryColorName = SettingsDefaults.primaryColorName,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.fontScale,
        useMaterialYou = SettingsDefaults.useMaterialYou
    ) {
        SettingsScreenContent(
            uiState = uiState,
            onBackPressed = {},
            onLanguageChange = {},
            onFontChange = {},
            onFontScaleChange = {},
            onThemeChange = {},
            onUseMaterialYouChange = {},
            onPrimaryColorChange = {},
            onHomeTabsChange = {},
            onForYouContentChange = {},
            onHomePageBottomBarLabelVisibilityChange = {},
            onFadePlaybackChange = {},
            onFadePlaybackDurationChange = {},
            onRequireAudioFocusChange = {},
            onIgnoreAudioFocusLossChange = {},
            onPlayOnHeadphonesConnectChange = {},
            onPauseOnHeadphonesDisconnectChange = {},
            onFastRewindDurationChange = {},
            onFastForwardDurationChange = {},
            onMiniPlayerShowTrackControlsChange = {},
            onMiniPlayerShowSeekControlsChange = {},
            onMiniPlayerTextMarqueeChange = {},
            onNowPlayingControlsLayoutChange = {},
            onNowPlayingLyricsLayoutChange = {},
            onShowNowPlayingAudioInformationChange = {},
            onShowNowPlayingSeekControlsChange = {}
        )
    }
}