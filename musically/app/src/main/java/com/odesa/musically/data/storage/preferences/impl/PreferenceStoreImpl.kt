package com.odesa.musically.data.storage.preferences.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.data.storage.preferences.ForYou
import com.odesa.musically.data.storage.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.storage.preferences.HomeTab
import com.odesa.musically.data.storage.preferences.NowPlayingControlsLayout
import com.odesa.musically.data.storage.preferences.NowPlayingLyricsLayout
import com.odesa.musically.data.storage.preferences.PreferenceStore
import com.odesa.musically.services.i18n.English
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode

class PreferenceStoreImpl( private val context: Context ) : PreferenceStore {

    override fun setLanguage( localeCode: String ) {
        getSharedPreferences().edit {
            putString(SettingsKeys.language, localeCode )
        }
    }

    override fun getLanguage(): String {
        val language = getSharedPreferences().getString(
            SettingsKeys.language, null
        )
        return language ?: SettingsDefaults.language.locale
    }

    override fun setFontName( fontName: String ) {
        getSharedPreferences().edit {
            putString(SettingsKeys.fontName, fontName )
        }
    }

    override fun getFontName() = getSharedPreferences().getString(
        SettingsKeys.fontName, SettingsDefaults.font.name
    ) ?: SettingsDefaults.font.name

    override fun setFontScale( fontScale: Float ) {
        getSharedPreferences().edit {
            putFloat(SettingsKeys.fontScale, fontScale )
        }
    }

    override fun getFontScale() = getSharedPreferences().getFloat(
        SettingsKeys.fontScale, SettingsDefaults.fontScale
    )

    override fun setThemeMode( themeMode: ThemeMode ) {
        getSharedPreferences().edit {
            putString(SettingsKeys.themeMode, themeMode.name )
        }
    }

    override fun getThemeMode() = getSharedPreferences().getString(SettingsKeys.themeMode, null )
        ?.let { ThemeMode.valueOf( it ) }
        ?: SettingsDefaults.themeMode

    override fun getUseMaterialYou() = getSharedPreferences().getBoolean(
        SettingsKeys.useMaterialYou, SettingsDefaults.useMaterialYou
    )

