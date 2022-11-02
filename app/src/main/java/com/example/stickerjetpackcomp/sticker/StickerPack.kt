package com.example.stickerjetpackcomp.sticker


data class StickerPack(
    val animated_sticker_pack: Boolean,
    val avoid_cache: Boolean,
    val identifier: String,
    val image_data_version: String,
    val license_agreement_website: String,
    val name: String,
    val privacy_policy_website: String,
    val publisher: String,
    val publisher_email: String,
    val publisher_website: String,
    val stickers: ArrayList<Sticker>,
    val tray_image_file: String,
    val android_play_store_link: String,
    val ios_app_store_link: String,
    val views: String,
    val count_set_to_whatsapp: String,
    val catId: Int
)