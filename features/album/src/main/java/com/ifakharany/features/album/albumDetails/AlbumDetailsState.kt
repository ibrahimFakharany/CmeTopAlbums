package com.ifakharany.features.album.albumDetails

import com.ifakharany.core.base.BaseViewState
import com.ifakharany.domain.model.Album

data class AlbumDetailsState(
    val album: Album? = null,
    val copyright: String? = null,
) : BaseViewState()
