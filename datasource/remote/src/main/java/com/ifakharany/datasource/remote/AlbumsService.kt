package com.ifakharany.datasource.remote

import com.ifakharany.datasource.remote.model.AlbumsResponse
import retrofit2.Response
import retrofit2.http.GET

interface AlbumsService {
    @GET("us/music/most-played/100/albums.json")
    suspend fun fetchTopAlbums(): Response<AlbumsResponse>
}