package com.example.stickerjetpackcomp.utils

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.example.testfriends_jetpackcompose.navigation.Screen

class AppTheme {

    companion object {
        val HOME = Screen.Home
        val DETAILS = Screen.Details
        //val SHAPE = CircleShape
        val SHAPE= RoundedCornerShape(10.dp)
    }

}