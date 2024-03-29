package com.example.stickerjetpackcomp.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.Languages

import com.example.stickerjetpackcomp.utils.AppTheme.Companion.HOME
import com.example.stickerjetpackcomp.utils.NetworkResults
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import kotlinx.coroutines.delay


@Composable
fun Splash(navController: NavHostController, viewModel: StickerViewModel) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }
    val message = viewModel.message.collectAsState()
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

//    LaunchedEffect(key1 = true) {
//        viewModel.setMessage(context)
//        startAnimation = true
//        delay(2000)
//        navController.popBackStack()
//        navController.navigate(HOME.route)
//        Toast.makeText(context,"${viewModel.adsList.value}",Toast.LENGTH_LONG).show()
//
//    }


    if (viewModel.infos.value is NetworkResults.Loading) {
        viewModel.getAds()
    } else if (viewModel.infos.value is NetworkResults.Error) {
        LaunchedEffect(key1 = true) {
            viewModel.setMessage(context)
            startAnimation = true
            delay(2000)
            navController.popBackStack()
            navController.navigate(HOME.route)
            //Toast.makeText(context, "${viewModel.adsList.value}", Toast.LENGTH_LONG).show()
        }
    } else {
        LaunchedEffect(key1 = true) {
            viewModel.setMessage(context)
            startAnimation = true
            delay(2000)
            navController.popBackStack()
            navController.navigate(HOME.route)
            //Toast.makeText(context, "${viewModel.adsList.value}", Toast.LENGTH_LONG).show()

        }
    }

    Splash(alpha = alphaAnim.value, message.value)
}

@Composable
fun Splash(alpha: Float, message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            modifier = Modifier
                .size(120.dp),
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = "Logo Icon",
            //tint = Color.DarkGray
        )
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(fontSize = 19.sp)
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onBackground,
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(4f)
            )
        }
    }

}

