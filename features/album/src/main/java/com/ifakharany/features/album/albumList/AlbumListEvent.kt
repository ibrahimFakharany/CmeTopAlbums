package com.ifakharany.features.album.albumList

import com.ifakharany.core.base.BaseViewEvent


sealed class AlbumListEvent : BaseViewEvent() {
    data class OnAlbumClick(val id: String) : AlbumListEvent()
    data object SwipeRefresh : AlbumListEvent()
}