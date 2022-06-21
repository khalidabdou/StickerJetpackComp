package com.example.stickerjetpackcomp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.stickerjetpackcomp.model.MySticker
import com.example.stickerjetpackcomp.sticker.Sticker
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.Config.Companion.BASE_URL
import java.io.File
import java.io.FileOutputStream
import java.lang.System.out
import kotlin.random.Random


class StickersUtils {

    companion object {
        const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
        const val EXTRA_STICKERPACK = "stickerpack"

        const val ADD_PACK = 200

        @JvmField
        var path: String? = null

        fun convertStickerToPack(sticker: MySticker): StickerPack {
            val stickers = convertStringtoSticker(sticker.stickers, sticker.folder)
            val stickerPack = StickerPack(
                sticker.animated_sticker_pack, true,
                "${sticker.identifier}",
                "",
                "",
                sticker.name,
                "",
                "SpecialOnes",
                "SpecialOnes@support.com", "SpecialOnes",
                stickers,
                BASE_URL + "/packs/oybq3/tray.png",
                sticker.android_play_store_link,
                "",
                Random.nextInt(100,900).toString(),
                Random.nextInt(100,900).toString(),
                catId = sticker.cid
                )
            return stickerPack

        }

        fun convertStringtoSticker(stickerString: String, packname: String): ArrayList<Sticker> {
            val stk = stickerString.split(",").toTypedArray()
            var stickerPksArray: ArrayList<Sticker> = ArrayList()


            stk.forEach { element ->
                var urlSticker = BASE_URL + "packs/" + packname + "/" + element
                var stickerPk = Sticker(arrayOf("", "", "").asList(), urlSticker)
                stickerPksArray.add(stickerPk)
                //Log.d("TAAAAG",urlSticker)
            }
            Log.d("TAAAAG", stickerPksArray.toString())
            return stickerPksArray

        }


        fun  downloadPR(url: String, fileName: String, pack: StickerPack) {
            PRDownloader.download(
                url,
                "${path}/${pack.identifier}/",
                fileName
            ).build().setOnProgressListener {}
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        Log.d("TGG", url + " ---//-- " + fileName)
                        val file = File(
                            "${path}/${pack.identifier}/",
                            fileName
                        )
                        val bitmapa = BitmapFactory.decodeFile(file.path)
                        bitmapa.compress(Bitmap.CompressFormat.PNG,40, out)
                        val b = BitmapFactory.decodeFile(file.path)
                        var bitmap = Bitmap.createScaledBitmap(bitmapa, 30, 30, false)
                        try {
                           val fOut = FileOutputStream(file)
                            bitmap.compress(Bitmap.CompressFormat.PNG, 10, fOut)
                            fOut.flush()
                            fOut.close()
                            b.recycle()
                            bitmap.recycle()
                        } catch (e: Exception) {
                        }
                        var lengthbmp = file.length() /1024
                        //Log.d("TAG",lengthbmp.toString())
                        //Log.d("hawk",Hawk.get<Any>("sticker_packs").toString())
                    }
                    override fun onError(error: com.downloader.Error?) {
                       Log.d("TAG","ERR")
                    }
                })
        }


        @RequiresApi(Build.VERSION_CODES.M)
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }
    }

}