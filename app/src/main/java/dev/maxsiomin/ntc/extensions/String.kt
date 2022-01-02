package dev.maxsiomin.ntc.extensions

/**
 * If string == null, returns ""
 */
fun String?.notNull(): String = this ?: ""
