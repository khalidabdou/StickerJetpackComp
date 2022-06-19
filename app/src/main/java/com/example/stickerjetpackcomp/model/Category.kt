package com.example.stickerjetpackcomp.model

import android.graphics.Color
import android.graphics.drawable.Icon
import com.example.stickerjetpackcomp.R
import com.google.gson.annotations.SerializedName

data class Category(
    val id:Int,
    val name:String,
    val image: String,
)

data class Categories(
    @SerializedName("categories")
    val results: List<Category>
)


