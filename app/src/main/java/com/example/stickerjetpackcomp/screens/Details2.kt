package com.example.stickerjetpackcomp.screens


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.stickerjetpackcomp.BuildConfig
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.AdProvider
import com.example.stickerjetpackcomp.ui.theme.backgroundGradient
import com.example.stickerjetpackcomp.utils.AppTheme.Companion.SHAPE
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_AUTHORITY
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_ID
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_NAME
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.downloadPR
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.path
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import com.ringtones.compose.feature.admob.*
import com.skydoves.landscapist.glide.GlideImage
import java.io.File

@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun Details2(viewModel: StickerViewModel) {

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
                border = BorderStroke(1.dp, MaterialTheme.colors.background),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.primary,

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
            if (AdProvider.Banner.ad_status)
                AdvertView()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(backgroundGradient)
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(
                            SHAPE
                        )
                        .background(MaterialTheme.colors.background)
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
                        color = MaterialTheme.colors.primary,
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
                            backgroundColor = White.copy(0.8f),
                            contentColor = MaterialTheme.colors.background
                        )
                    ) {

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
                            bigTextColor = MaterialTheme.colors.background,
                            foregroundIndicatorColor = Green,
                            backgroundIndicatorColor = MaterialTheme.colors.onBackground,
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





