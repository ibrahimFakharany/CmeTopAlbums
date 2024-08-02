package com.ifakharany.domain.paging

data class AlbumsFilterQuery (
    var per_page: Int = 10,
    var page: Int = 0
)