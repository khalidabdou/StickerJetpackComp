package com.example.stickerjetpackcomp.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Int,
    val name: String,
    val image: String,
    @SerializedName("pack_stickers")
    val pack_stickers: List<MySticker>
)

data class Categories(
    @SerializedName("categories")
    val results: List<Category>
)


