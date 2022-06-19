package com.example.stickerjetpackcomp.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.Category
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.ui.theme.colors
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.utils.Config
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun Home(navController: NavController, viewModel: StickerViewModel) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty())
            viewModel.getStickers()
        if  (viewModel.categories.value.isNullOrEmpty())
            viewModel.getCategories()
            Log.d("cats","")
    }

    val state = rememberLazyListState()

    Scaffold(
        backgroundColor = Color.White,
        modifier = Modifier,
        scaffoldState = scaffoldState
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .padding(10.dp)
                .background(
                    Color.White
                )
        ) {
            item() {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.h4,
                    color = darkGray,
                    modifier = Modifier.padding(start = 8.dp)
                )
                val listState = rememberLazyListState()
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                ) {
                    if (!viewModel.categories.value.isNullOrEmpty())
                    items(viewModel.categories.value!!.size-1) {
                        Category(
                            colors[it],
                            viewModel.categories.value!![it],
                            onClick = {
                                viewModel.cid=  it
                                navController.navigate(Screen.PacksByCategory.route)
                            })
                    }
                }
            }
            item {
                Text(
                    text = "Latest",
                    style = MaterialTheme.typography.h4,
                    color = darkGray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (viewModel.stickers.value != null)
                items(viewModel.stickers.value!!.size) {
                    Popular(viewModel.stickers.value!![it],
                        onClick = {
                            viewModel.isReady.value = false
                            viewModel.index = 0
                            viewModel.setDetailPack(viewModel.stickers.value!![it])
                            navController.navigate(Screen.Details.route)
                        })
                }
        }
    }
}

@Composable
fun Category(
    color: Color,
    cat: Category,
    onClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)

    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(0.4f))
                .clickable {
                    onClick(cat.id)
                },
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                imageModel = Config.BASE_URL+"categories/${cat.image}",
                // Crop, Fit, Inside, FillHeight, FillWidth, None
                contentScale = ContentScale.Crop,
                // shows a placeholder while loading the image.
                placeHolder = ImageBitmap.imageResource(R.drawable.sticker),
                // shows an error ImageBitmap when the request failed.
                error = ImageBitmap.imageResource(R.drawable.sticker),
                modifier = Modifier.size(70.dp).padding(6.dp)
            )
        }
        Text(text = cat.name, style = MaterialTheme.typography.body1, color = Color.Black)
    }
}


@ExperimentalAnimationApi
@Composable
fun Popular(pack: StickerPack, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        Pack(sticker = pack, onClick = {
            onClick()
        })
    }

}

@Preview
@Composable
fun CategoryPrev() {
    //Category(colors[0])
}

val stickers = listOf(R.drawable.sticker_2, R.drawable.sticker_3, R.drawable.sticker_4)





