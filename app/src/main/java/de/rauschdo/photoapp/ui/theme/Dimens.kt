package de.rauschdo.photoapp.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val zero = 0.dp

val textsize_14 = 14.sp
val textsize_16 = 16.sp
val textsize_18 = 18.sp
val textsize_20 = 20.sp
val textsize_24 = 24.sp

fun heigherOf(heigh1: Dp?, heigh2: Dp?, fallbackHeight: Dp): Dp {
    heigh1?.let { heightNegative ->
        heigh2?.let { heightPositive ->
            return maxOf(heightNegative.value, heightPositive.value).dp
        }
    }
    return fallbackHeight
}