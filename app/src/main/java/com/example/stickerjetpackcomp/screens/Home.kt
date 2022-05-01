package com.example.stickerjetpackcomp.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.Category
import com.example.stickerjetpackcomp.model.categories
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.colors
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.viewModel.StickerViewModel


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun Home(navController: NavController, viewModel: StickerViewModel) {

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty())
            viewModel.getStickers()
    }

    val state = rememberLazyListState()

    Scaffold(
        topBar = { AppBar(icon = R.drawable.ic_language_24, onClick = {}) },
        scaffoldState = scaffoldState,
        backgroundColor = Color.White
    ) {


        LazyColumn(
            state = state,
            modifier = Modifier

        ) {
            item() {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.h3,
                    color = darkGray,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(8.dp),

                    ) {
                    items(6) {
                        Category(colors[it], categories[it])
                    }
                }

            }
            item {
                Text(
                    text = "Popular",
                    style = MaterialTheme.typography.h3,
                    color = darkGray,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                )
            }
            if (viewModel.stickers.value != null) {
                items(4) {
                    Popular(viewModel.stickers.value!![it])
                }
            } else
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            color = darkGray, modifier = Modifier
                                .size(30.dp)

                        )
                    }

                }


        }


    }
}

@Composable
fun Category(color: Color, cat: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)

    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = cat.icon),
                tint = color,
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
        }
        Text(text = cat.name, style = MaterialTheme.typography.body1, color = Color.Black)
    }
}


@ExperimentalAnimationApi
@Composable
fun Popular(pack: StickerPack) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Pack(sticker = pack, onClick = {})
    }

}

@Preview
@Composable
fun CategoryPrev() {
    //Category(colors[0])
}

val stickers = listOf(R.drawable.sticker_2, R.drawable.sticker_3, R.drawable.sticker_4)





