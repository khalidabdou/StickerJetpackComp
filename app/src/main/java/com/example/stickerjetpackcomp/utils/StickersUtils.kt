package com.example.stickerjetpackcomp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.stickerjetpackcomp.model.MySticker
import com.example.stickerjetpackcomp.sticker.Sticker
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.Config.Companion.BASE_URL
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import java.io.File
import java.io.FileOutputStream
import java.lang.System.out


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
                "https://play.google.com/store/apps/details?id=com.snowcorp.stickerly.android",
                "",
                sticker.count_views,
                sticker.count_set_to_whatsapp,
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




        fun downloadAndShare(url: String, fileName: String,context: Context){
            PRDownloader.download(
                url,
                "${path}/111111/",
                fileName
            ).build().setOnProgressListener {
                // Update the progress
                //    binding.  progressBar.max = it.totalBytes.toInt()
                //    progressBar.progress = it.currentBytes.toInt()
            }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {

                        val file = File(
                            "${path}/111111/",
                            fileName
                        )
                        val bitmapa = BitmapFactory.decodeFile(file.path)
                        bitmapa.compress(Bitmap.CompressFormat.PNG,40, out)
                        val bitmap = Bitmap.createScaledBitmap(bitmapa, 30, 30, true)


                        val uri: Uri = Uri.parse(file.path)

                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_STREAM, uri)
                        context.startActivity(Intent.createChooser(intent, "Share Image"))

                    }
                    override fun onError(error: com.downloader.Error?) {
                        Log.d("TAG","ERR")
                    }
                })
        }
    }

}