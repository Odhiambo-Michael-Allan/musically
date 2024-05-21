package com.odesa.musicMatters.utils

object FileExplorer {

    class Path( val parts: List<String> ) {
        constructor( path: String ) : this( intoParts( path ) )

        val dirName: Path
            get() = Path( parts.subList( 0, parts.size - 1 ) )
        val hasChildParts: Boolean
            get() = parts.size > 1
        val firstPart: String
            get() = parts.first()
        val basename: String
            get() = parts.last()
        val isAbsolute: Boolean
            get() = parts.firstOrNull() == ""
        val isFile: Boolean
            get() = isFileRegex.containsMatchIn( basename )

        fun shift() = Path( parts.subList( 1, parts.size ) )

        fun resolve( to: Path ): Path {
            if ( to.isAbsolute ) return to
            val a = parts.toMutableList()
            val b = to.parts.toMutableList()
            while ( true ) {
                when ( b.firstOrNull() ) {
                    "." -> b.removeFirst()
                    ".." -> {
                        b.removeFirst()
                        a.removeLast()
                    }
                    else -> break
                }
            }
            a.addAll( b )
            return Path( a )
        }

        override fun toString() = parts.joinToString( "/" )

        companion object {
            private val isFileRegex = Regex( """.+\..+""" )
            private val intoPartsRegex = Regex( """[\\/]""" )

            fun isAbsolute( path: String ) = path.startsWith( "/" )

            private fun intoParts( path: String ) =
                path.split( intoPartsRegex ).filter { it.isNotBlank() }
        }
    }
}