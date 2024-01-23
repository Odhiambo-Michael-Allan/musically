package com.odesa.musically.data.preferences.storage

import com.odesa.musically.ui.theme.ThemeMode

interface PreferenceStore {
    fun setLanguage( localeCode: String )
    fun getLanguage(): String
    fun setFontName( fontName: String )
    fun getFontName(): String
    fun setFontScale( fontScale: Float )
    fun getFontScale(): Float
    fun setThemeMode( themeMode: ThemeMode )
    fun getThemeMode(): ThemeMode
    fun getUseMaterialYou(): Boolean
    fun setUseMaterialYou( useMaterialYou: Boolean )
    fun setPrimaryColorName( primaryColorName: String )
    fun getPrimaryColorName(): String
    fun getHomeTabs(): Set<HomeTab>
    fun setHomeTabs( homeTabs: Set<HomeTab> )
    fun getForYouContents(): Set<ForYou>
    fun setForYouContents( forYouContents: Set<ForYou> )
    fun getHomePageBottomBarLabelVisibility(): HomePageBottomBarLabelVisibility
    fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility )
    fun getFadePlayback(): Boolean
    fun setFadePlayback( fadePlayback: Boolean )
    fun getFadePlaybackDuration(): Float
    fun setFadePlaybackDuration( fadePlaybackDuration: Float )
    fun getRequireAudioFocus(): Boolean
    fun setRequireAudioFocus( requireAudioFocus: Boolean )
    fun getIgnoreAudioFocusLoss(): Boolean
    fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean )
    fun getPlayOnHeadphonesConnect(): Boolean
    fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean )
    fun getPauseOnHeadphonesDisconnect(): Boolean
    fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean )
    fun getFastRewindDuration(): Int
    fun setFastRewindDuration( fastRewindDuration: Int )
    fun getFastForwardDuration(): Int
    fun setFastForwardDuration( fastForwardDuration: Int )


}

enum class HomeTab {
    ForYou,
    Songs,
    Artists,
    Albums,
    AlbumArtists,
    Genres,
    Folders,
    Playlists,
    Tree
}

enum class ForYou {
    Albums,
    Artists,
    AlbumArtists
}

enum class HomePageBottomBarLabelVisibility {
    ALWAYS_VISIBLE,
    VISIBLE_WHEN_ACTIVE,
    INVISIBLE
}