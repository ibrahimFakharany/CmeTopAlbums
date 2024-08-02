package com.ifakharany.features.album.albumDetails

import com.ifakharany.core.base.BaseViewEvent

sealed class AlbumDetailsEvent : BaseViewEvent(){
    data object OnBackClick: AlbumDetailsEvent()
}