package com.odesa.musically.fakes

import androidx.media3.common.MediaItem
import com.odesa.musically.services.media.library.AbstractMusicSource
import com.odesa.musically.services.media.library.STATE_INITIALIZED

class FakeMusicSource : AbstractMusicSource() {

    private val tracks = emptyList<MediaItem>()

    override suspend fun load() = Unit

    override fun iterator(): Iterator<MediaItem> = tracks.iterator()

    fun prepare() {
        state = STATE_INITIALIZED
    }

}

var currentAlbumId = 0L
var currentTrackId = 0L
var currentAlbumArtistId = 0L
var currentGenreId = 0L

val albumTitles = listOf(
    "Thriller",
    "Back in Black",
    "The Bodyguard",
    "The Dark Size of the Moon",
    "The Greatest Hits",
    "Bat Out of Hell",
    "Hotel California",
    "Come On Over",
    "Rumours",
    "Saturday Night Fever",
    "Led Zeppelin IV",
    "Bad",
    "Jagged Little Pill",
    "Dirty Dancing",
    "Falling into You",
    "21",
    "1",
)

val correspondingAlbumArtists = listOf(
    "Michael Jackson",
    "AC/DC",
    "Whitney Houston",
    "Pink Floyd",
    "Eagles",
    "Meat Loaf",
    "Eagles",
    "Shania Twain",
    "Fleetwood Mac",
    "Bee Gees",
    "Led Zeppelin",
    "Michael Jackson",
    "Alanis Morissette",
    "Unknown",
    "Celine Dion",
    "Adele",
    "The Beatles",
)

val correspondingAlbumGenre = listOf(
    "Pop",
    "Hard rock",
    "R&B",
    "Progressive rock",
    "Country rock",
    "Hard rock",
    "Soft rock",
    "Country",
    "Disco",
    "Hard rock",
    "Pop",
    "Alternative rock",
    "Pop",
    "Pop",
    "Pop",
    "R&B",
    "Rock",
)

val songsInThriller = listOf(
    "Wanna Be Startin' Somethin'",
    "Baby Be Mine",
    "The Girl Is Mine",
    "Thriller"
)

val songsInBackInBlack = listOf(
    "Hells Bells",
    "Shoot to Thrill",
    "What Do You Do for Money Honey",
    "Givin the Dog a Bone",
    "Let Me Put My Love into You"
)

val songsInTheBodyguard = listOf(
    "I Will Always Love You",
    "I Have Nothing",
    "I'm Every Woman",
    "Run to You",
    "Queen of the Night",
    "Jesus Loves Me",
    "Even If My Heart Would Break",
    "Someday (I'm Coming Back)",
    "It's Gonna Be a Lovely Day",
    "Waiting for You",
    "Trust in Me"
)

val songsInDarkSideOfTheMoon = listOf(
    "Speak to Me",
    "Breathe (In the Air)",
    "On the Run",
    "Time",
    "The Great Gig in the Sky"
)

val songsInTheGreatestHits = listOf(
    "Take It Easy",
    "Witchy Woman",
    "Lyin' Eyes",
    "Already Gone",
    "Desperado"
)

val songsInBatOutOfHell = listOf(
    "Bat Out of Hell",
    "You Took the Words Right Out of My Mouth",
    "Heaven Can Wait",
    "All Revved Up with No Place to Go"
)

val songsInHotelCalifornia = listOf(
    "Hotel California",
    "New Kid in Town",
    "Life in the Fast Lane",
    "Wasted Time"
)

val songsInComeOnOver = listOf(
    "Man! I Feel Like a Woman!",
    "I'm Holdin' On to Love",
    "Love Gets Me Every Time",
    "Don't Be Stupid",
    "From This Moment On",
    "Come On Over",
    "When",
    "Whatever You Do! Don't",
    "If You Wanna Touch Her, Ask!",
    "You're Still the One",
    "Honey, I'm Home",
    "That Don't Impress Me Much",
    "Black Eyes, Blue Tears",
    "I Won't Leave You Lonely",
    "Rock This Country!",
    "You've Got a Way"
)

val songsInRumours = listOf(
    "Second Hand News",
    "Dreams",
    "Never Going Back Again",
    "Don't Stop",
    "Go Your Own Way",
    "Songbird"
)

val songsInSaturdayNightFever = listOf(
    "Stayin' Alive",
    "How Deep Is Your Love",
    "Night Fever",
    "More Than a Woman",
    "If I Can't Have You"
)

val songsInLedZeppelinIv = listOf(
    "Black Dog",
    "Rock and Roll",
    "The Battle of Evermore",
    "Stairway to Heaven"
)

