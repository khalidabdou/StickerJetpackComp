package com.example.testfriends_jetpackcompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stickerjetpackcomp.screens.Details
import com.example.stickerjetpackcomp.screens.Home
import com.example.stickerjetpackcomp.screens.PacksByCategory
import com.example.stickerjetpackcomp.screens.Splash
import com.example.stickerjetpackcomp.viewModel.StickerViewModel


@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel:StickerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PacksByCategory.route
    ) {
        composable(route = Screen.Splash.route) {
            Splash(navController = navController)
        }
        composable(route = Screen.Home.route) {
            Home(navController = navController,viewModel=viewModel)
        }
        composable(route = Screen.PacksByCategory.route) {
            PacksByCategory(navController = navController,viewModel=viewModel)
        }
        composable(route = Screen.Details.route) {
            Details(viewModel = viewModel)
        }

    }
}