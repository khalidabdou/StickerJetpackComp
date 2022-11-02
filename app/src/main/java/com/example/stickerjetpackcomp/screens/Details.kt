package com.example.stickerjetpackcomp.screens


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.stickerjetpackcomp.BuildConfig
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.ui.theme.darkGray2
import com.example.stickerjetpackcomp.utils.Config.Companion.ENABLE_ADS
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_AUTHORITY
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_ID
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_NAME
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.downloadPR
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.path
import com.example.stickerjetpackcomp.utils.shareWebp.Companion.saveImageAndShare
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import com.ringtones.compose.feature.admob.*
import com.skydoves.landscapist.glide.GlideImage
import java.io.File

@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun Details(viewModel: StickerViewModel) {

    val pack = viewModel.detailsPack.value
    val state = rememberLazyListState()
    val state2 = rememberLazyGridState()
    val context = LocalContext.current
    var editable by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

    LaunchedEffect(pack!!.identifier) {
        viewModel.incrementView(pack.identifier.toInt())
        loadRewarded(context)
        showInterstitialAfterClick(context)
    }

    path = context.filesDir.toString() + "/stickers_asset"
    val myDir = File("${path}/")
    myDir.mkdirs()
    if (myDir.exists())
        myDir.delete()

    var resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            openDialog.value = false
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                showRewarded(context)
            } else {
                showInterstitial(context)
                Toast.makeText(context, "try later", Toast.LENGTH_LONG).show()
            }
        }

    fun openWhatsappActivityForResult() {
        val intent = Intent()
        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        intent.putExtra(EXTRA_STICKER_PACK_ID, pack.identifier)
        intent.putExtra(
            EXTRA_STICKER_PACK_AUTHORITY,
            BuildConfig.CONTENT_PROVIDER_AUTHORITY
        )
        intent.putExtra(EXTRA_STICKER_PACK_NAME, pack.name)
        resultLauncher.launch(intent)
    }

    if (viewModel.isReady.value) {
        openWhatsappActivityForResult()
        viewModel.isReady.value = false
        viewModel.index = 0
        viewModel.progress.value = 0
    }

    val isVisible = remember { mutableStateOf(value = false) }
    val sharedWebp = remember {
        mutableStateOf(value = pack.stickers[0].image_file)
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            OutlinedButton(
                onClick = {
                    //showInterstitial(context = context)
                    openDialog.value = true
                    isVisible.value = true
                    viewModel.download()
                    viewModel.incrementAddToWhatsapp(pack.identifier.toInt())
                    val trayImageFile = getLastBitFromUrl(pack.tray_image_file)
                    downloadPR(pack.tray_image_file, trayImageFile, pack)
                },

                modifier = Modifier.padding(7.dp),  //avoid the oval shape
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color.Green),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Green,
                    backgroundColor = darkGray
                )
            ) {

                Icon(Icons.Default.Add, contentDescription = "content description")
                Text(
                    text = stringResource(R.string.add_to_whatsapp),
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
        },

        bottomBar = {
            if (ENABLE_ADS)
                AdvertView()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkGray)
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(darkGray2)
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(
                            CircleShape
                        )
                        .background(backgroundWhite)
                        .padding(10.dp)
                ) {
                    GlideImage(
                        imageModel = pack.tray_image_file,
                        // Crop, Fit, Inside, FillHeight, FillWidth, None
                        contentScale = ContentScale.Crop,
                        // shows a placeholder while loading the image.
                        placeHolder = ImageBitmap.imageResource(R.mipmap.ic_launcher_foreground),
                        // shows an error ImageBitmap when the request failed.
                        error = ImageBitmap.imageResource(R.mipmap.ic_launcher_foreground),
                        modifier = Modifier.size(70.dp)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(
                        text = pack.name,
                        color = backgroundWhite,
                        style = MaterialTheme.typography.h6
                    )
                    LabelLikes(icon = R.drawable.eye, text = pack.views.toString())
                    LabelLikes(
                        icon = R.drawable.ic_dwonloaded,
                        text = pack.count_set_to_whatsapp.toString()
                    )
                }
                Column {
                    Button(
                        onClick = {
                            openDialog.value = true
                            isVisible.value = true
                            viewModel.download()
                            viewModel.incrementAddToWhatsapp(pack.identifier.toInt())
                            val trayImageFile = getLastBitFromUrl(pack.tray_image_file)
                            downloadPR(pack.tray_image_file, trayImageFile, pack)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green.copy(0.8f),
                            contentColor = darkGray
                        )
                    ) {
                        Text(text = stringResource(R.string.add_to_whatsapp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.whatsapp),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            GridStickers(state = state2, pack = pack, context = context, onClick = {
                showInterstitialAfterClick(context)
                sharedWebp.value = pack.stickers[it].image_file
                editable = true
            })
        }
        SingleSticker(context, sharedWebp.value, editable, onClick = {
            editable = it
        })

        if (openDialog.value)
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Please wait")
                },
                confirmButton = {
                    Box(modifier = Modifier.size(100.dp)) {
                        CustomComponent(
                            indicatorValue = viewModel.progress.value,
                            maxIndicatorValue = 100,
                            bigTextColor = darkGray,
                            foregroundIndicatorColor = Green,
                            backgroundIndicatorColor = darkGray2,
                            backgroundIndicatorStrokeWidth = 30f,
                            foregroundIndicatorStrokeWidth = 30f
                        )
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
    }
}


@ExperimentalAnimationApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridStickers(
    pack: StickerPack,
    state: LazyGridState,
    context: Context,
    onClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        state = state,
        modifier = Modifier.padding(bottom = 130.dp)
    ) {
        items(pack.stickers.size) { index ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .clickable {
                        onClick(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                //"${pack.stickers[index].image_file}"
                StickerView(context = context, "${pack.stickers[index].image_file}")
            }
        }
    }
}

@Composable
private fun StickerView(context: Context, resource: String) {
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    imageLoader.memoryCache


    val painter = rememberAsyncImagePainter(
        model = resource,
        imageLoader = imageLoader,
    )

    Column(modifier = Modifier.padding(5.dp)) {
        Icon(
            Icons.Rounded.Share,
            tint = Color.DarkGray,
            modifier = Modifier.size(10.dp),
            contentDescription = "Localized description"
        )
        Image(
            painter = painter, contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun LabelLikes(icon: Int, text: String) {
    Row(
        modifier = Modifier
            .height(20.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Logo Icon",
            tint = backgroundWhite,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = backgroundWhite.copy(0.9f))
    }
}

@Composable
fun SingleSticker(
    context: Context,
    resource: String,
    editable: Boolean,
    onClick: (Boolean) -> Unit
) {
    val density = LocalDensity.current
    AnimatedVisibility(visible = editable,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Black.copy(0.3f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick(!editable)
                }
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(White)
                    .blur(radius = 16.dp)
            ) {

                Column {
                    Box(modifier = Modifier.weight(3f)) {
                        StickerView(context, resource)
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        OutlinedButton(
                            modifier = Modifier,
                            onClick = {
                                val loader = ImageLoader(context)
                                val req = ImageRequest.Builder(context)
                                    .data(resource) // demo link
                                    .target { result ->
                                        val bitmap = (result as BitmapDrawable).bitmap
                                        val trayImageFile = getLastBitFromUrl(resource)
                                        saveImageAndShare(bitmap, trayImageFile, context)
                                    }
                                    .build()

                                val disposable = loader.enqueue(req)

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Loc",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Share")
                        }

                    }


                }
            }

            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(White),
                imageVector = Icons.Default.Close, contentDescription = ""
            )
        }

    }
}

