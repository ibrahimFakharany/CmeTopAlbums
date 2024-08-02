package com.ifakharany.domain.usecase

import com.ifakharany.core.base.DataState
import com.ifakharany.core.base.ProgressState
import com.ifakharany.domain.RequestResult
import com.ifakharany.domain.repository.AlbumsRepositoryLocal
import com.ifakharany.domain.result.AlbumsNotifyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAlbumsNotifyUseCase @Inject constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal
) {
    fun execute(): Flow<DataState<AlbumsNotifyResult>> = flow {
        emit(DataState.Loading(progressState = ProgressState.Loading))

        albumsRepositoryLocal.getAlbumsByPagingNotify().collect { result ->
            when (result) {
                is RequestResult.Error -> {
                    emit(DataState.Data(AlbumsNotifyResult.Error))
                }

                is RequestResult.Success -> {
                    emit(DataState.Data(AlbumsNotifyResult.RecordsInserted))
                }
            }
        }

    }
}
