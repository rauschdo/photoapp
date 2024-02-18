package de.rauschdo.photoapp.ui.editor

import de.rauschdo.photoapp.architecture.NavigationRequest
import de.rauschdo.photoapp.architecture.ViewEvent
import de.rauschdo.photoapp.architecture.ViewState

class EditorContract {

    sealed class Action : ViewEvent {
        data object OnSaveClicked : Action()
    }

    sealed class Navigation : NavigationRequest {
        data object ToHome : Navigation()
    }

    data class UiState(
        val param: Any = ""
    ) : ViewState
}