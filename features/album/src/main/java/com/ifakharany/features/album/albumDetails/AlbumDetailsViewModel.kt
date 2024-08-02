package com.ifakharany.features.album.albumDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.ifakharany.core.base.BaseViewModel
import com.ifakharany.core.base.DataState
import com.ifakharany.core.base.NavigationEvent
import com.ifakharany.core.util.DataStoreManager
import com.ifakharany.domain.usecase.GetAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AlbumDetailsViewModel
@Inject constructor(
    private val getAlbumSingleUseCase: GetAlbumUseCase, private val dataStore: DataStoreManager
) : BaseViewModel<AlbumDetailsState, AlbumDetailsEvent>() {

    override val viewState: MutableState<AlbumDetailsState> = mutableStateOf(AlbumDetailsState())

    init {
        viewModelScope.launch {
            dataStore.getAlbumId.collect { albumId ->
                getAlbum(albumId)
            }
        }
    }

    override fun onTriggerEvent(event: AlbumDetailsEvent) {

        when (event) {
            is AlbumDetailsEvent.OnBackClick -> {
                viewModelScope.launch {
                    _navigationEventFlow.emit(NavigationEvent.NavigateBack)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAlbum(albumId: String?) {
        viewModelScope.launch {
            albumId?.let {
                getAlbumSingleUseCase.execute(it).collect { result ->
                    when (result) {
                        is DataState.Data -> {
                            viewState.value = viewState.value.copy(
                                album = result.data
                            )
                        }

                        is DataState.Error -> {

                        }

                        is DataState.Loading -> {

                        }
                    }
                }
            }
        }
    }
}


