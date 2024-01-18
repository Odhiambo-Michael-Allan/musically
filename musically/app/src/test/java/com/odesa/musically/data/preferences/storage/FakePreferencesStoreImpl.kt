package com.odesa.musically.data.preferences.storage

import com.odesa.musically.ui.theme.SupportedFonts

class FakePreferencesStoreImpl : PreferenceStore {

    private var language: String? = null
    private var fontName: String? = null

    override fun setLanguage( localeCode: String ) {
        language = localeCode
    }

    override fun getLanguage(): String {
        return language ?: "en"
    }

    override fun setFontName( fontName: String ) {
        this.fontName = fontName
    }

    override fun getFontName() = this.fontName ?: SupportedFonts.ProductSans.fontName
}