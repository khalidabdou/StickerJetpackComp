package com.example.stickerjetpackcomp.model

import com.google.gson.annotations.SerializedName

data class MySticker(
    val id: Int?,
    val identifier: Int,
    val name: String,
    val animated_sticker_pack: Boolean,
    val android_play_store_link: String,
    val stickers: String,
    val folder: String,
    @SerializedName("language_app")
    val language_app: String,
    val count_views: Int,
    val count_set_to_whatsapp: Int,
)

data class Stickers(
    @SerializedName("stickers")
    val results: List<MySticker>
)





