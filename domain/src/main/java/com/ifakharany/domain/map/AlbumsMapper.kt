package com.ifakharany.domain.map

import com.ifakharany.datasource.local.entity.AlbumEntity
import com.ifakharany.datasource.remote.model.AlbumsResponse
import com.ifakharany.domain.model.AlbumsResult

class AlbumsMapper {
    companion object {
        fun map(entities: List<AlbumEntity>): AlbumsResult {

            val albumsResult = AlbumsResult()

            entities.forEach {
                albumsResult.albumsList.add(
                    AlbumMapper.map(it)
                )
            }

            return albumsResult
        }


        fun map(albumsResponse: AlbumsResponse): AlbumsResult {

            val albumsResult = AlbumsResult()

            albumsResponse.feed.results.forEachIndexed { index, result ->
                albumsResult.albumsList.add(
                    AlbumMapper.map(result, index)
                )
            }

            return albumsResult
        }
    }
}