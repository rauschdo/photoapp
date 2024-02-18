package de.rauschdo.photoapp.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.rauschdo.photoapp.ui.navigation.AppNav

@Composable
fun rememberMainAppState(navController: NavHostController = rememberNavController()): MainAppState {
    return remember(navController) {
        MainAppState(
            AppNav().apply { this.navController = navController }
        )
    }
}

@Stable
class MainAppState(
    val navigator: AppNav,
)

val LocalAppNav = compositionLocalOf { AppNav() }