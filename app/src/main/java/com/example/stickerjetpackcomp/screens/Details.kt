package com.example.stickerjetpackcomp.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.downloader.PRDownloader.download
import com.example.stickerjetpackcomp.BuildConfig
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.ui.theme.darkGray2
import com.example.stickerjetpackcomp.utils.StickersUtils
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.ADD_PACK
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_AUTHORITY
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_ID
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.EXTRA_STICKER_PACK_NAME
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.downloadPR
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.path
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import java.io.File

@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun Details(viewModel: StickerViewModel) {

    var process by mutableStateOf(10)
    var favIcon by mutableStateOf(R.drawable.favorite)

    val pack = viewModel.detailsPack.value

    val state = rememberLazyListState()

    val context= LocalContext.current

    path = context.filesDir.toString() + "/stickers_asset"
    val myDir = File("${path}/")
    myDir.mkdirs()
    if (myDir.exists())
        myDir.delete()

    Hawk.init(context).build();


    var resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                Log.w("TAG",data.toString())
                //doSomeOperations()
            }else Log.w("TAG",result.toString())
        }

    fun openWhatsappActivityForResult() {
        //val intent = Intent(this, SomeActivity::class.java)
        val intent = Intent()
        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        intent.putExtra(EXTRA_STICKER_PACK_ID, pack!!.identifier)
        intent.putExtra(
            EXTRA_STICKER_PACK_AUTHORITY,
            BuildConfig.CONTENT_PROVIDER_AUTHORITY
        )
        intent.putExtra(EXTRA_STICKER_PACK_NAME, pack.name)
        //resultLauncher.launch(intent)
    }
    if (viewModel.isReady.value){
        Log.d("TAG","ready")
        openWhatsappActivityForResult()
    }

    Scaffold(topBar = {
        AppBar(
            background = darkGray2,
            icon = favIcon,
            onClick = {
                favIcon = R.drawable.ic_favorite
            })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkGray)
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
                        .background(darkGray)
                        .padding(10.dp)
                ) {
                    CustomComponent(
                        indicatorValue = viewModel.progress.value,
                        maxIndicatorValue = pack!!.stickers.size,
                        bigTextColor = backgroundWhite,
                        foregroundIndicatorColor = Green,
                        backgroundIndicatorColor = White,
                        backgroundIndicatorStrokeWidth = 15f,
                        foregroundIndicatorStrokeWidth = 15f
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(
                        text = "Sticker pack ",
                        color = backgroundWhite,
                        style = MaterialTheme.typography.h1
                    )
                    LabelLikes(icon = R.drawable.eye, text = pack!!.views.toString())
                    LabelLikes(
                        icon = R.drawable.ic_dwonloaded,
                        text = pack!!.addToWatsapp.toString()
                    )
                }
                Column() {
                    Button(
                        onClick = {
                            viewModel.download()
                            val trayImageFile = getLastBitFromUrl(pack!!.tray_image_file)
                            downloadPR(pack!!.tray_image_file, trayImageFile, pack)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green.copy(0.8f),
                            contentColor = darkGray
                        )
                    ) {
                        Text(text = "Add")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.whatsapp),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            GridStickers(state = state, pack = pack!!)
        }
    }
}

@ExperimentalAnimationApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridStickers(pack: StickerPack, state: LazyListState) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        state = state
    ) {
        items(pack.stickers.size) { index ->
            Card(
                modifier = Modifier
                    .size(90.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(10.dp)),
                backgroundColor = Color.White,
            ) {
                AsyncImage(
                    model = "${pack.stickers[index].image_file}",
                    contentDescription = "",
                    modifier = Modifier.size(70.dp)
                )
            }
        }
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

