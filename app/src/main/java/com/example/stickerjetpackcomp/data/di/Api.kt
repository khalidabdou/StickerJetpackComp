package com.example.testfriends_jetpackcompose.di

import com.example.stickerjetpackcomp.model.*
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.LANGUAGE
import org.intellij.lang.annotations.Language
import retrofit2.Response
import retrofit2.http.*


interface Api {
    @GET("stickers")
    suspend fun getStickers(
        @Query("language") language: Int = LANGUAGE
    ): Response<Stickers?>

    @GET("categoriesByLanguage")
    suspend fun getCategories(@Query("language") language: Int):Response<Categories?>

    @GET("languages")
    suspend fun getLanguages():Response<ListLanguages?>


    @POST("incrementViews")
    suspend fun incrementStickerViews(
        @Query("sticker") id: Int=7
    )


    @POST("incrementAdd")
    suspend fun incrementStickerAddTo(
        @Query("sticker") id: Int
    )

}