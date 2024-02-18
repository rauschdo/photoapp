package de.rauschdo.photoapp.ui.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rauschdo.photoapp.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.photoapp.ui.home.HomeContract
import de.rauschdo.photoapp.ui.navigation.AppNav
import de.rauschdo.photoapp.ui.navigation.AppNavDest
import de.rauschdo.photoapp.ui.navigation.navRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CameraDestination(
    navigator: AppNav,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val state: CameraContract.UiState by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(LAUNCH_EVENTS_FOR_NAVIGATION) {
        viewModel.navigator.onEach { navigationRequest ->
            when (navigationRequest) {
                CameraContract.Navigation.ToImageEditor -> navigator.navigate(AppNavDest.Editor.navRoute())
            }
        }.collect()
    }

    // TODO permission check
    if (false) {
        NoPermissionContent {
            // TODO permission call
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
    val scrollState = rememberScrollState()
    val modifier = Modifier
    val maxWidthModifier = modifier.fillMaxWidth()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

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
