package de.rauschdo.photoapp.ui.home

import android.app.Activity
import android.net.Uri
import de.rauschdo.photoapp.architecture.NavigationRequest
import de.rauschdo.photoapp.architecture.ViewEvent
import de.rauschdo.photoapp.architecture.ViewState

class HomeContract {

    sealed class Action : ViewEvent {
        data class OnGalleryClicked(val activity: Activity) : Action()
        data class OnImagePicked(val uri: Uri) : Action()
        data object OnCameraClicked : Action()
        data object ConsumeContract : Action()
    }

    sealed class Navigation : NavigationRequest {
        data object ToCamera : Navigation()
        data object ToImageEditor : Navigation()
    }

    data class UiState(
        val launchContract: Boolean = false
    ) : ViewState
}