package com.example.stickerjetpackcomp.model

import android.graphics.Color
import android.graphics.drawable.Icon
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.google.gson.annotations.SerializedName

data class Category(
    val id:Int,
    val name:String,
    val image: String,
    @SerializedName("pack_stickers")
    val pack: List<MySticker>,
)

data class Categories(
    @SerializedName("categories")
    val results: List<Category>
)


