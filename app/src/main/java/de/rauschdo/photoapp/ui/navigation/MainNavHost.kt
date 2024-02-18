package de.rauschdo.photoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.rauschdo.photoapp.ui.camera.CameraDestination
import de.rauschdo.photoapp.ui.editor.EditorDestination
import de.rauschdo.photoapp.ui.home.HomeDestination

@Composable
fun MainNavHost(
    modifier: Modifier,
    navigator: AppNav,
) {
    AppNavHost(
        modifier = modifier,
        navController = navigator.navController
    ) {
        composable(AppNavDest.Home) {
            HomeDestination(navigator)
        }
        composable(AppNavDest.Editor) {
            EditorDestination(navigator)
        }
        composable(AppNavDest.Camera) {
            CameraDestination(navigator)
        }
    }
}