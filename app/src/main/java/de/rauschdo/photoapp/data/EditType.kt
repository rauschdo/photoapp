package de.rauschdo.photoapp.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterFrames
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.ui.graphics.vector.ImageVector

enum class EditType(val icon: ImageVector) {
    FILTER(Icons.Default.FilterAlt),
    FRAME(Icons.Default.FilterFrames),
    BRIGHTNESS(Icons.Default.SettingsBrightness),
    CAPTION(Icons.Default.EditNote)
}