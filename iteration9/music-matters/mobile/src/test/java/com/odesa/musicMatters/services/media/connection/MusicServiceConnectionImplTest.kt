package com.odesa.musicMatters.services.media.connection

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.odesa.musicMatters.core.common.connection.CURRENTLY_PLAYING_MEDIA_ITEM_INDEX_UNDEFINED
import com.odesa.musicMatters.core.common.connection.Connectable
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.common.connection.MusicServiceConnectionImpl
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.allowedSpeedPitchValues
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.datatesting.albums.testAlbumMediaItems
import com.odesa.musicMatters.core.datatesting.artists.testArtistMediaItems
import com.odesa.musicMatters.core.datatesting.connection.FakeConnectable
import com.odesa.musicMatters.core.datatesting.genres.testGenreMediaItems
import com.odesa.musicMatters.core.datatesting.playlist.FakePlaylistRepository
import com.odesa.musicMatters.core.datatesting.repository.FakeSettingsRepository
import com.odesa.musicMatters.core.datatesting.songs.testSongMediaItems
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class MusicServiceConnectionImplTest {

    private val playbackPitch = 0.5f
    private val playbackSpeed = 0.5f
    private val currentRepeatMode = Player.REPEAT_MODE_OFF
    private lateinit var connectable: Connectable
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var musicServiceConnection: MusicServiceConnection


    @OptIn( ExperimentalCoroutinesApi::class )
    @Before
    fun setup() {
        connectable = FakeConnectable()
        playlistRepository = FakePlaylistRepository()
        settingsRepository = FakeSettingsRepository()
        musicServiceConnection = MusicServiceConnectionImpl(
            connectable,
            playlistRepository = playlistRepository,
            settingsRepository = settingsRepository,
            dispatcher = UnconfinedTestDispatcher(),
            initialPlaybackParameters = PlaybackParameters( playbackSpeed, playbackPitch ),
            initialRepeatMode = currentRepeatMode
        )
    }

    @Test
    fun testCachesAreCorrectlyUpdated() {
        assertFalse( musicServiceConnection.isInitializing.value )
        assertEquals( testSongMediaItems.size, musicServiceConnection.cachedSongs.value.size )
        assertEquals( testGenreMediaItems.size, musicServiceConnection.cachedGenres.value.size )
        assertEquals( 3, musicServiceConnection.cachedGenres.value.find { it.name == "Pop" }!!.numberOfTracks )
        assertEquals( testSongMediaItems.size, musicServiceConnection.cachedRecentlyAddedSongs.value.size )
        assertEquals( testArtistMediaItems.size, musicServiceConnection.cachedArtists.value.size )
        assertEquals( testArtistMediaItems.size, musicServiceConnection.cachedSuggestedArtists.value.size )
        assertEquals( testAlbumMediaItems.size, musicServiceConnection.cachedAlbums.value.size )
        assertEquals( testAlbumMediaItems.size, musicServiceConnection.cachedSuggestedAlbums.value.size )
    }

    @Test
    fun testAddToQueue() {
        testSongMediaItems.forEach {
            musicServiceConnection.addToQueue( it )
        }
        musicServiceConnection.addToQueue( testSongMediaItems.first() ) // NO DUPLICATES!
        assertEquals(
            testSongMediaItems.size,
            musicServiceConnection.mediaItemsInQueue.value.size
        )
        assertEquals(
            testSongMediaItems.size,
            connectable.player!!.mediaItemCount
        )
        assertEquals(
            testSongMediaItems.first().mediaId,
            connectable.player!!.currentMediaItem!!.mediaId
        )
        assertEquals(
            testSongMediaItems.first().mediaId,
            musicServiceConnection.nowPlayingMediaItem.value.mediaId
        )
        assertEquals(
            0,
            musicServiceConnection.currentlyPlayingMediaItemIndex.value
        )
        assertTrue( musicServiceConnection.playbackState.value.isPlaying )
        assertEquals(
            0,
            connectable.player!!.currentMediaItemIndex
        )
        assertEquals(
            testSongMediaItems.size,
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.size
        )
    }

    @Test
    fun testPlayNext() {
        testSongMediaItems.forEach {
            musicServiceConnection.playNext( it )
        }
        assertEquals(
            testSongMediaItems.size,
            musicServiceConnection.mediaItemsInQueue.value.size
        )
        assertEquals(
            testSongMediaItems.size,
            connectable.player!!.mediaItemCount
        )
        assertTrue( musicServiceConnection.playbackState.value.isPlaying )
        assertEquals(
            testSongMediaItems.first().mediaId,
            musicServiceConnection.mediaItemsInQueue.value.first().mediaId
        )
        musicServiceConnection.playNext(
            testSongMediaItems.last()
        )
        assertEquals(
            testSongMediaItems.size,
            musicServiceConnection.mediaItemsInQueue.value.size
        )
        assertEquals(
            testSongMediaItems.last().mediaId,
            musicServiceConnection.mediaItemsInQueue.value[1].mediaId
        )
        assertEquals(
            testSongMediaItems.last().mediaId,
            connectable.player!!.getMediaItemAt( 1 ).mediaId
        )
        assertEquals(
            testSongMediaItems.size,
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.size
        )
    }

    @Test
    fun testShuffleAndPlay() {
        musicServiceConnection.shuffleAndPlay( testSongMediaItems )
        assertTrue( musicServiceConnection.playbackState.value.isPlaying )
        assertEquals(
            testSongMediaItems.size,
            musicServiceConnection.mediaItemsInQueue.value.size
        )
        assertEquals(
            testSongMediaItems.size,
            connectable.player!!.mediaItemCount
        )
        assertNotNull(
            musicServiceConnection.mediaItemsInQueue.value.find {
                it.mediaId == musicServiceConnection.nowPlayingMediaItem.value.mediaId
            }
        )
        assertEquals(
            testSongMediaItems.size,
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.size
        )
    }

    @Test
    fun testSeekToNext() {
        testSongMediaItems.forEach {
            musicServiceConnection.addToQueue( it )
        }
        assertEquals( 0, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 0, connectable.player!!.currentMediaItemIndex )
        musicServiceConnection.playNextSong()
        assertEquals( 1, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 1, connectable.player!!.currentMediaItemIndex )
        musicServiceConnection.playNextSong()
        assertEquals( 2, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 2, connectable.player!!.currentMediaItemIndex )
    }

    @Test
    fun testShuffleSongsInQueue() {
        testSongMediaItems.forEach {
            musicServiceConnection.addToQueue( it )
        }
        connectable.player!!.seekToNext()  // testMediaItems[1] is currently playing
        musicServiceConnection.shuffleSongsInQueue()
        assertEquals( 0, connectable.player!!.currentMediaItemIndex )
        assertEquals( 0, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals(
            testSongMediaItems[1].mediaId,
            musicServiceConnection.nowPlayingMediaItem.value.mediaId
        )
        assertEquals(
            testSongMediaItems[1].mediaId,
            connectable.player!!.currentMediaItem!!.mediaId
        )
        assertEquals(
            testSongMediaItems.size,
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.size
        )
    }

    @Test
    fun testMoveMediaItem() {
        testSongMediaItems.forEach {
            musicServiceConnection.addToQueue( it )
        }
        musicServiceConnection.moveMediaItem( 0, 2 )
        assertEquals(
            testSongMediaItems.first().mediaId,
            musicServiceConnection.mediaItemsInQueue.value[2].mediaId
        )
        assertEquals( 2, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 2, connectable.player!!.currentMediaItemIndex )
        musicServiceConnection.moveMediaItem( 2, 0 )
        assertEquals( 0, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 0, connectable.player!!.currentMediaItemIndex )
        assertEquals(
            testSongMediaItems.size,
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.size
        )
    }

    @Test
    fun testClearQueue() {
        testSongMediaItems.forEach {
            musicServiceConnection.addToQueue( it )
        }
        musicServiceConnection.clearQueue()
        assertEquals( 0, musicServiceConnection.mediaItemsInQueue.value.size )
        assertEquals( 0, connectable.player!!.mediaItemCount )
        assertEquals( MediaItem.EMPTY, musicServiceConnection.nowPlayingMediaItem.value )
        assertEquals(
            CURRENTLY_PLAYING_MEDIA_ITEM_INDEX_UNDEFINED,
            musicServiceConnection.currentlyPlayingMediaItemIndex.value
        )
        assertTrue(
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.isEmpty()
        )
    }

    @Test
    fun testPlaybackParametersAreSetCorrectly() {
        assertEquals( playbackSpeed, connectable.player!!.playbackParameters.speed )
        assertEquals( playbackPitch, connectable.player!!.playbackParameters.pitch )
    }

    @Test
    fun testSetPlaybackSpeed() {
        allowedSpeedPitchValues.forEach {
            musicServiceConnection.setPlaybackSpeed( it )
            assertEquals( it, connectable.player!!.playbackParameters.speed )
        }
    }

    @Test
    fun testSetPlaybackPitch() {
        allowedSpeedPitchValues.forEach {
            musicServiceConnection.setPlaybackPitch( it )
            assertEquals( it, connectable.player!!.playbackParameters.pitch )
        }
    }

    @Test
    fun testSetRepeatMode() {
        assertEquals( Player.REPEAT_MODE_OFF, connectable.player!!.repeatMode )
        listOf( Player.REPEAT_MODE_OFF, Player.REPEAT_MODE_ONE, Player.REPEAT_MODE_ALL ).forEach {
            musicServiceConnection.setRepeatMode( it )
            assertEquals( it, connectable.player!!.repeatMode )
        }
    }

    @Test
    fun testPlayPause() {
        testSongMediaItems.forEach {
            musicServiceConnection.addToQueue( it )
        }
        musicServiceConnection.playPause()
        assertFalse( musicServiceConnection.playbackState.value.isPlaying )
        assertFalse( connectable.player!!.isPlaying )
        musicServiceConnection.playPause()
        assertTrue( musicServiceConnection.playbackState.value.isPlaying )
        assertTrue( connectable.player!!.isPlaying )
    }

    @Test
    fun testPlayPreviousSong() {
        testSongMediaItems.forEach {
            musicServiceConnection.addToQueue( it )
        }
        assertEquals( 0, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 0, connectable.player!!.currentMediaItemIndex )

        musicServiceConnection.playPreviousSong()
        assertEquals( 0, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 0, connectable.player!!.currentMediaItemIndex )

        musicServiceConnection.playNextSong()
        musicServiceConnection.playNextSong()
        assertEquals( 2, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 2, connectable.player!!.currentMediaItemIndex )

        musicServiceConnection.playPreviousSong()
        assertEquals( 1, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 1, connectable.player!!.currentMediaItemIndex )

        musicServiceConnection.playPreviousSong()
        assertEquals( 0, musicServiceConnection.currentlyPlayingMediaItemIndex.value )
        assertEquals( 0, connectable.player!!.currentMediaItemIndex )
        connectable.player
    }

    @Test
    fun testMediaItemsInQueueAreSavedCorrectly() {
        assertTrue( playlistRepository.currentPlayingQueuePlaylist.value.songIds.isEmpty() )
        musicServiceConnection.playMediaItem(
            mediaItem = testSongMediaItems.first(),
            mediaItems = testSongMediaItems,
            shuffle = false
        )
        assertEquals(
            testSongMediaItems.size,
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.size
        )
    }

}