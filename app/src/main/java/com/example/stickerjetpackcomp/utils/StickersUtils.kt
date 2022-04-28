package com.example.stickerjetpackcomp.utils

import android.util.Log
import com.example.stickerjetpackcomp.model.MySticker
import com.example.stickerjetpackcomp.utils.Config.Companion.BASE_URL
import com.example.stickerjetpackcomp.utils.Config.Companion.SETTING
import com.green.china.sticker.features.sticker.models.Sticker
import com.green.china.sticker.features.sticker.models.StickerPack



class StickersUtils {

    companion object{
        fun convertStickerToPack(sticker: MySticker):StickerPack {
            val stickers=convertStringtoSticker(sticker.stickers,sticker.folder)
            val stickerPack= StickerPack(
                sticker.animated_sticker_pack ,true,
                "${sticker.identifier}",
                "",
                "",
                sticker.name,
                "",
                "SpecialOnes",
                "SpecialOnes@support.com","SpecialOnes",
                stickers,
                BASE_URL+"/stickers/"+sticker.folder+"/im.png",
                "https://play.google.com/store/apps/details?id=${SETTING}",
                "",
                sticker.count_views,
                sticker.count_set_to_whatsapp,

                )
            return  stickerPack

        }

        fun convertStringtoSticker(stickerString: String,packname :String):ArrayList<Sticker>{
            val stk = stickerString.split(",").toTypedArray()
            var stickerPksArray : ArrayList<Sticker> = ArrayList()


            stk.forEach { element->
                var urlSticker = BASE_URL+"stickers/"+packname+"/"+element
                var stickerPk=Sticker( arrayOf("","","").asList(), urlSticker)
                stickerPksArray.add(stickerPk)
                //Log.d("TAAAAG",urlSticker)
            }
            Log.d("TAAAAG",stickerPksArray.toString())
            return stickerPksArray

        }
    }

}