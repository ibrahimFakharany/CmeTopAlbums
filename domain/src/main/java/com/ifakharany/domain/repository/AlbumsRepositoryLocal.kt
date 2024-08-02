package com.ifakharany.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ifakharany.datasource.local.dao.AlbumsDao
import com.ifakharany.domain.RequestResult
import com.ifakharany.domain.map.AlbumMapper
import com.ifakharany.domain.map.AlbumsEntityMapper
import com.ifakharany.domain.map.AlbumsMapper
import com.ifakharany.domain.model.Album
import com.ifakharany.domain.model.AlbumsResult
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumsRepositoryLocal @Inject constructor(
    private val albumsDao: AlbumsDao
) {

    suspend fun getAlbumsByPagingNotify(): Flow<RequestResult<AlbumsResult>> = flow {
        albumsDao.getAlbumsByPagingNotify().collect { data ->
            when (data) {
                is InitialResults -> {
                    if (data.list.size > 0) {
                        val result = AlbumsMapper.map(data.list)
                        emit(RequestResult.Success(result))
                    }
                }

                is UpdatedResults -> {
                    if (data.list.size > 0) {
                        val result = AlbumsMapper.map(data.list)
                        emit(RequestResult.Success(result))
                    }
                }
            }
        }
    }

    suspend fun getAlbumsByPaging(page: Int, limit: Int): RequestResult<List<Album>> =
        withContext(Dispatchers.IO) {

            val albumList = albumsDao.getAlbumsByPaging(page = page, limit = limit).toList()

            val result = AlbumsMapper.map(albumList)
            if (result.albumsList.isNotEmpty()) {
                return@withContext RequestResult.Success(result.albumsList)
            } else {
                return@withContext RequestResult.Error(Exception("albums empty"))
            }

        }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getById(id: String): RequestResult<Album> = withContext(Dispatchers.IO) {
        val album = albumsDao.getById(id = id)
        if (album != null) {
            val result = AlbumMapper.map(album)
            return@withContext RequestResult.Success(result)
        } else {
            return@withContext RequestResult.Error(Exception("album doesn't exist"))
        }
    }

    suspend fun insertAll(list: List<Album>): RequestResult<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val entityList = AlbumsEntityMapper.map(list)
                albumsDao.insertAll(entityList)
                return@withContext RequestResult.Success(true)
            } catch (e: Exception) {
                return@withContext RequestResult.Error(e)
            }
        }

    suspend fun deleteAll(): RequestResult<Boolean> = withContext(Dispatchers.IO) {
        try {
            albumsDao.deleteAll()
            return@withContext RequestResult.Success(true)
        } catch (e: Exception) {
            return@withContext RequestResult.Error(e)
        }
    }
}