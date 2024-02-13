package com.odesa.musically.services.media.library

import com.odesa.musically.fakes.FakeMusicSource
import com.odesa.musically.fakes.songsIn1
import com.odesa.musically.fakes.songsIn21
import com.odesa.musically.fakes.songsInBackInBlack
import com.odesa.musically.fakes.songsInBad
import com.odesa.musically.fakes.songsInBatOutOfHell
import com.odesa.musically.fakes.songsInComeOnOver
import com.odesa.musically.fakes.songsInDarkSideOfTheMoon
import com.odesa.musically.fakes.songsInDirtyDancing
import com.odesa.musically.fakes.songsInFallingIntoYou
import com.odesa.musically.fakes.songsInHotelCalifornia
import com.odesa.musically.fakes.songsInJaggedLittlePill
import com.odesa.musically.fakes.songsInLedZeppelinIv
import com.odesa.musically.fakes.songsInRumours
import com.odesa.musically.fakes.songsInSaturdayNightFever
import com.odesa.musically.fakes.songsInTheBodyguard
import com.odesa.musically.fakes.songsInTheGreatestHits
import com.odesa.musically.fakes.songsInThriller
import com.odesa.musically.fakes.tracksMetadataList
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class BrowseTreeTest {

    private lateinit var musicSource: FakeMusicSource
    private lateinit var browseTree: BrowseTree

    @Before
    fun setup() {
        musicSource = FakeMusicSource()
        browseTree = BrowseTree( musicSource )
    }

    @Test
    fun testAlbumsAreRetrievedCorrectly() {
        val albums = browseTree[ MUSICALLY_ALBUMS_ROOT ]
        assertEquals( 17, albums.size )
    }

    @Test
    fun testSongsInGivenAlbumAreRetrievedCorrectly() {
        val idForThriller = 1L
        val idForBackInBlack = 2L
        val idForTheBodyguard = 3L
        val idForTheDarkSizeOfTheMoon = 4L
        val idForTheGreatestHits = 5L
        val idForBatOutOfHell = 6L
        val idForHotelCalifornia = 7L
        val idForComeOnOver = 8L
        val idForRumours = 9L
        val idForSaturdayNightFever = 10L
        val idForLedZeppelinIv = 11L
        val idForBad = 12L
        val idForJaggedLittlePill = 13L
        val idForDirtyDancing = 14L
        val idForFallingIntoYou = 15L
        val idFor21 = 16L
        val idFor1 = 17L
        assertEquals( songsInThriller.size, browseTree[ idForThriller.toString() ].size )
        assertEquals( songsInBackInBlack.size, browseTree[ idForBackInBlack.toString() ].size )
        assertEquals( songsInTheBodyguard.size, browseTree[ idForTheBodyguard.toString() ].size )
        assertEquals( songsInDarkSideOfTheMoon.size, browseTree[ idForTheDarkSizeOfTheMoon.toString() ].size )
        assertEquals( songsInTheGreatestHits.size, browseTree[ idForTheGreatestHits.toString() ].size )
        assertEquals( songsInBatOutOfHell.size, browseTree[ idForBatOutOfHell.toString() ].size )
        assertEquals( songsInHotelCalifornia.size, browseTree[ idForHotelCalifornia.toString() ].size )
        assertEquals( songsInComeOnOver.size, browseTree[ idForComeOnOver.toString() ].size )
        assertEquals( songsInRumours.size, browseTree[ idForRumours.toString() ].size )
        assertEquals( songsInSaturdayNightFever.size, browseTree[ idForSaturdayNightFever.toString() ].size )
        assertEquals( songsInLedZeppelinIv.size, browseTree[ idForLedZeppelinIv.toString() ].size )
        assertEquals( songsInBad.size, browseTree[ idForBad.toString() ].size )
        assertEquals( songsInJaggedLittlePill.size, browseTree[ idForJaggedLittlePill.toString() ].size )
        assertEquals( songsInDirtyDancing.size, browseTree[ idForDirtyDancing.toString() ].size )
        assertEquals( songsInFallingIntoYou.size, browseTree[ idForFallingIntoYou.toString() ].size )
        assertEquals( songsIn21.size, browseTree[ idFor21.toString() ].size )
        assertEquals( songsIn1.size, browseTree[ idFor1.toString() ].size )
    }

    @Test
    fun testTracksAreCorrectlyRetrieved() {
        assertEquals( tracksMetadataList.size, browseTree[ MUSICALLY_TRACKS_ROOT ].size )
    }

}