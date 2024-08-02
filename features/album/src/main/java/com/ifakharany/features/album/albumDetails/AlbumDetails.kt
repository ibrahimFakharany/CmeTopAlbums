package com.ifakharany.features.album.albumDetails

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ifakharany.core.base.NavigationEvent
import com.ifakharany.core.navigation.Route
import com.ifakharany.core.theme.AppTheme
import com.ifakharany.features.album.albumList.AlbumListViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlbumDetailsScreen(navController: NavHostController) {

    val onPane = AppTheme.onePane
    val viewModel = hiltViewModel<AlbumDetailsViewModel>()
    val viewState = viewModel.viewState.value

    LaunchedEffect(Unit) {
        viewModel.navigationEventFlow.collect {
            when (it) {
                is NavigationEvent.NavigateBack -> {
                    if (onPane) {
                        navController.navigate(Route.AlbumsList.name) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }

    AlbumComponent(viewModel, viewState)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlbumComponent(
    viewModel: AlbumDetailsViewModel,
    viewState: AlbumDetailsState,
) {
    val uriHandler = LocalUriHandler.current
    val album = viewState.album

    if (album != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(album.image)
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 13.dp, bottom = 12.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = album.albumName,
                )
                Text(text = album.artistName)

                Spacer(modifier = Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    album.genres.forEach { genre ->
                        Text(
                            genre, modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Released " + album.releaseDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                )

                Text(text = "Copyright © 2024 Apple Inc. All rights reserved.")
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { uriHandler.openUri(album.albumUrl) }) {
                    Text(text = "View Album on iTunes")
                }
                Spacer(modifier = Modifier.height(47.dp))
            }
        }
    } else {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                text = "Choose an Album",
                textAlign = TextAlign.Center,
                fontSize = 27.sp,
            )
        }
    }
}
