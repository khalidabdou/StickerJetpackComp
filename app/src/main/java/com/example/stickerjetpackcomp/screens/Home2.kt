package com.example.stickerjetpackcomp.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.ui.theme.backgroundGradient
import com.example.stickerjetpackcomp.utils.AppUtil
import com.example.stickerjetpackcomp.utils.Config.Companion.ENABLE_ADS
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.ringtones.compose.feature.admob.AdvertView


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun Home2(navController: NavController, viewModel: StickerViewModel) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val packagename = context.packageName
    val message = viewModel.message.collectAsState()
    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty())
            try {
                viewModel.getStickers(context.packageName)
            } catch (ex: Exception) {
                Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
            }


    }

    val state = rememberLazyListState()
    var editable by remember { mutableStateOf(false) }


    Scaffold(
        backgroundColor = Color.White,
        modifier = Modifier,
        scaffoldState = scaffoldState,
        topBar = {
            //var message = "Good Morning"

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colors.background)
                    .padding(5.dp)
            ) {
                Text(
                    text = message.value,
                    style = MaterialTheme.typography.h4,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(5f)
                )
                AppBarIcon(icon = R.drawable.share) {
                    AppUtil.share(context)
                }
                AppBarIcon(icon = R.drawable.star) {
                    AppUtil.openStore(context)
                }
                Spacer(modifier = Modifier.width(3.dp))
                /*AppBarIcon(icon = R.drawable.share) {
                    shareWebp.share(context)
                }*/
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier.background(Color.White)
            ) {
                val listState = rememberLazyListState()
                Spacer(modifier = Modifier
                    .height(3.dp)
                    .background(MaterialTheme.colors.background))
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                        .background(backgroundGradient)
                        .height(100.dp)
                ) {
                    if (!viewModel.categories.value.isNullOrEmpty())
                        items(viewModel.categories.value!!.size) {
                            CategoryCompose(
                                cat = viewModel.categories.value!![it],
                                packagename = packagename,
                                onClick = {
                                    viewModel.cid = it
                                    navController.navigate(Screen.PacksByCategory.route)
                                })
                        } else {
                        item {
                            repeat(4) {
                                LoadingShimmerEffect()
                            }
                        }
                    }
                }
                if (ENABLE_ADS)
                    AdvertView()
            }

        }

    ) {

        LazyColumn(
            state = state,
            modifier = Modifier
                .padding(10.dp)
                .background(
                    Color.White
                )
        ) {

            item {
                Text(
                    text = stringResource(R.string.packs),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.background,
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
                            navController.navigate(Screen.Details2.route)
                        })
                }
            else item {
                repeat(5) {
                    LoadingShimmerEffect()
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }

        }


    }
}







