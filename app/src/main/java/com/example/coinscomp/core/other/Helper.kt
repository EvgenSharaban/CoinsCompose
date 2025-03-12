package com.example.coinscomp.core.other

import java.util.Locale

fun Double.roundTo(numbersAfterPoint: Int): Double {
    return String.format(Locale.US, "%.${numbersAfterPoint}f", this).toDouble()
}

