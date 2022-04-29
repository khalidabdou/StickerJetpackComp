package com.green.china.sticker.core.extensions.dateAndTime

import java.util.*

fun isFetchSixHours(lastFetchTime: Long): Boolean = Date().time - lastFetchTime >= 6*60*60*1000
