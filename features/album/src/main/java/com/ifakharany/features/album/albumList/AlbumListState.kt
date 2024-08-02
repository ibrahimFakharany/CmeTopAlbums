package com.ifakharany.features.album.albumList

import com.ifakharany.core.base.BaseViewState
import com.ifakharany.core.base.ProgressState

data class AlbumListState(
    val networkError: Boolean = false,
    val progress: ProgressState = ProgressState.Gone
): BaseViewState()