val songsInBad = listOf(
    "Bad",
    "The Way You Make Me Feel",
    "Speed Demon",
    "Liberian Girl",
    "Just Good Friends"
)

val songsInJaggedLittlePill = listOf(
    "All I Really Want",
    "You Oughta Know",
    "Perfect",
    "Hand in My Pocket",
    "Right Through You",
    "Forgiven",
    "You Learn",
    "Head over Feet",
    "Mary Jane",
    "Ironic",
    "Not the Doctor",
    "Wake Up"
)

val songsInDirtyDancing = listOf(
    "The Time of My Life",
    "Be My Baby",
    "She's Like the Wind",
    "Hungry Eyes",
    "Stay",
    "Yes",
    "You Don't Own Me",
    "Hey! Baby",
    "Overload",
    "Love Is Strange",
    "Where Are You Tonight?",
    "In the Still of the Night"
)

val songsInFallingIntoYou = listOf(
    "It's All Coming Back to Me Now",
    "Because You Loved Me",
    "Falling into You",
    "Make You Happy",
    "Seduces Me",
    "All by Myself",
    "Declaration of Love",
    "Dreamin' of You",
    "I Love You",
    "If That's What It Takes",
    "I Don't Know",
    "River Deep, Mountain High",
    "Call the Man",
    "Fly"
)

val songsIn21 = listOf(
    "Rolling in the Deep",
    "Rumour Has It",
    "Turning Tables",
    "Don't You Remember",
    "Set Fire to the Rain",
    "He Won't Go",
    "Take It All",
    "I'll Be Waiting",
    "One and Only",
    "Love Song"
)

val songsIn1 = listOf(
    "Ticket to Ride",
    "Help!",
    "Yesterday",
    "Day Tripper",
    "We Can Work It Out",
    "Paperback Writer",
    "Yellow Submarine",
    "Eleanor Rigby"
)

val correspondingTrackList = listOf(
    songsInThriller,
    songsInBackInBlack,
    songsInTheBodyguard,
    songsInDarkSideOfTheMoon,
    songsInTheGreatestHits,
    songsInBatOutOfHell,
    songsInHotelCalifornia,
    songsInComeOnOver,
    songsInRumours,
    songsInSaturdayNightFever,
    songsInLedZeppelinIv,
    songsInBad,
    songsInJaggedLittlePill,
    songsInDirtyDancing,
    songsInFallingIntoYou,
    songsIn21,
    songsIn1,
)

//val tracksMetadataList = mutableListOf<MediaMetadataCompat>().apply {
//    albumTitles.forEachIndexed { index, albumTitle ->
//        val _albumArtist = correspondingAlbumArtists[ index ]
//        val albumGenre = correspondingAlbumGenre[ index ]
//        val trackList = correspondingTrackList[ index ]
//        val currentAlbumId = ++currentAlbumId
//        trackList.forEach {
//            MediaMetadataCompat.Builder().apply {
//                id = ++currentTrackId
//                title = it
//                artist = _albumArtist
//                albumArtist = _albumArtist
//                genre = albumGenre
//                album = albumTitle
//                albumId = currentAlbumId
//                flag = MediaItem.FLAG_PLAYABLE
//                dateModified = 0L
//                size = 0L
//                path = ""
//                composer = ""
//                albumId = 0L
//                trackNumber = 1L
//                year = 0L
//                duration = 0L
//            }.build().also { add( it ) }
//        }
//    }
//}

//val albumMetadataList = mutableListOf<MediaMetadataCompat>().apply {
//    albumTitles.forEachIndexed { index, albumTitle ->
//        MediaMetadataCompat.Builder().apply {
//            id = ++currentAlbumId
//            title = albumTitle
//            artist = correspondingAlbumArtists[ index ]
//            genre = correspondingAlbumGenre[ index ]
//            flag = MediaItem.FLAG_BROWSABLE
//        }.build().also { add( it ) }
//    }
//}

//val albumArtistsMetadataList = mutableListOf<MediaMetadataCompat>().apply {
//    correspondingAlbumArtists.forEach {  albumArtist ->
//        MediaMetadataCompat.Builder().apply {
//            id = ++currentAlbumArtistId
//            title = albumArtist
//            flag = MediaItem.FLAG_BROWSABLE
//        }.build().also { add( it ) }
//    }
//}

//val genresMetadataList = mutableListOf<MediaMetadataCompat>().apply {
//    correspondingAlbumGenre.forEach { genreTitle ->
//        MediaMetadataCompat.Builder().apply {
//            id = ++currentGenreId
//            title = genreTitle
//            flag = MediaItem.FLAG_BROWSABLE
//        }.build().also { add( it ) }
//    }
//}



