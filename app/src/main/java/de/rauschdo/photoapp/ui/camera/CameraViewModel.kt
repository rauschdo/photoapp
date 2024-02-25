package de.rauschdo.photoapp.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.photoapp.architecture.BaseViewModel
import de.rauschdo.photoapp.utility.BitmapController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val bitmapController: BitmapController
) : BaseViewModel<CameraContract.Action, CameraContract.Navigation, CameraContract.UiState>() {

    private val _uiState = MutableStateFlow(CameraContract.UiState())
    override val viewState: StateFlow<CameraContract.UiState>
        get() = _uiState

    override fun handleActions(action: CameraContract.Action) {
        when (action) {
            is CameraContract.Action.OnForwardPermissionResult -> {
                _uiState.update { it.copy(hasPermission = action.isGranted) }
            }

            is CameraContract.Action.OnCapturePhotoClicked -> capturePhoto(
                action.context,
                action.cameraController
            )
        }
    }

    private fun capturePhoto(
        context: Context,
        cameraController: LifecycleCameraController
    ) {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

        cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val correctedBitmap: Bitmap = image
                    .toBitmap()
                    .rotateBitmap(image.imageInfo.rotationDegrees)

                // Hold image in controller and navigate into editor
                bitmapController.captureHolder.value = correctedBitmap
                image.close()

                requestNavigation(CameraContract.Navigation.ToImageEditor)
            }

            override fun onError(exception: ImageCaptureException) {
                Timber.e(exception, "Error capturing image")
            }
        })
    }
}

private fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
