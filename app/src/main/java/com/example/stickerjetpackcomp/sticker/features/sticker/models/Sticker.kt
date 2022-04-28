package com.green.china.sticker.features.sticker.models

import java.io.Serializable

data class Sticker(
    val emojis: List<String>,
    val image_file: String
): Serializable