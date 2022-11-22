package com.example.stickerjetpackcomp.model

import com.google.gson.annotations.SerializedName

data class Ad(
    val id: Int,
    var ad_id: String,
    val type: String,
    var ad_status: Boolean,
    var show_count: Int?,

    )

data class Setting(
    @SerializedName("package")
    val package_name : String,
    val dynamic_link:String,
    val email :String
)


data class Ads(
    @SerializedName(value = "admobe", alternate = ["ads"])
    val ads: List<Ad>,
)