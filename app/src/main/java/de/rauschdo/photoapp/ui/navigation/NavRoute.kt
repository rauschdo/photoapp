package de.rauschdo.photoapp.ui.navigation

class NavRoute(val dest: AppNavDest) {
    val route: String = dest.destinationTarget()
}

fun AppNavDest.navRoute() = NavRoute(this)