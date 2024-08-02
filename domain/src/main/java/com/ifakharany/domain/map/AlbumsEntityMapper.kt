package com.ifakharany.domain.map

import com.ifakharany.datasource.local.entity.AlbumEntity
import com.ifakharany.domain.model.Album
import java.time.format.DateTimeFormatter

class AlbumsEntityMapper {
    companion object {
        fun map(entities: List<Album>): List<AlbumEntity> {

            val list: MutableList<AlbumEntity> = mutableListOf()
            entities.forEach { data ->

                list.add(AlbumEntity().apply {
                    index = data.index
                    id = data.id
                    albumUrl = data.albumUrl
                    artistName = data.artistName
                    albumName = data.albumName
                    genres = data.genres.joinToString(",")
                    image = data.image
                    releaseDate = data.releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                })
            }

            return list
        }
    }
}