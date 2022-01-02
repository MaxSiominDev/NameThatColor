package dev.maxsiomin.ntc.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

@SuppressLint("ClickableViewAccessibility")
fun ImageView.addOnTouchListener(action: (ImageView, MotionEvent) -> Unit) {
    setOnTouchListener { _, event ->
        action(this, event)
        true
    }
}
