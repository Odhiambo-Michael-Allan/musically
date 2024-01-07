package com.odesa.musically.services.i18n

import com.odesa.musically.data.AppContainer

class Translator( private val appContainer: AppContainer ) {

    suspend fun onChange( fn: ( Translation ) -> Unit ) {
        appContainer.settingRepository.language.collect {
            fn( getCurrentTranslation() )
        }
    }

    fun getCurrentTranslation() = appContainer.settingRepository.language.value
        ?.let { parse( it ) }
        ?: getDefaultTranslation()

    private fun getDefaultTranslation(): Translation = EnglishTranslation()

    private fun parse( localeCode: String ) = EnglishTranslation()
}