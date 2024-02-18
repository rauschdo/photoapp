package de.rauschdo.photoapp.ui.home

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rauschdo.photoapp.R
import de.rauschdo.photoapp.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.photoapp.ui.navigation.AppNav
import de.rauschdo.photoapp.ui.navigation.AppNavDest
import de.rauschdo.photoapp.ui.navigation.navRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Composable
fun HomeDestination(
    navigator: AppNav,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state: HomeContract.UiState by viewModel.viewState.collectAsStateWithLifecycle()
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            // TODO post in bitmap controller and navigate
        } ?: Timber.i("No Media selected.")
    }

    LaunchedEffect(LAUNCH_EVENTS_FOR_NAVIGATION) {
        viewModel.navigator.onEach { navigationRequest ->
            when (navigationRequest) {
                HomeContract.Navigation.ToCamera -> navigator.navigate(AppNavDest.Camera.navRoute())
                HomeContract.Navigation.ToImageEditor -> navigator.navigate(AppNavDest.Editor.navRoute())
            }
        }.collect()
    }

    if (state.launchContract) {
        viewModel.setAction(HomeContract.Action.ConsumeContract)
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    HomeScreen(
        state = state,
        forwardAction = { action -> viewModel.setAction(action) }
    )
}

@Composable
private fun HomeScreen(
    state: HomeContract.UiState,
    forwardAction: (HomeContract.Action) -> Unit
) {
    val activity = LocalContext.current as Activity

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .size(width = 300.dp, 250.dp)
                .align(Alignment.TopCenter),
            painter = painterResource(id = R.drawable.bob_ross),
            contentDescription = "Iconic image of Bob Ross",
            contentScale = ContentScale.Fit
        )
        Button(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 50.dp, bottom = 96.dp),
            onClick = { forwardAction(HomeContract.Action.OnGalleryClicked(activity)) }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(imageVector = Icons.Default.Image, contentDescription = null)
                Text(text = "Gallery")
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp, bottom = 96.dp),
            onClick = { forwardAction(HomeContract.Action.OnCameraClicked) }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(imageVector = Icons.Default.Camera, contentDescription = null)
                Text(text = "Camera")
            }
        }
    }
}

@Composable
private fun ScreenPreview() {
    HomeScreen(
        state = HomeContract.UiState(),
        forwardAction = {}
    )
}
