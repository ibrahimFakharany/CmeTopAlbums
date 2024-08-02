package com.ifakharany.domain.repository

import com.ifakharany.core.util.DataStoreManager
import com.ifakharany.datasource.remote.AlbumsService
import com.ifakharany.domain.RequestResult
import com.ifakharany.domain.map.AlbumsMapper
import com.ifakharany.domain.model.AlbumsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumsRepositoryRemote @Inject constructor(
    private val apiService: AlbumsService,
) {
    suspend fun fetchTopAlbums(): RequestResult<AlbumsResult> = withContext(
        Dispatchers.IO
    ) {
        try {
            val network = apiService.fetchTopAlbums()

            val networkResponse = network.body()!!

            val result = AlbumsMapper.map(networkResponse)

            return@withContext RequestResult.Success(result)

        } catch (e: Exception) {
            return@withContext (RequestResult.Error(e))
        }
    }
}