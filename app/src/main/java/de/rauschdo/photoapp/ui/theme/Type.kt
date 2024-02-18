package de.rauschdo.photoapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import de.rauschdo.photoapp.R

// use defaults for now
val TypeDefinition = Typography(
//    displayLarge = defineTextStyle(FontWeight.Bold, textsize_24),
//    displayMedium = defineTextStyle(FontWeight.Bold, textsize_20),
//    displaySmall = defineTextStyle(FontWeight.Normal, textsize_14),
//    headlineLarge = defineTextStyle(FontWeight.Bold, textsize_24),
//    headlineMedium = defineTextStyle(FontWeight.Bold, textsize_20),
//    headlineSmall = defineTextStyle(FontWeight.Bold, textsize_18),
//    titleLarge = defineTextStyle(FontWeight.Bold, textsize_20),
//    titleMedium = defineTextStyle(FontWeight.Bold, textsize_18),
//    titleSmall = defineTextStyle(FontWeight.Bold, textsize_18),
//    bodyLarge = defineTextStyle(FontWeight.Normal, textsize_18),
//    bodyMedium = defineTextStyle(FontWeight.Normal, textsize_16),
//    bodySmall = defineTextStyle(FontWeight.Normal, textsize_14),
//    labelLarge = defineTextStyle(FontWeight.Normal, textsize_18),
//    labelMedium = defineTextStyle(FontWeight.Normal, textsize_16),
//    labelSmall = defineTextStyle(FontWeight.Normal, textsize_14),
)

private fun defineTextStyle(
    fontFamily: FontFamily = FontFamily.Default,
    weight: FontWeight,
    textSize: TextUnit,
    color: Color = Color.Unspecified,
    textDirection: TextDecoration? = null,
) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = weight,
    fontSize = textSize,
    color = color,
    textDecoration = textDirection
)