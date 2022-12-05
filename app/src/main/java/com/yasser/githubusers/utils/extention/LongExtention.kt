package com.yasser.githubusers.utils.extention

import java.text.SimpleDateFormat
import java.util.*

fun Long.asDate()= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(this)

fun Long.asTimeZoneDate()= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(this)