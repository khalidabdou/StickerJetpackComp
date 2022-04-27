package com.example.testfriends_jetpackcompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stickerjetpackcomp.screens.Details
import com.example.stickerjetpackcomp.screens.Home
import com.example.stickerjetpackcomp.screens.Splash


@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,

) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            Splash(navController = navController)
        }
        composable(route = Screen.Home.route) {
            Home(navController = navController)
        }
        composable(route = Screen.Details.route) {
            Details()
        }

    }
}