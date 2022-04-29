package com.example.stickerjetpackcomp.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.ui.theme.Purple200
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.viewModel.StickerViewModel


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun Home(navController: NavController, viewModel: StickerViewModel) {

    Scaffold(topBar = { AppBar(icon = R.drawable.ic_language_24, onClick = {}) }
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),

            ) {
            items(5){
                Category()
            }

        }



    }
}

@Composable
fun Category() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Purple200)
            .padding(10.dp)

    ) {
        Icon(
            painter = painterResource(id = R.drawable.funny),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )
        Text(text = "Funny", style = MaterialTheme.typography.h4, color = Color.Black)
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row() {
                repeat(3) {
                    Image(
                        painter = painterResource(id = R.drawable.sticker_2),
                        contentDescription = "", modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(darkGray.copy(0.7f))
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
            Text(text = "+12", style = MaterialTheme.typography.h6)
        }
    }
}

@Preview
@Composable
fun CategoryPrev() {
    Category()
}

val stickers = listOf(R.drawable.sticker_2, R.drawable.sticker_3, R.drawable.sticker_4)





