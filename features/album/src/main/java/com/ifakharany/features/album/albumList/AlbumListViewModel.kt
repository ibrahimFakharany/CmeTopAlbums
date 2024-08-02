package com.ifakharany.features.album.albumList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ifakharany.core.base.BaseViewModel
import com.ifakharany.core.base.DataState
import com.ifakharany.core.base.ProgressState
import com.ifakharany.core.util.DataStoreManager
import com.ifakharany.domain.model.Album
import com.ifakharany.domain.usecase.GetAlbumsNotifyUseCase
import com.ifakharany.domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val albumsUseCase: GetAlbumsUseCase,
    private val getAlbumsNotifyUseCase: GetAlbumsNotifyUseCase,
    private val dataStore: DataStoreManager
) : BaseViewModel<AlbumListState, AlbumListEvent>() {

    override val viewState: MutableState<AlbumListState> = mutableStateOf(AlbumListState())

    lateinit var lazyAlbumsItems: Flow<PagingData<Album>>

    init {
        LoadAlbumsRemote()
        ChangeAlbumsListData()
        NotifyOnAlbumsChange()
    }

    override fun onTriggerEvent(event: AlbumListEvent) {
        when (event) {
            is AlbumListEvent.OnAlbumClick -> {
                viewModelScope.launch {
                    dataStore.setAlbumId(event.id)
                    _navigationEventFlow.emit(AlbumListNavigationEvent.NavigateToAlbumSingle)

                }
            }

            is AlbumListEvent.SwipeRefresh -> {
                viewModelScope.launch {
                    LoadAlbumsRemote()
                }
            }
        }
    }

    private fun LoadAlbumsRemote() {
        albumsUseCase.executeRemote().onEach { data ->
            when (data) {
                is DataState.Data -> {
                    viewState.value = viewState.value.copy(
                        networkError = false, progress = ProgressState.Gone
                    )
                }

                is DataState.Error -> {
                    // if local database is empty and there is an issue with the remote request
                    // user can swipe to try again
                    viewState.value = viewState.value.copy(
                        networkError = true, progress = ProgressState.Gone
                    )

                }

                is DataState.Loading -> {
                    // if local database is empty we show the circle progress
                    viewState.value = viewState.value.copy(progress = ProgressState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun ChangeAlbumsListData() {
        lazyAlbumsItems = albumsUseCase.execute(pageSize = 10)
    }

    private fun NotifyOnAlbumsChange() {
        getAlbumsNotifyUseCase.execute().onEach { data ->
            when (data) {
                is DataState.Data -> {
                    _actionsEventFlow.emit(AlbumListActionEvent.RefreshAlbumList)
                }

                is DataState.Error -> {

                }

                is DataState.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }
}


