package com.odesa.musically.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.appearance.scalingPresets
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel( private val settingsRepository: SettingsRepository ) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsScreenUiState(
            language = settingsRepository.language.value,
            font = settingsRepository.font.value,
            fontScale = settingsRepository.fontScale.value,
            themeMode = settingsRepository.themeMode.value,
            useMaterialYou = settingsRepository.useMaterialYou.value,
            primaryColorName = settingsRepository.primaryColorName.value,
            homeTabs = settingsRepository.homeTabs.value,
            forYouContent = settingsRepository.forYouContent.value,
            homePageBottomBarLabelVisibility = settingsRepository.homePageBottomBarLabelVisibility.value,
            fadePlayback = settingsRepository.fadePlayback.value,
            fadePlaybackDuration = settingsRepository.fadePlaybackDuration.value,
            requireAudioFocus = settingsRepository.requireAudioFocus.value,
            ignoreAudioFocusLoss = settingsRepository.ignoreAudioFocusLoss.value,
            playOnHeadphonesConnect = settingsRepository.playOnHeadphonesConnect.value,
            pauseOnHeadphonesDisconnect = settingsRepository.pauseOnHeadphonesDisconnect.value,
            fastRewindDuration = settingsRepository.fastRewindDuration.value,
            fastForwardDuration = settingsRepository.fastForwardDuration.value,
            miniPlayerShowTrackControls = settingsRepository.miniPlayerShowTrackControls.value,
            miniPlayerShowSeekControls = settingsRepository.miniPlayerShowSeekControls.value,
            miniPlayerTextMarquee = settingsRepository.miniPlayerTextMarquee.value,
        )
    )
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch { observeLanguageSetting() }
        viewModelScope.launch { observeFontSetting() }
        viewModelScope.launch { observeFontScaleSetting() }
        viewModelScope.launch { observeThemeModeSetting() }
        viewModelScope.launch { observeUseMaterialYouSetting() }
        viewModelScope.launch { observePrimaryColorNameSetting() }
        viewModelScope.launch { observeHomeTabsSetting() }
        viewModelScope.launch { observeForYouContentSetting() }
        viewModelScope.launch { observeHomePageBottomBarLabelVisibilitySetting() }
        viewModelScope.launch { observeFadePlaybackSetting() }
        viewModelScope.launch { observeFadePlaybackDurationSetting() }
        viewModelScope.launch { observeRequireAudioFocusSetting() }
        viewModelScope.launch { observeIgnoreAudioFocusLossSetting() }
        viewModelScope.launch { observePlayOnHeadphonesConnectSetting() }
        viewModelScope.launch { observePauseOnHeadphonesDisconnectSetting() }
        viewModelScope.launch { observeFastRewindDurationSetting() }
        viewModelScope.launch { observeFastForwardDurationSetting() }
        viewModelScope.launch { observeMiniPlayerShowTrackControlsSetting() }
        viewModelScope.launch { observeMiniPlayerShowSeekControlsSetting() }
        viewModelScope.launch { observeMiniPlayerTextMarqueeSetting() }
    }

    private suspend fun observeLanguageSetting() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

    private suspend fun observeFontSetting() {
        settingsRepository.font.collect {
            _uiState.value = _uiState.value.copy(
                font = it
            )
        }
    }

    private suspend fun observeFontScaleSetting() {
        settingsRepository.fontScale.collect {
            _uiState.value = _uiState.value.copy(
                fontScale = it
            )
        }
    }

    private suspend fun observeThemeModeSetting() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    private suspend fun observeUseMaterialYouSetting() {
        settingsRepository.useMaterialYou.collect {
            _uiState.value = _uiState.value.copy(
                useMaterialYou = it
            )
        }
    }

    private suspend fun observePrimaryColorNameSetting() {
        settingsRepository.primaryColorName.collect {
            _uiState.value = _uiState.value.copy(
                primaryColorName = it
            )
        }
    }

    private suspend fun observeHomeTabsSetting() {
        settingsRepository.homeTabs.collect {
            _uiState.value = _uiState.value.copy(
                homeTabs = it
            )
        }
    }

    private suspend fun observeForYouContentSetting() {
        settingsRepository.forYouContent.collect {
            _uiState.value = _uiState.value.copy(
                forYouContent = it
            )
        }
    }

    private suspend fun observeHomePageBottomBarLabelVisibilitySetting() {
        settingsRepository.homePageBottomBarLabelVisibility.collect {
            _uiState.value = _uiState.value.copy(
                homePageBottomBarLabelVisibility = it
            )
        }
    }

    private suspend fun observeFadePlaybackSetting() {
        settingsRepository.fadePlayback.collect {
            _uiState.value = _uiState.value.copy(
                fadePlayback = it
            )
        }
    }

    private suspend fun observeFadePlaybackDurationSetting() {
        settingsRepository.fadePlaybackDuration.collect {
            _uiState.value = _uiState.value.copy(
                fadePlaybackDuration = it
            )
        }
    }

    private suspend fun observeRequireAudioFocusSetting() {
        settingsRepository.requireAudioFocus.collect {
            _uiState.value = _uiState.value.copy(
                requireAudioFocus = it
            )
        }
    }

    private suspend fun observeIgnoreAudioFocusLossSetting() {
        settingsRepository.ignoreAudioFocusLoss.collect {
            _uiState.value = _uiState.value.copy(
                ignoreAudioFocusLoss = it
            )
        }
    }

    private suspend fun observePlayOnHeadphonesConnectSetting() {
        settingsRepository.playOnHeadphonesConnect.collect {
            _uiState.value = _uiState.value.copy(
                playOnHeadphonesConnect = it
            )
        }
    }

    private suspend fun observePauseOnHeadphonesDisconnectSetting() {
        settingsRepository.pauseOnHeadphonesDisconnect.collect {
            _uiState.value = _uiState.value.copy(
                pauseOnHeadphonesDisconnect = it
            )
        }
    }

    private suspend fun observeFastRewindDurationSetting() {
        settingsRepository.fastRewindDuration.collect {
            _uiState.value = _uiState.value.copy(
                fastRewindDuration = it
            )
        }
    }

    private suspend fun observeFastForwardDurationSetting() {
        settingsRepository.fastForwardDuration.collect {
            _uiState.value = _uiState.value.copy(
                fastForwardDuration = it
            )
        }
    }

    private suspend fun observeMiniPlayerShowTrackControlsSetting() {
        settingsRepository.miniPlayerShowTrackControls.collect {
            _uiState.value = _uiState.value.copy(
                miniPlayerShowTrackControls = it
            )
        }
    }

    private suspend fun observeMiniPlayerShowSeekControlsSetting() {
        settingsRepository.miniPlayerShowSeekControls.collect {
            _uiState.value = _uiState.value.copy(
                miniPlayerShowSeekControls = it
            )
        }
    }

    private suspend fun observeMiniPlayerTextMarqueeSetting() {
        settingsRepository.miniPlayerTextMarquee.collect {
            _uiState.value = _uiState.value.copy(
                miniPlayerTextMarquee = it
            )
        }
    }

    fun setLanguage( localeCode: String ) {
        settingsRepository.setLanguage( localeCode )
    }

    fun setFont( fontName: String ) {
        settingsRepository.setFont( fontName )
    }

    fun setFontScale( fontScale: String ) {
        val float = fontScale.toFloatOrNull()
        float?.let {
            if ( scalingPresets.contains( it ) ) settingsRepository.setFontScale( it )
        }
    }

    fun setThemeMode( themeMode: ThemeMode ) {
        settingsRepository.setThemeMode( themeMode )
    }

    fun setUseMaterialYou( useMaterialYou: Boolean ) {
        settingsRepository.setUseMaterialYou( useMaterialYou )
    }

    fun setPrimaryColorName( primaryColorName: String ) {
        settingsRepository.setPrimaryColorName( primaryColorName )
    }

    fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        settingsRepository.setHomeTabs( homeTabs )
    }

    fun setForYouContent( forYouContent: Set<ForYou> ) {
        settingsRepository.setForYouContents( forYouContent )
    }

    fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility ) {
        settingsRepository.setHomePageBottomBarLabelVisibility( value )
    }

    fun setFadePlayback( fadePlayback: Boolean ) {
        settingsRepository.setFadePlayback( fadePlayback )
    }

    fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        settingsRepository.setFadePlaybackDuration( fadePlaybackDuration )
    }

    fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        settingsRepository.setRequireAudioFocus( requireAudioFocus )
    }

    fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        settingsRepository.setIgnoreAudioFocusLoss( ignoreAudioFocusLoss )
    }

    fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        settingsRepository.setPlayOnHeadphonesConnect( playOnHeadphonesConnect )
    }

    fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        settingsRepository.setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect )
    }

    fun setFastRewindDuration( fastRewindDuration: Int ) {
        settingsRepository.setFastRewindDuration( fastRewindDuration )
    }

    fun setFastForwardDuration( fastForwardDuration: Int ) {
        settingsRepository.setFastForwardDuration( fastForwardDuration )
    }

    fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        settingsRepository.setMiniPlayerShowTrackControls( showTrackControls )
    }

    fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        settingsRepository.setMiniPlayerShowSeekControls( showSeekControls )
    }

    fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        settingsRepository.setMiniPlayerTextMarquee( textMarquee )
    }

}

@Suppress( "UNCHECKED_CAST" )
class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SettingsViewModel( settingsRepository ) as T )
}

data class SettingsScreenUiState(
    val language: Language,
    val font: MusicallyFont,
    val fontScale: Float,
    val themeMode: ThemeMode,
    val useMaterialYou: Boolean,
    val primaryColorName: String,
    val homeTabs: Set<HomeTab>,
    val forYouContent: Set<ForYou>,
    val homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility,
    val fadePlayback: Boolean,
    val fadePlaybackDuration: Float,
    val requireAudioFocus: Boolean,
    val ignoreAudioFocusLoss: Boolean,
    val playOnHeadphonesConnect: Boolean,
    val pauseOnHeadphonesDisconnect: Boolean,
    val fastRewindDuration: Int,
    val fastForwardDuration: Int,
    val miniPlayerShowTrackControls: Boolean,
    val miniPlayerShowSeekControls: Boolean,
    val miniPlayerTextMarquee: Boolean,
)