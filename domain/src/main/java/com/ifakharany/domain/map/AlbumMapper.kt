package com.ifakharany.domain.map

import android.os.Build
import androidx.annotation.RequiresApi
import com.ifakharany.datasource.local.entity.AlbumEntity
import com.ifakharany.datasource.remote.model.AlbumsResponse
import com.ifakharany.domain.model.Album
import java.time.LocalDate

class AlbumMapper {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun map(entity: AlbumEntity): Album {

            val releasedDate: LocalDate = LocalDate.parse(entity.releaseDate)

            return Album(
                index = entity.index,
                id = entity.id,
                albumUrl = entity.albumUrl,
                artistName = entity.artistName,
                albumName = entity.albumName,
                genres = entity.genres.split(","),
                image = entity.image,
                releaseDate = releasedDate
            )
        }


        fun map(entity: AlbumsResponse.Feed.Result, index: Int): Album {

            val releasedDate: LocalDate = LocalDate.parse(entity.releasedDate)

            return Album(
                index = index,
                id = entity.id,
                albumUrl = entity.url,
                artistName = entity.artistName,
                albumName = entity.name,
                genres = entity.genres.map { it.name },
                image = entity.artworkUrl100,
                releaseDate = releasedDate
            )
        }
    }
}