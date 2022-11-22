package com.example.stickerjetpackcomp.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun CheckInternet() {
    Box(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) MaterialTheme.colors.background else MaterialTheme.colors.onBackground)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = {

            },

            modifier = Modifier.padding(7.dp),  //avoid the oval shape
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, Color.Green),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Green,
                backgroundColor = MaterialTheme.colors.background
            )
        ) {

            Icon(Icons.Default.Add, contentDescription = "content description")
            Text(text = "Check Internet", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}