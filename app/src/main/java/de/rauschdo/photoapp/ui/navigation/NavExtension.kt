package de.rauschdo.photoapp.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

var START_DESTINATION = AppNavDest.Home

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    start: AppNavDest = START_DESTINATION,
    builder: NavGraphBuilder.() -> Unit
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = start.destinationTarget(),
        modifier = modifier,
        builder = builder
    )
}

/**
 * Bridge to [NavGraphBuilder.composable]
 * Animations may be given, example
 *   enterTransition = {
 *       slideIntoContainer(
 *           AnimatedContentTransitionScope.SlideDirection.Left,
 *           animationSpec = tween(300, easing = EaseInOut)
 *       )
 *   },
 *   exitTransition = {
 *       slideOutOfContainer(
 *           AnimatedContentTransitionScope.SlideDirection.Right,
 *           animationSpec = tween(300, easing = EaseInOut)
 *       )
 *   }
 *
 * @param dest defining target route for Destination
 * @param enterTransition
 * @param exitTransition
 * @param popEnterTransition
 * @param popExitTransition
 */
fun NavGraphBuilder.composable(
    dest: AppNavDest,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable () -> Unit
) = composable(
    route = dest.destinationTarget(),
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    popEnterTransition = popEnterTransition,
    popExitTransition = popExitTransition,
    content = {
        content()
    }
)