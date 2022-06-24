package com.example.stickerjetpackcomp.data

import com.example.stickerjetpackcomp.model.Languages
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.LANGUAGE
import com.example.testfriends_jetpackcompose.di.Api
import javax.inject.Inject

class Remote @Inject constructor(
    private val api: Api
) {
    suspend fun getStickers() = api.getStickers()
    suspend fun getCategories()=api.getCategories(LANGUAGE)
    suspend fun getLanguages()=api.getLanguages()
    suspend fun incrementStickerViews(id: Int) {
        api.incrementStickerViews(id)
    }
    suspend fun incrementStickerAddTo(id: Int) {
        api.incrementStickerAddTo(id)
    }
}