package com.ifakharany.features.album.albumList

import com.ifakharany.core.base.ActionsEvent

sealed class AlbumListActionEvent : ActionsEvent() {
    data object RefreshAlbumList : AlbumListActionEvent()
}