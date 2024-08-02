package com.ifakharany.cmetopalbums

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ifakharany.core.navigation.Route
import com.ifakharany.core.theme.AppTheme
import com.ifakharany.core.theme.CmeTopAlbumsTheme
import com.ifakharany.features.album.albumDetails.AlbumDetailsScreen
import com.ifakharany.features.album.albumList.AlbumListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CmeTopAlbumsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppWrapper()
                }
            }
        }
    }
}


@Composable
fun AppWrapper() {
    val navController = rememberNavController()

    if (AppTheme.onePane) {
        NavHost(
            navController = navController,
            startDestination = Route.AlbumsList.name
        ) {

            composable(
                Route.AlbumsList.name
            ) {
                AlbumListScreen(navController = navController)
            }
            composable(
                Route.AlbumsDetails.name
            ) {
                AlbumDetailsScreen(navController = navController)
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                AlbumListScreen(navController = navController)
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AlbumDetailsScreen(navController = navController)
                }
            }
        }
    }
}