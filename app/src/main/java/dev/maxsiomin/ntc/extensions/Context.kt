package dev.maxsiomin.ntc.extensions

import android.content.Context
import androidx.preference.PreferenceManager
import dev.maxsiomin.ntc.util.SharedPrefs

fun Context.getDefaultSharedPrefs(): SharedPrefs =
    PreferenceManager.getDefaultSharedPreferences(this)
