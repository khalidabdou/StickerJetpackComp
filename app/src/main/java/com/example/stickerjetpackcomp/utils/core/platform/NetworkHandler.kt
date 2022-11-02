package com.green.china.sticker.core.platform

import android.content.Context
import com.green.china.sticker.core.extensions.others.checkNetworkState

class NetworkHandler
    (private val context: Context) {
    val isConnected get() = context.checkNetworkState()
}