package com.example.testfriends_jetpackcompose.navigation

sealed class Screen (val route :String) {
    object Splash : Screen(route = "splash_screen")
    object Home :Screen(route = "Home_screen")
    object Details : Screen(route = "details_screen")
    object PacksByCategory : Screen(route = "packs_category")
    object CheckInternet : Screen(route = "check_internet")

}