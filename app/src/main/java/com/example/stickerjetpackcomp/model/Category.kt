package com.example.stickerjetpackcomp.model

import android.graphics.Color
import android.graphics.drawable.Icon
import com.example.stickerjetpackcomp.R

data class Category(
    val id:Int,
    val name:String,
    val icon: Int,
    //val color: Color
)

val categories= listOf(
    Category(id = 0, name = "Funny", icon = R.drawable.funny),
    Category(id = 2,name = "Love", icon = R.drawable.hand),
    Category(id = 3,name = "Birthday", icon = R.drawable.cake),
    Category(id = 1,name = "Flowers", icon = R.drawable.freesia),
    Category(id = 4,name = "Emoji", icon = R.drawable.emoji),
    Category(id = 5,name = "Thank you", icon = R.drawable.thanks),
    Category(id = 6,name = "Thank you", icon = R.drawable.thanks),

)
