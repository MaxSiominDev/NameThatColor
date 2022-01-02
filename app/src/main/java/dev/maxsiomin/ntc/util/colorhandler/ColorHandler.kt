package dev.maxsiomin.ntc.util.colorhandler

import dev.maxsiomin.ntc.extensions.pow
import timber.log.Timber

object ColorHandler {

    private const val IMPOSSIBLE_RANGE = (255 * 255 * 3) + 1

    /**
     * Color sample: 556A74
     */
    fun getColorName(color: String): String {
        Timber.d("color=$color")

        if (color in names.keys)
            return names[color]!!

        val r = color.slice(0..1).toInt(16)
        val g = color.slice(2..3).toInt(16)
        val b = color.slice(4..5).toInt(16)

        var currentMin: Pair<Int, String>? = null
        var range: Int

        names.forEach { (colorValue, name) ->
            range = (
                (r - colorValue.slice(0..1).toInt(16)).pow() +
                (g - colorValue.slice(2..3).toInt(16)).pow() +
                (b - colorValue.slice(4..5).toInt(16)).pow()
            )

            if (currentMin?.first ?: IMPOSSIBLE_RANGE > range)
                currentMin = range to name
        }

        return currentMin!!.second
    }
}
