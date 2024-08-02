package com.ifakharany.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.ifakharany.core.base.DataState
import com.ifakharany.core.base.ProgressState
import com.ifakharany.domain.RequestResult
import com.ifakharany.domain.model.Album
import com.ifakharany.domain.repository.AlbumsRepositoryLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.GeneralSecurityException
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun execute(id: String): Flow<DataState<Album>> = flow {
        emit(DataState.Loading(progressState = ProgressState.Loading))

        when (val data = albumsRepositoryLocal.getById(id = id)) {
            is RequestResult.Success -> {
                emit(DataState.Data(data = data.data))
            }

            is RequestResult.Error -> {
                emit(DataState.Error(error = GeneralSecurityException()))
            }
        }
    }
}
