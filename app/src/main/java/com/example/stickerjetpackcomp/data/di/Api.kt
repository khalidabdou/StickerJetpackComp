package com.example.testfriends_jetpackcompose.di

import com.example.stickerjetpackcomp.model.Categories
import retrofit2.Response
import retrofit2.http.*


interface Api {
    @GET("categories/{package}")
    suspend fun getStickers(
        @Path("package") packageName: String
    ): Response<Categories?>

    @GET("categories")
    suspend fun getCategories(): Response<Categories?>

    @Headers("Content-Type: application/json")
    @PUT("incrementViews")
    suspend fun incrementStickerViews(
        @Query("id") id: Int
    )

    @Headers("Content-Type: application/json")
    @PUT("incrementAdd")
    suspend fun incrementStickerAddTo(
        @Query("id") id: Int
    )

}