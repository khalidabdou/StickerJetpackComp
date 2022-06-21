package com.example.testfriends_jetpackcompose.di

import com.example.stickerjetpackcomp.model.Categories
import com.example.stickerjetpackcomp.model.Category
import com.example.stickerjetpackcomp.model.Stickers
import retrofit2.Response
import retrofit2.http.*


interface Api {
    @GET("stickers")
    suspend fun getStickers(
        @Query("language") language: Int = 1
    ): Response<Stickers?>

    @GET("categories")
    suspend fun getCategories():Response<Categories?>


    @POST("incrementViews")
    suspend fun incrementStickerViews(
        @Query("sticker") id: Int=7
    )


    @POST("incrementAdd")
    suspend fun incrementStickerAddTo(
        @Query("sticker") id: Int
    )

}