package com.example.stickerjetpackcomp.model

import com.google.gson.annotations.SerializedName

data class Languages(
    val id: Int,
    val name: String,
    val label: String,
    val language_code: String,
    val filename: String,
)

data class ListLanguages(
    @SerializedName("languages")
    val result: List<Languages>
)