    override fun setUseMaterialYou( useMaterialYou: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.useMaterialYou, useMaterialYou )
        }
    }

    override fun setPrimaryColorName( primaryColorName: String ) {
        getSharedPreferences().edit {
            putString(SettingsKeys.primaryColorName, primaryColorName )
        }
    }

    override fun getPrimaryColorName() = getSharedPreferences()
        .getString(SettingsKeys.primaryColorName, null ) ?: SettingsDefaults.primaryColorName

    override fun getHomeTabs() = getSharedPreferences()
        .getString(SettingsKeys.homeTabs, null )
        ?.split( "," )
        ?.mapNotNull { parseEnumValue<HomeTab>( it ) }
        ?.toSet()
        ?: SettingsDefaults.homeTabs

    override fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        getSharedPreferences().edit {
            putString(SettingsKeys.homeTabs, homeTabs.joinToString( "," ) { it.name } )
        }
    }

    override fun getForYouContents() = getSharedPreferences()
        .getString(SettingsKeys.forYouContents, null )
        ?.split( "," )
        ?.mapNotNull { parseEnumValue<ForYou>( it ) }
        ?.toSet()
        ?: SettingsDefaults.forYouContents

    override fun setForYouContents(forYouContents: Set<ForYou>) {
        getSharedPreferences().edit {
            putString(
                SettingsKeys.forYouContents,
                forYouContents.joinToString( "," ) { it.name } )
        }
    }

    override fun getHomePageBottomBarLabelVisibility() = getSharedPreferences()
        .getEnum(SettingsKeys.homePageBottomBarLabelVisibility, null )
        ?: SettingsDefaults.homePageBottomBarLabelVisibility

    override fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility) {
        getSharedPreferences().edit {
            putEnum(SettingsKeys.homePageBottomBarLabelVisibility, value )
        }
    }

    override fun getFadePlayback() = getSharedPreferences().getBoolean(
        SettingsKeys.fadePlayback,
        SettingsDefaults.fadePlayback
    )

    override fun setFadePlayback( fadePlayback: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.fadePlayback, fadePlayback )
        }
    }

    override fun getFadePlaybackDuration() = getSharedPreferences().getFloat(
        SettingsKeys.fadePlaybackDuration,
        SettingsDefaults.fadePlaybackDuration
    )

    override fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        getSharedPreferences().edit {
            putFloat(SettingsKeys.fadePlaybackDuration, fadePlaybackDuration )
        }
    }

    override fun getRequireAudioFocus() = getSharedPreferences()
        .getBoolean(SettingsKeys.requireAudioFocus, SettingsDefaults.requireAudioFocus)
    override fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.requireAudioFocus, requireAudioFocus )
        }
    }

    override fun getIgnoreAudioFocusLoss() = getSharedPreferences()
        .getBoolean(SettingsKeys.ignoreAudioFocusLoss, SettingsDefaults.ignoreAudioFocusLoss)

    override fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.ignoreAudioFocusLoss, ignoreAudioFocusLoss )
        }
    }

    override fun getPlayOnHeadphonesConnect() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.playOnHeadphonesConnect,
            SettingsDefaults.playOnHeadphonesConnect
        )

    override fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.playOnHeadphonesConnect, playOnHeadphonesConnect )
        }
    }

    override fun getPauseOnHeadphonesDisconnect() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.pauseOnHeadphonesDisconnect,
            SettingsDefaults.pauseOnHeadphonesDisconnect
        )

    override fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(
                SettingsKeys.pauseOnHeadphonesDisconnect,
                pauseOnHeadphonesDisconnect )
        }
    }

    override fun getFastRewindDuration() = getSharedPreferences().getInt(
        SettingsKeys.fastRewindDuration, SettingsDefaults.fastRewindDuration
    )

    override fun setFastRewindDuration( fastRewindDuration: Int ) {
        getSharedPreferences().edit {
            putInt(SettingsKeys.fastRewindDuration, fastRewindDuration )
        }
    }

    override fun getFastForwardDuration() = getSharedPreferences().getInt(
        SettingsKeys.fastForwardDuration, SettingsDefaults.fastForwardDuration
    )

    override fun setFastForwardDuration( fastForwardDuration: Int ) {
        getSharedPreferences().edit {
            putInt(SettingsKeys.fastForwardDuration, fastForwardDuration )
        }
    }

    override fun getMiniPlayerShowTrackControls() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.miniPlayerShowTrackControls,
            SettingsDefaults.miniPlayerShowTrackControls
        )

    override fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(
                SettingsKeys.miniPlayerShowTrackControls,
                showTrackControls )
        }
    }

    override fun getMiniPlayerShowSeekControls() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.miniPlayerShowSeekControls,
            SettingsDefaults.miniPlayerShowSeekControls
        )

    override fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(
                SettingsKeys.miniPlayerShowSeekControls,
                showSeekControls )
        }
    }

    override fun getMiniPlayerTextMarquee() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.miniPlayerTextMarquee,
            SettingsDefaults.miniPlayerTextMarquee
        )

    override fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.miniPlayerTextMarquee, textMarquee )
        }
    }

    override fun getNowPlayingControlsLayout() = getSharedPreferences().getEnum(
        SettingsKeys.nowPlayingControlsLayout, null
    ) ?: SettingsDefaults.nowPlayingControlsLayout


    override fun setNowPlayingControlsLayout( nowPlayingControlsLayout: NowPlayingControlsLayout) {
        getSharedPreferences().edit {
            putEnum(SettingsKeys.nowPlayingControlsLayout, nowPlayingControlsLayout )
        }
    }

    override fun getNowPlayingLyricsLayout() = getSharedPreferences().getEnum(
        SettingsKeys.nowPlayingLyricsLayout, null
    ) ?: SettingsDefaults.nowPlayingLyricsLayout

    override fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout) {
        getSharedPreferences().edit {
            putEnum(SettingsKeys.nowPlayingLyricsLayout, nowPlayingLyricsLayout )
        }
    }

    override fun getShowNowPlayingAudioInformation() = getSharedPreferences().getBoolean(
        SettingsKeys.showNowPlayingAudioInformation, SettingsDefaults.showNowPlayingAudioInformation
    )

    override fun setShowNowPlayingAudioInformation( showNowPlayingAudioInformation: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(
                SettingsKeys.showNowPlayingAudioInformation,
                showNowPlayingAudioInformation
            )
        }
    }

    override fun getShowNowPlayingSeekControls() = getSharedPreferences().getBoolean(
        SettingsKeys.showNowPlayingSeekControls, SettingsDefaults.showNowPlayingSeekControls
    )

    override fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.showNowPlayingSeekControls, showNowPlayingSeekControls )
        }
    }

    override fun setSortSongsBy(sortSongsBy: SortSongsBy) {
        getSharedPreferences().edit {
            putEnum(SettingsKeys.sortSongsBy, sortSongsBy )
        }
    }

    override fun getSortSongsBy() = getSharedPreferences()
        .getEnum(SettingsKeys.sortSongsBy, null ) ?: SettingsDefaults.sortSongsBy

    override fun setSortSongsInReverse(sortSongsInReverse: Boolean ) {
        getSharedPreferences().edit {
            putBoolean(SettingsKeys.sortSongsInReverse, sortSongsInReverse )
        }
    }

    override fun getSortSongsInReverse() = getSharedPreferences()
        .getBoolean(SettingsKeys.sortSongsInReverse, SettingsDefaults.sortSongsInReverse)

    private fun getSharedPreferences() = context.getSharedPreferences(
        SettingsKeys.identifier,
        Context.MODE_PRIVATE
    )
}

