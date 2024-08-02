package com.ifakharany.features.album.albumList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ifakharany.core.navigation.Route
import com.ifakharany.core.theme.AppTheme
import com.ifakharany.domain.model.Album
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ifakharany.core.MeasureUnconstrainedViewWidth
import com.ifakharany.core.base.ProgressState
import com.ifakharany.core.calculateCurrentSize
import com.ifakharany.core.getFraction
import com.ifakharany.core.theme.Purple40

@Composable
fun AlbumListScreen(navController: NavHostController) {

    val isSingle = AppTheme.onePane
    val viewModel = hiltViewModel<AlbumListViewModel>()

    val onTriggerEvents = viewModel::onTriggerEvent
    val viewState = viewModel.viewState.value

    val lazyAlbumList: LazyPagingItems<Album> = viewModel.lazyAlbumsItems.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.navigationEventFlow.collect {
            when (it) {
                is AlbumListNavigationEvent.NavigateToAlbumSingle -> {
                    val link = Route.AlbumsDetails.name
                    if (isSingle) {
                        navController.navigate(link)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.actionsEventFlow.collect {
            when (it) {
                is AlbumListActionEvent.RefreshAlbumList -> {
                    lazyAlbumList.refresh()
                }
            }
        }
    }

    AlbumListComponent(
        viewModel, viewState, onTriggerEvents, lazyAlbumList
    )
}

@Composable
fun AlbumListComponent(
    viewModel: AlbumListViewModel,
    viewState: AlbumListState,
    onTriggerEvents: (AlbumListEvent) -> Unit,
    lazyAlbumList: LazyPagingItems<Album>
) {
    val lazyGridState = rememberLazyGridState()

    val toolbarLabel: String = "Top 100 Albums"
    var maximumScroll: Float
    var toolbarExpandedHeight: Float
    var toolbarCollapsedHeight: Float

    var toolbarTextSizeExpanded: Float
    var toolbarTextSizeCollapsed: Float

    with(LocalDensity.current) {
        maximumScroll = 30.dp.toPx()

        toolbarExpandedHeight = 65.dp.toPx()
        toolbarCollapsedHeight = 43.dp.toPx()

        toolbarTextSizeExpanded = 34.sp.toPx()
        toolbarTextSizeCollapsed = 16.sp.toPx()
    }

    val currentOffset: Float by remember {
        derivedStateOf {
            val offset = lazyGridState.firstVisibleItemScrollOffset
            val index = lazyGridState.firstVisibleItemIndex
            println("offset: $offset, index: $index")
            if (lazyGridState.firstVisibleItemIndex > 0) {
                maximumScroll
            } else {
                offset.toFloat().coerceIn(0f, maximumScroll)
            }
        }
    }

    var toolbarHeight = 0.dp
    var toolbarHeightPx = 0f
    var toolbarTextSize = 0.sp
    var fraction = 0f
    with(LocalDensity.current) {
        fraction = getFraction(maximumScroll, currentOffset)
        toolbarHeightPx =
            calculateCurrentSize(toolbarCollapsedHeight, toolbarExpandedHeight, fraction)
        toolbarHeight = toolbarHeightPx.toDp()
        toolbarTextSize =
            calculateCurrentSize(toolbarTextSizeCollapsed, toolbarTextSizeExpanded, fraction).toSp()
    }

    val lazyAlbumsIsRefreshing = lazyAlbumList.loadState.refresh is LoadState.Loading

    var swipeEnabled by remember { mutableStateOf(false) }
    var loadingProgressBarEnabled by remember { mutableStateOf(false) }
    swipeEnabled = !lazyAlbumsIsRefreshing && lazyAlbumList.itemCount <= 0 && viewState.networkError

    loadingProgressBarEnabled =
        viewState.progress == ProgressState.Loading && (!lazyAlbumsIsRefreshing && lazyAlbumList.itemCount <= 0)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(lazyAlbumsIsRefreshing), onRefresh = {
                onTriggerEvents(AlbumListEvent.SwipeRefresh)
            }, swipeEnabled = swipeEnabled
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp, top = 15.dp + 65.dp, bottom = 40.dp
                )
            ) {
                items(lazyAlbumList.itemCount) { index ->
                    val data = lazyAlbumList.get(index)
                    data?.let {
                        AlbumItem(album = data, onAlbumClick = {
                            onTriggerEvents(AlbumListEvent.OnAlbumClick(data.id))
                        })
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(toolbarHeight)
                    .background(Purple40)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    MeasureUnconstrainedViewWidth(viewToMeasure = {
                        Text(
                            text = toolbarLabel, fontSize = toolbarTextSize
                        )
                    }) { measuredEndWidth, measuredEndHeight ->
                        // Our text view that we are animating based on the returned measured "end" values
                        Text(
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)

                                layout(constraints.maxWidth, constraints.maxHeight) {
                                    val xOffsetEnd =
                                        (constraints.maxWidth / 2) - (measuredEndWidth / 2)

                                    val xOffset =
                                        calculateCurrentSize(xOffsetEnd.toFloat(), 0f, fraction)

                                    val yOffset = (toolbarHeightPx / 2) - (placeable.height / 2)
                                    placeable.placeRelative(xOffset.toInt(), yOffset.toInt())
                                }
                            },
                            text = toolbarLabel,
                            fontSize = toolbarTextSize,

                            )
                    }
                }
            }
            AnimatedVisibility(swipeEnabled, enter = fadeIn(), exit = fadeOut()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = "Something went wrong! Swipe to Refresh",
                    )
                }
            }
        }

        AnimatedVisibility(loadingProgressBarEnabled, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
private fun AlbumItem(
    album: Album, onAlbumClick: (album: Album) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(shape = RoundedCornerShape(20.dp))
        .clickable {
            onAlbumClick(album)
        }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(album.image).crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, end = 13.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.Start,

            ) {
            Text(
                text = album.albumName,
                color = Color.White
            )
            Text(
                text = album.artistName,
                color = Color.White

            )
        }
    }
}