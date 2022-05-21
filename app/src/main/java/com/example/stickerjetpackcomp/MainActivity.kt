package com.example.stickerjetpackcomp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.example.stickerjetpackcomp.ui.theme.StickerJetpackCompTheme
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.SetupNavGraph
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var config = PRDownloaderConfig.newBuilder()
        .setReadTimeout(30000)
        .setConnectTimeout(30000)
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StickerJetpackCompTheme {
                Hawk.init(this).build()
                PRDownloader.initialize(applicationContext, config)
                val viewModel: StickerViewModel = hiltViewModel()
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
