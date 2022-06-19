package com.example.stickerjetpackcomp.data

import com.example.testfriends_jetpackcompose.di.Api
import javax.inject.Inject

class Remote @Inject constructor(
    private val api: Api
) {

    suspend fun getStickers() = api.getStickers()

    suspend fun getCategories()=api.getCategories()

    suspend fun incrementStickerViews(id: Int) {
        api.incrementStickerViews(id)
    }
    suspend fun incrementStickerAddTo(id: Int) {
        api.incrementStickerAddTo(id)
    }
}