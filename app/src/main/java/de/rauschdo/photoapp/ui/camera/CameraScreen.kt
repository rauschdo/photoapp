package de.rauschdo.photoapp.ui.camera

import android.Manifest
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rauschdo.photoapp.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.photoapp.ui.navigation.AppNav
import de.rauschdo.photoapp.ui.navigation.AppNavDest
import de.rauschdo.photoapp.ui.navigation.navRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

private const val permission = Manifest.permission.CAMERA

@Composable
fun CameraDestination(
    navigator: AppNav,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val state: CameraContract.UiState by viewModel.viewState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.setAction(CameraContract.Action.OnForwardPermissionResult(isGranted))
    }
    LaunchedEffect(LAUNCH_EVENTS_FOR_NAVIGATION) {
        viewModel.navigator.onEach { navigationRequest ->
            when (navigationRequest) {
                CameraContract.Navigation.ToImageEditor -> navigator.navigate(AppNavDest.Editor.navRoute())
            }
        }.collect()
    }

    LifecycleResumeEffect(viewModel) {
        launcher.launch(permission)
        onPauseOrDispose {
            // nothing
        }
    }

    if (!state.hasPermission) {
        NoPermissionContent {
            launcher.launch(permission)
        }
    } else {
        CameraScreen(
            state = state,
            forwardAction = { action -> viewModel.setAction(action) }
        )
    }
}

@Composable
private fun CameraScreen(
    state: CameraContract.UiState,
    forwardAction: (CameraContract.Action) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setBackgroundColor(Color.BLACK)
                    implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also { previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )
        Button(onClick = {
            forwardAction(
                CameraContract.Action.OnCapturePhotoClicked(
                    context,
                    cameraController
                )
            )
        }) {
            Text(text = "Take Photo")
        }
    }
}

@Composable
private fun NoPermissionContent(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Please grant the permission to use the camera to use the core functionality of this app.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Icon(imageVector = Icons.Filled.Camera, contentDescription = "Camera")
            Text(text = "Grant permission")
        }
    }
}

@Composable
private fun ScreenPreview() {
    CameraScreen(
        state = CameraContract.UiState(),
        forwardAction = {}
    )
}
