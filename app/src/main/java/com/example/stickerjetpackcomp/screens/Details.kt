package com.example.stickerjetpackcomp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.darkGray

@Composable
fun Details() {

    Scaffold(topBar = { AppBar()}) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(backgroundWhite)
            .padding(20.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)) {
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(
                            CircleShape
                        )
                        .background(darkGray)
                        .padding(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sticker_4),
                        contentDescription = "",
                        )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column() {
                    Text(text = "Sticker pack ", color = darkGray, style = MaterialTheme.typography.h1)
                    LabelLikes(icon = R.drawable.eye, text = "10k")
                    LabelLikes(icon = R.drawable.favorite, text = "1506")
                }

            }
        }
    }

}

@Composable
fun LabelLikes(icon :Int,text:String) {
    Row(modifier = Modifier.width(200.dp)) {
        Icon(painter = painterResource(id = icon),
            contentDescription = "Logo Icon",
            tint = Color.White,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = text, color = darkGray.copy(0.9f))
    }
}