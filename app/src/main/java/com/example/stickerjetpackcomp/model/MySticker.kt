package com.example.stickerjetpackcomp.model

data class MySticker(
    val id: Int?,
    val identifier: Int,
    val cid: Int,
    val name: String,
    val animated_sticker_pack: Boolean,
    val android_play_store_link: String,
    val stickers: String,
    val folder: String,
    val count_views: String,
    val count_set_to_whatsapp: String,
)

data class Stickers(
    val pack_stickers: List<MySticker>
)





