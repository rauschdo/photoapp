package de.rauschdo.photoapp.ui.camera

import de.rauschdo.photoapp.architecture.NavigationRequest
import de.rauschdo.photoapp.architecture.ViewEvent
import de.rauschdo.photoapp.architecture.ViewState

// TODO dialog to input filename
class CameraContract {

    sealed class Action : ViewEvent {
        //
    }

    sealed class Navigation : NavigationRequest {
        data object ToImageEditor : Navigation()
    }

    data class UiState(
        val param: Any = ""
    ) : ViewState
}