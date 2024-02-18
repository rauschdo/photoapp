package de.rauschdo.photoapp.ui.home

import android.app.Activity
import de.rauschdo.photoapp.architecture.NavigationRequest
import de.rauschdo.photoapp.architecture.ViewEvent
import de.rauschdo.photoapp.architecture.ViewState

class HomeContract {

    sealed class Action : ViewEvent {
        class OnGalleryClicked(val activity: Activity) : Action()
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