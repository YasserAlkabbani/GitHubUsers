package com.yasser.githubusers.utils.extention

import java.text.SimpleDateFormat
import java.util.*

fun Long.asDate()= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(this)