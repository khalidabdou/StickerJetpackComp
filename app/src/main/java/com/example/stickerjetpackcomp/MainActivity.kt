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
import okhttp3.OkHttpClient

@ExperimentalAnimationApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StickerJetpackCompTheme {


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
