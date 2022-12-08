package com.example.stickerjetpackcomp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.App
import com.example.stickerjetpackcomp.utils.AppUtil
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun AdBannerApp(app: App?) {
    val context = LocalContext.current
    if (app != null)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,


            ) {

            Text(
                text = "Ad",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            GlideImage(
                imageModel = "${app.image}",
                contentScale = ContentScale.Crop,
                placeHolder = ImageBitmap.imageResource(R.mipmap.ic_launcher_foreground),
                error = ImageBitmap.imageResource(com.example.stickerjetpackcomp.R.mipmap.ic_launcher_foreground),
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = app.title,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        onClick = {
                            AppUtil.openUrl(context, app.url)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.install),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

            }

        }
}