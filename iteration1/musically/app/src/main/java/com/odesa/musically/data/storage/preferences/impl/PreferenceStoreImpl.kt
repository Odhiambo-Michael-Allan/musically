package com.odesa.musically.data.storage.preferences.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.odesa.musically.data.storage.preferences.ForYou
import com.odesa.musically.data.storage.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.storage.preferences.HomeTab
import com.odesa.musically.data.storage.preferences.NowPlayingControlsLayout
import com.odesa.musically.data.storage.preferences.NowPlayingLyricsLayout
import com.odesa.musically.data.storage.preferences.PreferenceStore
import com.odesa.musically.data.storage.preferences.SortSongsBy
import com.odesa.musically.services.i18n.English
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferenceStoreImpl( private val context: Context ) : PreferenceStore {

    override suspend fun setLanguage( localeCode: String ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putString(SettingsKeys.language, localeCode )
            }
        }
    }

    override fun getLanguage(): String {
        val language = getSharedPreferences().getString(
            SettingsKeys.language, null
        )
        return language ?: SettingsDefaults.language.locale
    }

    override suspend fun setFontName( fontName: String ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putString(SettingsKeys.fontName, fontName )
            }
        }
    }

    override fun getFontName() = getSharedPreferences().getString(
        SettingsKeys.fontName, SettingsDefaults.font.name
    ) ?: SettingsDefaults.font.name

    override suspend fun setFontScale( fontScale: Float ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putFloat(SettingsKeys.fontScale, fontScale )
            }
        }
    }

    override fun getFontScale() = getSharedPreferences().getFloat(
        SettingsKeys.fontScale, SettingsDefaults.fontScale
    )

    override suspend fun setThemeMode( themeMode: ThemeMode ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putString(SettingsKeys.themeMode, themeMode.name )
            }
        }
    }

    override fun getThemeMode() = getSharedPreferences().getString(SettingsKeys.themeMode, null )
        ?.let { ThemeMode.valueOf( it ) }
        ?: SettingsDefaults.themeMode

    override fun getUseMaterialYou() = getSharedPreferences().getBoolean(
        SettingsKeys.useMaterialYou, SettingsDefaults.useMaterialYou
    )

    override suspend fun setUseMaterialYou( useMaterialYou: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.useMaterialYou, useMaterialYou )
            }
        }
    }

    override suspend fun setPrimaryColorName( primaryColorName: String ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putString(SettingsKeys.primaryColorName, primaryColorName )
            }
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

    override suspend fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putString(SettingsKeys.homeTabs, homeTabs.joinToString( "," ) { it.name } )
            }
        }
    }

    override fun getForYouContents() = getSharedPreferences()
        .getString(SettingsKeys.forYouContents, null )
        ?.split( "," )
        ?.mapNotNull { parseEnumValue<ForYou>( it ) }
        ?.toSet()
        ?: SettingsDefaults.forYouContents

    override suspend fun setForYouContents(forYouContents: Set<ForYou>) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putString(
                    SettingsKeys.forYouContents,
                    forYouContents.joinToString( "," ) { it.name } )
            }
        }
    }

    override fun getHomePageBottomBarLabelVisibility() = getSharedPreferences()
        .getEnum(SettingsKeys.homePageBottomBarLabelVisibility, null )
        ?: SettingsDefaults.homePageBottomBarLabelVisibility

    override suspend fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putEnum(SettingsKeys.homePageBottomBarLabelVisibility, value )
            }
        }
    }

    override fun getFadePlayback() = getSharedPreferences().getBoolean(
        SettingsKeys.fadePlayback,
        SettingsDefaults.fadePlayback
    )

    override suspend fun setFadePlayback( fadePlayback: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.fadePlayback, fadePlayback )
            }
        }
    }

    override fun getFadePlaybackDuration() = getSharedPreferences().getFloat(
        SettingsKeys.fadePlaybackDuration,
        SettingsDefaults.fadePlaybackDuration
    )

    override suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putFloat(SettingsKeys.fadePlaybackDuration, fadePlaybackDuration )
            }
        }
    }

    override fun getRequireAudioFocus() = getSharedPreferences()
        .getBoolean( SettingsKeys.requireAudioFocus, SettingsDefaults.requireAudioFocus )

    override suspend fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.requireAudioFocus, requireAudioFocus )
            }
        }
    }

    override fun getIgnoreAudioFocusLoss() = getSharedPreferences()
        .getBoolean(SettingsKeys.ignoreAudioFocusLoss, SettingsDefaults.ignoreAudioFocusLoss)

    override suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.ignoreAudioFocusLoss, ignoreAudioFocusLoss )
            }
        }
    }

    override fun getPlayOnHeadphonesConnect() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.playOnHeadphonesConnect,
            SettingsDefaults.playOnHeadphonesConnect
        )

    override suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.playOnHeadphonesConnect, playOnHeadphonesConnect )
            }
        }
    }

    override fun getPauseOnHeadphonesDisconnect() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.pauseOnHeadphonesDisconnect,
            SettingsDefaults.pauseOnHeadphonesDisconnect
        )

    override suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(
                    SettingsKeys.pauseOnHeadphonesDisconnect,
                    pauseOnHeadphonesDisconnect )
            }
        }
    }

    override fun getFastRewindDuration() = getSharedPreferences().getInt(
        SettingsKeys.fastRewindDuration, SettingsDefaults.fastRewindDuration
    )

    override suspend fun setFastRewindDuration( fastRewindDuration: Int ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putInt(SettingsKeys.fastRewindDuration, fastRewindDuration )
            }
        }
    }

    override fun getFastForwardDuration() = getSharedPreferences().getInt(
        SettingsKeys.fastForwardDuration, SettingsDefaults.fastForwardDuration
    )

    override suspend fun setFastForwardDuration( fastForwardDuration: Int ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putInt(SettingsKeys.fastForwardDuration, fastForwardDuration )
            }
        }
    }

    override fun getMiniPlayerShowTrackControls() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.miniPlayerShowTrackControls,
            SettingsDefaults.miniPlayerShowTrackControls
        )

    override suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(
                    SettingsKeys.miniPlayerShowTrackControls,
                    showTrackControls )
            }
        }
    }

    override fun getMiniPlayerShowSeekControls() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.miniPlayerShowSeekControls,
            SettingsDefaults.miniPlayerShowSeekControls
        )

    override suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(
                    SettingsKeys.miniPlayerShowSeekControls,
                    showSeekControls )
            }
        }
    }

    override fun getMiniPlayerTextMarquee() = getSharedPreferences()
        .getBoolean(
            SettingsKeys.miniPlayerTextMarquee,
            SettingsDefaults.miniPlayerTextMarquee
        )

    override suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.miniPlayerTextMarquee, textMarquee )
            }
        }
    }

    override fun getNowPlayingControlsLayout() = getSharedPreferences().getEnum(
        SettingsKeys.nowPlayingControlsLayout, null
    ) ?: SettingsDefaults.nowPlayingControlsLayout


    override suspend fun setNowPlayingControlsLayout( nowPlayingControlsLayout: NowPlayingControlsLayout) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putEnum(SettingsKeys.nowPlayingControlsLayout, nowPlayingControlsLayout )
            }
        }
    }

    override fun getNowPlayingLyricsLayout() = getSharedPreferences().getEnum(
        SettingsKeys.nowPlayingLyricsLayout, null
    ) ?: SettingsDefaults.nowPlayingLyricsLayout

    override suspend fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putEnum(SettingsKeys.nowPlayingLyricsLayout, nowPlayingLyricsLayout )
            }
        }
    }

    override fun getShowNowPlayingAudioInformation() = getSharedPreferences().getBoolean(
        SettingsKeys.showNowPlayingAudioInformation, SettingsDefaults.showNowPlayingAudioInformation
    )

    override suspend fun setShowNowPlayingAudioInformation( showNowPlayingAudioInformation: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(
                    SettingsKeys.showNowPlayingAudioInformation,
                    showNowPlayingAudioInformation
                )
            }
        }
    }

    override fun getShowNowPlayingSeekControls() = getSharedPreferences().getBoolean(
        SettingsKeys.showNowPlayingSeekControls, SettingsDefaults.showNowPlayingSeekControls
    )

    override suspend fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.showNowPlayingSeekControls, showNowPlayingSeekControls )
            }
        }
    }

    override suspend fun setSortSongsBy(sortSongsBy: SortSongsBy) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putEnum(SettingsKeys.sortSongsBy, sortSongsBy )
            }
        }
    }

    override fun getSortSongsBy() = getSharedPreferences()
        .getEnum(SettingsKeys.sortSongsBy, null ) ?: SettingsDefaults.sortSongsBy

    override suspend fun setSortSongsInReverse(sortSongsInReverse: Boolean ) {
        withContext( Dispatchers.IO ) {
            getSharedPreferences().edit {
                putBoolean(SettingsKeys.sortSongsInReverse, sortSongsInReverse )
            }
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
    const val primaryColorName = "Green"
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
