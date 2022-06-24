package com.example.stickerjetpackcomp

import android.app.Application
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication :Application(){
    var config = PRDownloaderConfig.newBuilder()
        .setReadTimeout(30000)
        .setConnectTimeout(30000)
        .build()
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        PRDownloader.initialize(applicationContext, config)
    }

}