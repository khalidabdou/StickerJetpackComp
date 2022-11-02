package com.example.stickerjetpackcomp.utils

import com.example.stickerjetpackcomp.BuildConfig

class Config {
    companion object {
        val BASE_URL: String = BuildConfig.API
        var PACKAGE: String = BuildConfig.PACKAGE_NAME
        val ENABLE_ADS = false
    }
}