package com.example.stickerjetpackcomp.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.data.Local
import com.example.stickerjetpackcomp.model.Languages
import com.example.stickerjetpackcomp.ui.theme.Purple700
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import kotlinx.coroutines.delay


@Composable
fun Splash(navController: NavHostController, viewModel: StickerViewModel) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }
    val local = Local(context)
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        navController.popBackStack()
        navController.navigate(Screen.Home.route)
    }
    //  Splash(alpha = alphaAnim.value)

    LaunchedEffect(0) {
        //if (viewModel.languages.value.isNullOrEmpty())
        // viewModel.getLanguages(context)
    }
    /* if (!viewModel.languages.value.isNullOrEmpty()) {
         val lg = local.getLanguage.collectAsState(initial = 0)
         if (lg.value != 0) {
             LaunchedEffect(key1 = true) {
                 LANGUAGE=lg.value!!
                 Log.d("LANG",lg.value.toString())
                 navController.popBackStack()
                 navController.navigate(Screen.Home.route)
             }
         } else
             Languages(viewModel.languages.value!!) {
                 viewModel.saveLanguage(it)
                 Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
             }
     } else*/
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) backgroundWhite else Purple700)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(120.dp),
            painter = painterResource(id = R.drawable.sticker),
            contentDescription = "Logo Icon",
            tint = Color.DarkGray
        )
    }
}

@Composable
fun Languages(languages: List<Languages>, onClick: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.padding(4.dp)) {
        item {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(8.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.languages), contentDescription = "")
            }
        }
        items(languages.size) {
            ItemLanguage(languages[it]) {
                onClick(it)
            }

        }
    }
}

@Composable
fun ItemLanguage(languages: Languages, onClick: (Int) -> Unit) {
    Box(modifier = Modifier.padding(4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .alpha(0.5f)
                .padding(4.dp)
                .clickable {
                    onClick(languages.id)
                }
        ) {

            Text(
                text = languages.name,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(4f)
            )
        }
    }

}

@Composable
@Preview
fun SplashScreenPreview() {
    Splash(alpha = 1f)
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun SplashScreenDarkPreview() {
    Splash(alpha = 1f)
}

@Preview
@Composable
fun PreviewLanguage() {
    //Languages()
}