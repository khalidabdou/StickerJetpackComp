package com.example.stickerjetpackcomp.utils

import com.example.stickerjetpackcomp.BuildConfig

class Config {
    companion object {
        val BASE_URL: String = BuildConfig.API
        var SETTING = "pack"
        var PACKAGE: String = "com.catsmemes.stickers"
    }
}