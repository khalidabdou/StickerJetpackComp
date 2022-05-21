package com.example.stickerjetpackcomp.model

import android.graphics.Color
import android.graphics.drawable.Icon
import com.example.stickerjetpackcomp.R

data class Category(
    //var id:Int,
    val name:String,
    val icon: Int,
    //val color: Color
)

val categories= listOf(
    Category(name = "Funny", icon = R.drawable.funny),
    Category(name = "Love", icon = R.drawable.hand),
    Category(name = "Birthday", icon = R.drawable.cake),
    Category(name = "Flowers", icon = R.drawable.freesia),
    Category(name = "Emoji", icon = R.drawable.emoji),
    Category(name = "Thank you", icon = R.drawable.thanks),
    Category(name = "Thank you", icon = R.drawable.thanks),

)
