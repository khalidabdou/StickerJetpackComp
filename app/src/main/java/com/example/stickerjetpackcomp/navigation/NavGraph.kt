package com.example.testfriends_jetpackcompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stickerjetpackcomp.screens.*
import com.example.stickerjetpackcomp.viewModel.StickerViewModel


@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: StickerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(route = Screen.CheckInternet.route) {
            CheckInternet()
        }

        composable(route = Screen.Splash.route) {
            Splash(navController = navController, viewModel)
        }
        composable(route = Screen.Home.route) {
            Home(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Home2.route) {
            Home2(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.PacksByCategory.route) {
            PacksByCategory(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Details.route) {
            Details(viewModel = viewModel)
        }

        composable(route = Screen.Details2.route) {
            Details2(viewModel = viewModel)
        }

    }


}