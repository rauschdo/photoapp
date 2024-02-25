package de.rauschdo.photoapp.ui.camera

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import de.rauschdo.photoapp.architecture.NavigationRequest
import de.rauschdo.photoapp.architecture.ViewEvent
import de.rauschdo.photoapp.architecture.ViewState

// TODO dialog to input filename
class CameraContract {

    sealed class Action : ViewEvent {
        data class OnForwardPermissionResult(val isGranted: Boolean) : Action()
        data class OnCapturePhotoClicked(
            val context: Context,
            val cameraController: LifecycleCameraController
        ) : Action()
    }

    sealed class Navigation : NavigationRequest {
        data object ToImageEditor : Navigation()
    }

    data class UiState(
        val hasPermission: Boolean = false
    ) : ViewState
}