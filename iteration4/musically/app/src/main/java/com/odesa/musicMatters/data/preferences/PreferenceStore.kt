package com.odesa.musicMatters.data.preferences

import com.odesa.musicMatters.data.preferences.impl.LoopMode
import com.odesa.musicMatters.ui.theme.ThemeMode

interface PreferenceStore {

    suspend fun setLanguage( localeCode: String )
    fun getLanguage(): String

    suspend fun setFontName( fontName: String )
    fun getFontName(): String

    suspend fun setFontScale( fontScale: Float )
    fun getFontScale(): Float

    suspend fun setThemeMode( themeMode: ThemeMode )
    fun getThemeMode(): ThemeMode

    fun getUseMaterialYou(): Boolean
    suspend fun setUseMaterialYou( useMaterialYou: Boolean )

    suspend fun setPrimaryColorName( primaryColorName: String )
    fun getPrimaryColorName(): String

    fun getHomeTabs(): Set<HomeTab>
    suspend fun setHomeTabs( homeTabs: Set<HomeTab> )

    fun getForYouContents(): Set<ForYou>
    suspend fun setForYouContents( forYouContents: Set<ForYou> )

    fun getHomePageBottomBarLabelVisibility(): HomePageBottomBarLabelVisibility
    suspend fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility)

    fun getFadePlayback(): Boolean
    suspend fun setFadePlayback( fadePlayback: Boolean )

    fun getFadePlaybackDuration(): Float
    suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float )

    fun getRequireAudioFocus(): Boolean
    suspend fun setRequireAudioFocus( requireAudioFocus: Boolean )

    fun getIgnoreAudioFocusLoss(): Boolean
    suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean )

    fun getPlayOnHeadphonesConnect(): Boolean
    suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean )

    fun getPauseOnHeadphonesDisconnect(): Boolean
    suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean )

    fun getFastRewindDuration(): Int
    suspend fun setFastRewindDuration( fastRewindDuration: Int )

    fun getFastForwardDuration(): Int
    suspend fun setFastForwardDuration( fastForwardDuration: Int )

    fun getMiniPlayerShowTrackControls(): Boolean
    suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean )

    fun getMiniPlayerShowSeekControls(): Boolean
    suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean )

    fun getMiniPlayerTextMarquee(): Boolean
    suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean )

    fun getControlsLayoutIsDefault(): Boolean
    suspend fun setControlsLayoutIsDefault( controlsLayoutIsDefault: Boolean )

    fun getNowPlayingLyricsLayout(): NowPlayingLyricsLayout
    suspend fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout)

    fun getShowNowPlayingAudioInformation(): Boolean
    suspend fun setShowNowPlayingAudioInformation( showNowPlayingAudioInformation: Boolean )

    fun getShowNowPlayingSeekControls(): Boolean
    suspend fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean )

    suspend fun setSortSongsBy( sortSongsBy: SortSongsBy)
    fun getSortSongsBy(): SortSongsBy

    suspend fun setSortSongsInReverse( sortSongsInReverse: Boolean )
    fun getSortSongsInReverse(): Boolean

    suspend fun setCurrentPlayingSpeed( currentPlayingSpeed: Float )
    fun getCurrentPlayingSpeed(): Float

    suspend fun setCurrentPlayingPitch( currentPlayingPitch: Float )
    fun getCurrentPlayingPitch(): Float

    suspend fun setCurrentLoopMode( loopMode: LoopMode)
    fun getCurrentLoopMode(): LoopMode

    suspend fun setShuffle( shuffle: Boolean )
    fun getShuffle(): Boolean

    suspend fun setShowLyrics( showLyrics: Boolean )
    fun getShowLyrics(): Boolean

    suspend fun setCurrentlyDisabledTreePaths(paths: List<String> )
    fun getCurrentlyDisabledTreePaths(): List<String>

}

enum class HomeTab {
    ForYou,
    Songs,
    Artists,
    Albums,
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

enum class NowPlayingLyricsLayout {
    ReplaceArtwork,
    SeparatePage
}

enum class SortSongsBy {
    CUSTOM,
    TITLE,
    ARTIST,
    ALBUM,
    DURATION,
    DATE_ADDED,
    DATE_MODIFIED,
    COMPOSER,
    ALBUM_ARTIST,
    YEAR,
    FILENAME,
    TRACK_NUMBER,
}

enum class GenreSortBy {
    CUSTOM,
    GENRE,
    TRACKS_COUNT,
}

enum class PlaylistSortBy {
    CUSTOM,
    TITLE,
    TRACKS_COUNT,
}

enum class SortPathsBy {
    CUSTOM,
    NAME
}