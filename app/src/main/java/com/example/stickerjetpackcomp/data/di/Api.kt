package com.example.testfriends_jetpackcompose.di

import com.example.stickerjetpackcomp.model.Stickers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Query


interface Api {
    @GET("stickers")
    suspend fun getStickers(
        @Query("language") language: Int = 7
    ): Response<Stickers?>

    @Headers("Content-Type: application/json")
    @PUT("incrementStickerViews")
    suspend fun incrementStickerViews(
        @Query("id") id: Int=7
    )

    @Headers("Content-Type: application/json")
    @PUT("incrementStickerAddTo")
    suspend fun incrementStickerAddTo(
        @Query("id") id: Int
    )

}