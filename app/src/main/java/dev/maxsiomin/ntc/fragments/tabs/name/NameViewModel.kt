package dev.maxsiomin.ntc.fragments.tabs.name

import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.ntc.fragments.BaseViewModel
import dev.maxsiomin.ntc.fragments.intMutableLiveData
import dev.maxsiomin.ntc.fragments.stringMutableLiveData
import dev.maxsiomin.ntc.util.UiActions
import dev.maxsiomin.ntc.util.colorhandler.ColorHandler.getColorName
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    /**
     * [View.GONE] or [View.VISIBLE]
     */
    val infoVisibilityLiveData = intMutableLiveData(View.VISIBLE)

    val hexLiveData = stringMutableLiveData()
    val rgbLiveData = stringMutableLiveData()
    val colorNameLiveData = stringMutableLiveData()

    fun onImageSet() {
        if (infoVisibilityLiveData.value == View.VISIBLE)
            infoVisibilityLiveData.value = View.GONE
    }

    fun processCoords(@ColorInt pixel: Int) {
        val rgbRed = pixel.red
        val rgbGreen = pixel.green
        val rgbBlue = pixel.blue
        updateRgbLiveData(rgbBlue, rgbGreen, rgbBlue)

        var hexRed = rgbRed.toString(16)
        if (hexRed.length == 1)
            hexRed = "0$hexRed"

        var hexGreen = rgbGreen.toString(16)
        if (hexGreen.length == 1)
            hexGreen = "0$hexGreen"

        var hexBlue = rgbBlue.toString(16)
        if (hexBlue.length == 1)
            hexBlue = "0$hexBlue"

        updateHexLiveData(hexRed, hexGreen, hexBlue)

        val colorName = getColorName("$hexRed$hexGreen$hexBlue")
        Timber.i("colorName=$colorName")
        updateColorNameLiveData(colorName)
    }

    private fun updateRgbLiveData(r: Int, g: Int, b: Int) {
        // Example: 255, 255, 255
        rgbLiveData.value = "$r, $g, $b"
    }

    // Example: #FFFFFF
    private fun updateHexLiveData(r: String, g: String, b: String) {
        hexLiveData.value = "#$r$g$b".uppercase(Locale.getDefault())
    }

    // Example: White
    private fun updateColorNameLiveData(colorName: String) {
        colorNameLiveData.value = colorName
    }
}
