package com.ifakharany.features.album.albumList

import com.ifakharany.core.base.NavigationEvent

sealed class AlbumListNavigationEvent : NavigationEvent() {
    data object NavigateToAlbumSingle : AlbumListNavigationEvent()
}