package de.rauschdo.photoapp.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rauschdo.photoapp.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.photoapp.ui.navigation.AppNav
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorDestination(
    navigator: AppNav,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val state: EditorContract.UiState by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(LAUNCH_EVENTS_FOR_NAVIGATION) {
        viewModel.navigator.onEach { navigationRequest ->
            when (navigationRequest) {
                else -> {}
            }
        }.collect()
    }
    EditorScreen(
        state = state,
        forwardAction = { action -> viewModel.setAction(action) }
    )
}

@Composable
private fun EditorScreen(
    state: EditorContract.UiState,
    forwardAction: (EditorContract.Action) -> Unit
) {
    val scrollState = rememberScrollState()
    val modifier = Modifier
    val maxWidthModifier = modifier.fillMaxWidth()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var accentColors = listOf<Color>(
        Color(0xFF7ACA8F),
        Color(0xFFa783bc),
        Color(0xFFF77262),
        Color(0xFFFFCF01)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Row(Modifier.height(6.dp)) {
            val accentModifierBase: Modifier = if (screenWidth > 400.dp) {
                accentColors = accentColors.plus(accentColors)
                Modifier.weight(1f)
            } else {
                Modifier.width(100.dp)
            }
            accentColors.forEachIndexed { _, color ->
                Box(modifier = accentModifierBase.background(color))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "EditorScreen: TODO...")
    }
}

@Composable
private fun ScreenPreview() {
    EditorScreen(
        state = EditorContract.UiState(),
        forwardAction = {}
    )
}
