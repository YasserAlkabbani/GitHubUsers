package com.yasser.githubusers.utils.extention

import java.text.SimpleDateFormat
import java.util.*

fun String.dateAsTimeStamp()=
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(this)?.time?:0