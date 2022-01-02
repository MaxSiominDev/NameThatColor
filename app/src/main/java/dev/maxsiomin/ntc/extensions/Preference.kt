package dev.maxsiomin.ntc.extensions

import androidx.preference.Preference

fun Preference.setOnClickListener(onClick: () -> Unit) {
    onPreferenceClickListener = Preference.OnPreferenceClickListener {
        onClick()
        true
    }
}

fun Preference.addOnPreferenceChangeListener(onClick: (newValue: Any) -> Unit) {
    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue: Any ->
        onClick(newValue)
        true
    }
}

