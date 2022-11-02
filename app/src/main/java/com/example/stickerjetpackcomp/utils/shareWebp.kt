package com.example.stickerjetpackcomp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.stickerjetpackcomp.R
import java.io.ByteArrayOutputStream


class shareWebp {

    companion object {
        fun getImageUri(inImage: Bitmap?, name: String, context: Context): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage!!.compress(Bitmap.CompressFormat.JPEG, 70, bytes)
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                inImage,
                name,
                inImage.toString()
            )
            //Log.d("TAAA",UUID.randomUUID().toString())
            return Uri.parse(path)
        }

        fun saveImageAndShare(gifDrawable: Bitmap, filename: String?, context: Context) {
            var uri: Uri? = getImageUri(gifDrawable as Bitmap?, filename!!, context)
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = "image/*"
            context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
        }

        fun share(context: Context) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "${context.getString(R.string.pre_fix)}${context.packageName}"
            )
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.send_to)
                )
            )
        }
    }


}