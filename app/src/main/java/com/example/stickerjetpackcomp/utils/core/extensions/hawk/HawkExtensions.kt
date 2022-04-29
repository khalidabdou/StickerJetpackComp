package com.green.china.sticker.core.extensions.hawk

import android.content.Context
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk


fun Context.initHawk() = Hawk.init(this).build()