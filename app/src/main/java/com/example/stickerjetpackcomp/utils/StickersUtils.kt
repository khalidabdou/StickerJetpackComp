package com.example.stickerjetpackcomp.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.stickerjetpackcomp.BuildConfig
import com.example.stickerjetpackcomp.model.MySticker
import com.example.stickerjetpackcomp.sticker.Sticker
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.Config.Companion.BASE_URL
import com.example.stickerjetpackcomp.utils.Config.Companion.SETTING
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import java.io.File


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
                BASE_URL + "/stickers/" + sticker.folder + "/im.png",
                "https://play.google.com/store/apps/details?id=${SETTING}",
                "",
                sticker.count_views,
                sticker.count_set_to_whatsapp,

                )
            return stickerPack

        }

        fun convertStringtoSticker(stickerString: String, packname: String): ArrayList<Sticker> {
            val stk = stickerString.split(",").toTypedArray()
            var stickerPksArray: ArrayList<Sticker> = ArrayList()


            stk.forEach { element ->
                var urlSticker = BASE_URL + "stickers/" + packname + "/" + element
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
            ).build().setOnProgressListener {
                // Update the progress
                //    binding.  progressBar.max = it.totalBytes.toInt()
                //    progressBar.progress = it.currentBytes.toInt()
            }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        // Update the progress bar to show the completeness
//                    progressBar.max = 100
//                    progressBar.progress = 100
//
//                    // Read the file
//                    readFile(fileName)

                        Log.d("TGG", url + " ---//-- " + fileName)
                        val file = File(
                            "${path}/${pack.identifier}/",
                            fileName
                        )

                    }

                    override fun onError(error: com.downloader.Error?) {
                       Log.d("TAG","ERR")
                    }


                })
        }
    }

}