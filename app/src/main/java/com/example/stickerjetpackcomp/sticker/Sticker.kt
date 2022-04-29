package com.example.stickerjetpackcomp.sticker

import java.io.Serializable

data class Sticker(
    val emojis: List<String>,
    val image_file: String
): Serializable