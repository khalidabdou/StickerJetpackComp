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
import androidx.navigation.compose.rememberNavController
import com.example.stickerjetpackcomp.ui.theme.StickerJetpackCompTheme
import com.example.testfriends_jetpackcompose.navigation.SetupNavGraph


@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StickerJetpackCompTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                )
            }
        }
    }
}
