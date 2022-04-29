package com.green.china.sticker.core.extensions.sharedprefs

import android.content.Context

fun Context.initSharedPrefs() = SharedPrefsHelpers.init(this)