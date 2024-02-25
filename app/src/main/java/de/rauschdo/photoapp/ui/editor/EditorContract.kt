package de.rauschdo.photoapp.ui.editor

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import de.rauschdo.photoapp.architecture.NavigationRequest
import de.rauschdo.photoapp.architecture.ViewEvent
import de.rauschdo.photoapp.architecture.ViewState
import de.rauschdo.photoapp.data.EditType
import de.rauschdo.photoapp.data.FilterType
import de.rauschdo.photoapp.data.Frame

class EditorContract {

    sealed class Action : ViewEvent {
        data object OnBackClicked : Action()
        data class OnEditorItemClicked(val editType: EditType) : Action()
        data class OnFilterClicked(val filterType: FilterType) : Action()
        data class OnFrameClicked(val frame: Frame?) : Action()
        data class OnBrightnessChanged(val brightness: Float) : Action()
        data class OnApplyCaptionClicked(val caption: String) : Action()
        data object OnSaveClicked : Action()
        data class OnBitmapCreated(val bitmap: ImageBitmap) : Action()
    }

    sealed class Navigation : NavigationRequest {
        data object ToHome : Navigation()
    }

    /**
     * @param photo the selected or captured photo before editor was entered
     */
    data class UiState(
        val photo: Bitmap? = null,
        val brightness: Float = 1.0f,
        @DrawableRes val selectedFrame: Int? = null,
        val caption: String? = null,
        val activeEditType: EditType? = null,
        val bitmapCreatorActive: Boolean = false
    ) : ViewState
}