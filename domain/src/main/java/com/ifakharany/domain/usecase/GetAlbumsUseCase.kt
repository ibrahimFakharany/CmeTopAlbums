package com.ifakharany.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ifakharany.core.base.DataState
import com.ifakharany.core.base.ProgressState
import com.ifakharany.domain.RequestResult
import com.ifakharany.domain.model.Album
import com.ifakharany.domain.model.AlbumsResult
import com.ifakharany.domain.paging.AlbumsFilterQuery
import com.ifakharany.domain.paging.AlbumsPaging
import com.ifakharany.domain.repository.AlbumsRepositoryLocal
import com.ifakharany.domain.repository.AlbumsRepositoryRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal,
    private val albumsRepositoryRemote: AlbumsRepositoryRemote
) {
    fun executeRemote(): Flow<DataState<AlbumsResult>> = flow {
        emit(DataState.Loading(progressState = ProgressState.Loading))

        when (val data =
            albumsRepositoryRemote.fetchTopAlbums()) {
            is RequestResult.Success -> {

                when (val deleteAllData = albumsRepositoryLocal.deleteAll()) {
                    is RequestResult.Error -> {
                        emit(DataState.Error(error = deleteAllData.exception))
                    }

                    is RequestResult.Success -> {
                        when (val insertAllData =
                            albumsRepositoryLocal.insertAll(data.data.albumsList)) {
                            is RequestResult.Error -> {
                                emit(DataState.Error(error = insertAllData.exception))
                            }

                            is RequestResult.Success -> {
                            }
                        }
                    }
                }

                emit(DataState.Data(data = data.data))
            }

            is RequestResult.Error -> {
                emit(DataState.Error(error = data.exception))
            }
        }
    }

    fun execute(pageSize: Int): Flow<PagingData<Album>> {

        val albums: Flow<PagingData<Album>> = Pager(PagingConfig(pageSize = pageSize)) {
            AlbumsPaging(
                filter = AlbumsFilterQuery(per_page = pageSize),
                albumsRepositoryLocal = albumsRepositoryLocal
            )
        }.flow

        return albums

    }
}
