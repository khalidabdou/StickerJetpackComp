package com.example.stickerjetpackcomp.model

import com.google.gson.annotations.SerializedName

data class App(
    val id: Int,
    val title: String,
    val desc: String,
    val url: String,
    val image: String,
)