object SettingsKeys {
    const val identifier = "musically_settings"
    const val language = "language"
    const val fontName = "font_name"
    const val fontScale = "font_scale"
    const val themeMode = "theme_mode"
    const val useMaterialYou = "use_material_you"
    const val primaryColorName = "primary_color_name"
    const val homeTabs = "home_tabs"
    const val forYouContents = "for_you_contents"
    const val homePageBottomBarLabelVisibility = "home_page_bottom_bar_visibility"
    const val fadePlayback = "fade_playback"
    const val fadePlaybackDuration = "fade_playback_duration"
    const val requireAudioFocus = "require_audio_focus"
    const val ignoreAudioFocusLoss = "ignore_audio_focus_loss"
    const val playOnHeadphonesConnect = "play_on_headphones_connect"
    const val pauseOnHeadphonesDisconnect = "pause_on_headphones_disconnect"
    const val fastForwardDuration = "fast_forward_duration"
    const val fastRewindDuration = "fast_rewind_duration"
    const val miniPlayerShowTrackControls = "mini_player_show_track_controls"
    const val miniPlayerShowSeekControls = "mini_player_show_seek_controls"
    const val miniPlayerTextMarquee = "mini_player_text_marquee"
    const val nowPlayingControlsLayout = "now_playing_controls_layout"
    const val nowPlayingLyricsLayout = "now_playing_lyrics_layout"
    const val showNowPlayingAudioInformation = "show_now_playing_audio_information"
    const val showNowPlayingSeekControls = "show_now_playing_seek_controls"

    const val sortSongsBy = "sort_songs_by"
    const val sortSongsInReverse = "sort_songs_in_reverse"
}

object SettingsDefaults {
    val language = English
    val font = SupportedFonts.ProductSans
    const val fontScale = 1.25f
    val themeMode = ThemeMode.SYSTEM
    const val useMaterialYou = true
    const val primaryColorName = "Blue"
    val homeTabs = setOf(
        HomeTab.ForYou,
        HomeTab.Songs,
        HomeTab.Albums,
        HomeTab.Artists,
        HomeTab.Playlists,
    )
    val forYouContents = setOf(
        ForYou.Albums,
        ForYou.Artists
    )
    val homePageBottomBarLabelVisibility = HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE
    const val fadePlayback = false
    const val fadePlaybackDuration = 1f
    const val requireAudioFocus = true
    const val ignoreAudioFocusLoss = false
    const val playOnHeadphonesConnect = false
    const val pauseOnHeadphonesDisconnect = true
    const val fastForwardDuration = 30
    const val fastRewindDuration = 15
    const val miniPlayerShowTrackControls = true
    const val miniPlayerShowSeekControls = false
    const val miniPlayerTextMarquee = true
    val nowPlayingControlsLayout = NowPlayingControlsLayout.Default
    val nowPlayingLyricsLayout = NowPlayingLyricsLayout.ReplaceArtwork
    const val showNowPlayingAudioInformation = true
    const val showNowPlayingSeekControls = false

    val sortSongsBy = SortSongsBy.TITLE
    const val sortSongsInReverse = false
}

private inline fun <reified T : Enum<T>> parseEnumValue( value: String ): T? =
    T::class.java.enumConstants?.find { it.name == value }

private inline fun <reified T : Enum<T>> SharedPreferences.Editor.putEnum(
    key: String,
    value: T?
) = putString( key, value?.name )

private inline fun <reified T : Enum<T>> SharedPreferences.getEnum(
    key: String,
    defaultValue: T?
): T? {
    var result = defaultValue
    getString( key, null )?.let { value -> result = parseEnumValue<T>( value ) }
    return result
}
