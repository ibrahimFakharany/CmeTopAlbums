package com.ifakharany.core.navigation

sealed class Route(val name: String) {
    data object AlbumsList : Route(name = "albumsList")
    data object AlbumsDetails : Route(name = "albumDetails")
}