package com.ifakharany.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ifakharany.domain.RequestResult
import com.ifakharany.domain.model.Album
import com.ifakharany.domain.repository.AlbumsRepositoryLocal

class AlbumsPaging constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal,
    private val filter: AlbumsFilterQuery
) : PagingSource<Int, Album>() {
    override fun getRefreshKey(state: PagingState<Int, Album>): Int?
    {
        return null
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        val nextPage = params.key ?: 1

        return try {
            filter.page = nextPage

            when (val result = albumsRepositoryLocal.getAlbumsByPaging(page = filter.page, limit = filter.per_page)) {
                is RequestResult.Error -> {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
                is RequestResult.Success -> {
                    LoadResult.Page(
                        data = result.data,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = if (result.data.isEmpty()) null else nextPage + 1
                    )
                }
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
