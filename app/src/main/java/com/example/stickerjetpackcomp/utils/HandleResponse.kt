package com.example.stickerjetpackcomp.utils

import android.util.Log
import retrofit2.Response

class HandleResponse<T>(private val response: Response<T?>?) {

    fun handleResult(): NetworkResults<T> {
        Log.d("user", response.toString())
        when {
            response == null -> return NetworkResults.Error("No Data Found")
            response.body() == null -> return NetworkResults.Error("No Data Found")
            response.message().toString()
                .contains("timeout") -> return NetworkResults.Error("Timeout")
            response.code() == 402 -> return NetworkResults.Error("Api Key Limited.")
            response.isSuccessful -> {
                Log.d("cats_body", response.body().toString())
                val responseBody = response.body()
                return NetworkResults.Success(responseBody!!)
            }
            else -> {
                Log.d("user", response.message())
                return NetworkResults.Error(response.message())
            }
        }
    }

}

